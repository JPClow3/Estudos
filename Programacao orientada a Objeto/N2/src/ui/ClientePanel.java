package ui;

import model.Cliente;
import service.ClienteService;
import service.ValidationException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

public class ClientePanel extends JPanel {
    private final ClienteService service;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField nomeField;
    private final JTextField emailField;
    private Integer selectedId;

    public ClientePanel(ClienteService service) {
        this.service = service;
        this.setLayout(new BorderLayout(8, 8));

        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table = new JTable(tableModel);
        this.table.setRowHeight(24);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.getSelectionModel().addListSelectionListener(e -> preencherFormulario());

        this.nomeField = new JTextField();
        this.emailField = new JTextField();
        this.nomeField.setToolTipText("Nome completo do cliente");
        this.emailField.setToolTipText("Ex.: aluno@email.com");

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        formPanel.setBorder(BorderFactory.createTitledBorder("Formulario de Cliente"));
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        JButton salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> salvar());
        JButton removerButton = new JButton("Remover");
        removerButton.addActionListener(e -> remover());
        JButton limparButton = new JButton("Limpar");
        limparButton.addActionListener(e -> limparFormulario());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(salvarButton);
        buttonPanel.add(removerButton);
        buttonPanel.add(limparButton);

        JPanel bottomPanel = new JPanel(new BorderLayout(6, 6));
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Clientes Cadastrados"));
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void salvar() {
        try {
            if (selectedId == null) {
                service.criar(nomeField.getText(), emailField.getText());
            } else {
                service.atualizar(selectedId, nomeField.getText(), emailField.getText());
            }

            if (nomeField.getText() != null && nomeField.getText().toLowerCase().contains("gustavo")) {
                JOptionPane.showMessageDialog(this, "Nome classico detectado: Gustavo desbloqueou bonus!", "ShopControl", JOptionPane.INFORMATION_MESSAGE);
            }

            refreshTable();
            limparFormulario();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validacao", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void remover() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para remover.");
            return;
        }
        try {
            service.remover(selectedId);
            refreshTable();
            limparFormulario();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validacao", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void preencherFormulario() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        selectedId = (Integer) tableModel.getValueAt(row, 0);
        nomeField.setText((String) tableModel.getValueAt(row, 1));
        emailField.setText((String) tableModel.getValueAt(row, 2));
    }

    private void limparFormulario() {
        selectedId = null;
        table.clearSelection();
        nomeField.setText("");
        emailField.setText("");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Cliente> clientes = service.listar();
        for (Cliente cliente : clientes) {
            tableModel.addRow(new Object[]{cliente.getId(), cliente.getNome(), cliente.getEmail()});
        }
    }
}


