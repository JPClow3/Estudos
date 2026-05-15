package ui;

import model.Categoria;
import service.CategoriaService;
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

public class CategoriaPanel extends JPanel {
    private final CategoriaService service;
    private final Runnable onDataChanged;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField nomeField;
    private Integer selectedId;

    public CategoriaPanel(CategoriaService service, Runnable onDataChanged) {
        this.service = service;
        this.onDataChanged = onDataChanged;
        this.setLayout(new BorderLayout(8, 8));

        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0) {
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
        this.nomeField.setToolTipText("Ex.: Informatica");

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 6, 6));
        formPanel.setBorder(BorderFactory.createTitledBorder("Formulario de Categoria"));
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);

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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Categorias Cadastradas"));
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private void salvar() {
        try {
            if (selectedId == null) {
                service.criar(nomeField.getText());
            } else {
                service.atualizar(selectedId, nomeField.getText());
            }
            refreshTable();
            limparFormulario();
            onDataChanged.run();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validacao", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void remover() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para remover.");
            return;
        }
        try {
            service.remover(selectedId);
            refreshTable();
            limparFormulario();
            onDataChanged.run();
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
    }

    private void limparFormulario() {
        selectedId = null;
        table.clearSelection();
        nomeField.setText("");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Categoria> categorias = service.listar();
        for (Categoria categoria : categorias) {
            tableModel.addRow(new Object[]{categoria.getId(), categoria.getNome()});
        }
    }
}


