package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Objeto;
import repository.ObjetoRepository;

public class ObjetosPanel extends JPanel {
    private JTextField txtNome;
    private JTextField txtCategoria;
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JCheckBox chkDisponivel;
    private JTable tabelaObjetos;
    private DefaultTableModel tableModel;
    
    private ObjetoRepository objetoRepository;
    private Integer selectedId = null;

    public ObjetosPanel(ObjetoRepository objetoRepository) {
        this.objetoRepository = objetoRepository;
        setLayout(new BorderLayout(10, 10));
        setBackground(StyleGuide.FUNDO_PRINCIPAL);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(StyleGuide.FUNDO_PRINCIPAL);
        topPanel.add(initForm(), BorderLayout.NORTH);
        topPanel.add(initButtons(), BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(initTable(), BorderLayout.CENTER);
        
        carregarDados();
    }

    private JPanel initForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(StyleGuide.FUNDO_PRINCIPAL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels
        JLabel lblNome = createLabel("Nome:");
        JLabel lblCategoria = createLabel("Categoria:");
        JLabel lblDescricao = createLabel("Descrição:");
        JLabel lblValor = createLabel("Valor (R$):");

        // Fields
        txtNome = createTextField(20);
        txtCategoria = createTextField(20);
        txtDescricao = createTextField(20);
        txtValor = createTextField(20);
        chkDisponivel = new JCheckBox("Disponível");
        chkDisponivel.setBackground(StyleGuide.FUNDO_PRINCIPAL);
        chkDisponivel.setFont(StyleGuide.FONTE_TEXTO);
        chkDisponivel.setForeground(StyleGuide.TEXTO_PRINCIPAL);
        chkDisponivel.setSelected(true);

        // Row 0: Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lblNome, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        formPanel.add(txtNome, gbc);

        // Row 1: Categoria
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lblCategoria, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        formPanel.add(txtCategoria, gbc);

        // Row 2: Descricao
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(lblDescricao, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        formPanel.add(txtDescricao, gbc);

        // Row 3: Valor
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(lblValor, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1;
        formPanel.add(txtValor, gbc);

        // Row 4: Disponivel
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1;
        formPanel.add(chkDisponivel, gbc);

        return formPanel;
    }

    private JPanel initButtons() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(StyleGuide.FUNDO_PRINCIPAL);

        JButton btnSalvar = createButton("Salvar", StyleGuide.COR_PRIMARIA, StyleGuide.COR_PRIMARIA_HOVER, StyleGuide.BRANCO);
        JButton btnEditar = createButton("Editar", StyleGuide.COR_SECUNDARIA, StyleGuide.COR_SECUNDARIA_HOVER, StyleGuide.BRANCO);
        JButton btnExcluir = createButton("Excluir", StyleGuide.ERRO_ATRASO, StyleGuide.ERRO_ATRASO_HOVER, StyleGuide.BRANCO);
        JButton btnLimpar = createButton("Limpar", StyleGuide.TEXTO_SECUNDARIO, StyleGuide.TEXTO_SECUNDARIO_HOVER, StyleGuide.BRANCO);
        JButton btnAtualizar = createButton("Atualizar", StyleGuide.TEXTO_PRINCIPAL, StyleGuide.TEXTO_PRINCIPAL_HOVER, StyleGuide.BRANCO);

        btnSalvar.addActionListener(e -> salvarObjeto());
        btnEditar.addActionListener(e -> editarObjeto());
        btnExcluir.addActionListener(e -> excluirObjeto());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnAtualizar.addActionListener(e -> carregarDados());

        btnPanel.add(btnSalvar);
        btnPanel.add(btnEditar);
        btnPanel.add(btnExcluir);
        btnPanel.add(btnLimpar);
        btnPanel.add(btnAtualizar);

        return btnPanel;
    }

    private JScrollPane initTable() {
        String[] columns = {"ID", "Nome", "Categoria", "Valor", "Disponibilidade"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaObjetos = new JTable(tableModel);
        tabelaObjetos.setFont(StyleGuide.FONTE_TABELA);
        tabelaObjetos.setRowHeight(30);
        tabelaObjetos.getTableHeader().setFont(StyleGuide.FONTE_TEXTO);
        tabelaObjetos.getTableHeader().setBackground(StyleGuide.COR_PRIMARIA);
        tabelaObjetos.getTableHeader().setForeground(StyleGuide.BRANCO);
        tabelaObjetos.setDefaultRenderer(Object.class, new ModernTableRenderer());
        tabelaObjetos.setShowGrid(false);
        tabelaObjetos.setIntercellSpacing(new Dimension(0, 0));
        
        // Ajuste de largura das colunas
        tabelaObjetos.getColumnModel().getColumn(0).setMaxWidth(60); // ID
        tabelaObjetos.getColumnModel().getColumn(0).setPreferredWidth(60);
        
        tabelaObjetos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaObjetos.getSelectedRow() != -1) {
                int row = tabelaObjetos.getSelectedRow();
                selectedId = (Integer) tableModel.getValueAt(row, 0);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaObjetos);
        scrollPane.getViewport().setBackground(StyleGuide.FUNDO_PRINCIPAL);
        
        return scrollPane;
    }
    
    public void refreshData() {
        carregarDados();
    }
    
    private void carregarDados() {
        tableModel.setRowCount(0);
        List<Objeto> objetos = objetoRepository.listar();
        for (Objeto obj : objetos) {
            tableModel.addRow(new Object[]{
                obj.getId(),
                obj.getNome(),
                obj.getCategoria(),
                String.format("R$ %.2f", obj.getValor()),
                obj.isDisponivel() ? "Sim" : "Não"
            });
        }
        limparFormulario();
    }
    
    private void salvarObjeto() {
        String nome = txtNome.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String descricao = txtDescricao.getText().trim();
        boolean disponivel = chkDisponivel.isSelected();
        
        if (nome.isEmpty() || categoria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Categoria são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Double valor = 0.0;
        try {
            String valorTxt = txtValor.getText().trim().replace(",", ".");
            if (!valorTxt.isEmpty()) {
                valor = Double.parseDouble(valorTxt);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O valor deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Objeto> objetos = objetoRepository.listar();
        
        if (selectedId == null) {
            // Novo objeto
            int novoId = objetoRepository.proximoId();
            objetos.add(new Objeto(novoId, nome, categoria, descricao, disponivel, valor));
        } else {
            // Atualizar objeto existente
            for (int i = 0; i < objetos.size(); i++) {
                if (objetos.get(i).getId() == selectedId) {
                    objetos.set(i, new Objeto(selectedId, nome, categoria, descricao, disponivel, valor));
                    break;
                }
            }
        }
        
        objetoRepository.salvarTodos(objetos);
        carregarDados();
        JOptionPane.showMessageDialog(this, "Objeto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editarObjeto() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um objeto na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Pega do banco de dados para ter a descricao (que nao esta na tabela)
        List<Objeto> objetos = objetoRepository.listar();
        for (Objeto obj : objetos) {
            if (obj.getId() == selectedId) {
                txtNome.setText(obj.getNome());
                txtCategoria.setText(obj.getCategoria());
                txtDescricao.setText(obj.getDescricao());
                txtValor.setText(String.format("%.2f", obj.getValor()));
                chkDisponivel.setSelected(obj.isDisponivel());
                break;
            }
        }
    }
    
    private void excluirObjeto() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um objeto na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este objeto?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Objeto> objetos = objetoRepository.listar();
            objetos.removeIf(o -> o.getId() == selectedId);
            objetoRepository.salvarTodos(objetos);
            carregarDados();
            JOptionPane.showMessageDialog(this, "Objeto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtCategoria.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        chkDisponivel.setSelected(true);
        selectedId = null;
        tabelaObjetos.clearSelection();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(StyleGuide.FONTE_LABEL);
        label.setForeground(StyleGuide.TEXTO_PRINCIPAL);
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(StyleGuide.FONTE_TEXTO);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return textField;
    }

    private JButton createButton(String text, Color bg, Color hoverBg, Color fg) {
        return new ModernButton(text, bg, hoverBg, fg);
    }
}
