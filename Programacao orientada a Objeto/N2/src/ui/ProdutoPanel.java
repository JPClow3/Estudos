package ui;

import model.Categoria;
import model.Produto;
import service.ProdutoService;
import service.ValidationException;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdutoPanel extends JPanel {
    private final ProdutoService service;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField nomeField;
    private final JTextField precoField;
    private final JComboBox<CategoriaItem> categoriaCombo;
    private final Map<Integer, Integer> categoriaPorProduto = new HashMap<>();
    private Integer selectedId;

    public ProdutoPanel(ProdutoService service) {
        this.service = service;
        this.setLayout(new BorderLayout(8, 8));

        this.tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Preco", "Categoria"}, 0) {
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
        this.precoField = new JTextField();
        this.categoriaCombo = new JComboBox<>();
        this.nomeField.setToolTipText("Ex.: Mouse sem fio");
        this.precoField.setToolTipText("Aceita 199.90 ou 199,90");

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 6, 6));
        formPanel.setBorder(BorderFactory.createTitledBorder("Formulario de Produto"));
        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Preco:"));
        formPanel.add(precoField);
        formPanel.add(new JLabel("Categoria:"));
        formPanel.add(categoriaCombo);

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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshData();
    }

    public void refreshData() {
        refreshCategorias();
        refreshTable();
    }

    private void refreshCategorias() {
        categoriaCombo.removeAllItems();
        List<Categoria> categorias = service.listarCategorias();
        for (Categoria categoria : categorias) {
            categoriaCombo.addItem(new CategoriaItem(categoria.getId(), categoria.getNome()));
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        categoriaPorProduto.clear();
        List<Categoria> categorias = service.listarCategorias();
        Map<Integer, String> categoriasById = new HashMap<>();
        for (Categoria categoria : categorias) {
            categoriasById.put(categoria.getId(), categoria.getNome());
        }

        List<Produto> produtos = service.listar();
        for (Produto produto : produtos) {
            String categoriaNome = categoriasById.getOrDefault(produto.getIdCategoria(), "Sem categoria");
            categoriaPorProduto.put(produto.getId(), produto.getIdCategoria());
            tableModel.addRow(new Object[]{produto.getId(), produto.getNome(), produto.getPreco(), categoriaNome});
        }
    }

    private void salvar() {
        try {
            CategoriaItem selectedCategoria = (CategoriaItem) categoriaCombo.getSelectedItem();
            if (selectedCategoria == null) {
                throw new ValidationException("Cadastre uma categoria antes de criar produtos.");
            }
            double preco = Double.parseDouble(precoField.getText().trim().replace(",", "."));
            if (selectedId == null) {
                service.criar(nomeField.getText(), preco, selectedCategoria.getId());
            } else {
                service.atualizar(selectedId, nomeField.getText(), preco, selectedCategoria.getId());
            }

            if (nomeField.getText() != null && nomeField.getText().toLowerCase().contains("gustavo")) {
                JOptionPane.showMessageDialog(this, "Easter egg: produto aprovado pelo Prof. Gustavo!", "ShopControl", JOptionPane.INFORMATION_MESSAGE);
            }

            refreshTable();
            limparFormulario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preco invalido.", "Validacao", JOptionPane.WARNING_MESSAGE);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validacao", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void remover() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
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
        precoField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        Integer categoriaId = categoriaPorProduto.get(selectedId);

        for (int i = 0; i < categoriaCombo.getItemCount(); i++) {
            CategoriaItem item = categoriaCombo.getItemAt(i);
            if (categoriaId != null && item.getId() == categoriaId) {
                categoriaCombo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limparFormulario() {
        selectedId = null;
        table.clearSelection();
        nomeField.setText("");
        precoField.setText("");
        if (categoriaCombo.getItemCount() > 0) {
            categoriaCombo.setSelectedIndex(0);
        }
    }
}


