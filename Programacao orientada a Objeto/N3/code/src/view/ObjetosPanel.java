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
import service.LoanService;
import service.ValidationUtils;

@SuppressWarnings({"serial", "this-escape"})
public class ObjetosPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField txtNome;
    private JTextField txtCategoria;
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JCheckBox chkDisponivel;
    private JTable tabelaObjetos;
    private DefaultTableModel tableModel;
    
    private final ObjetoRepository objetoRepository;
    private final LoanService loanService;
    private Integer selectedId = null;

    public ObjetosPanel(ObjetoRepository objetoRepository, LoanService loanService) {
        this.objetoRepository = objetoRepository;
        this.loanService = loanService;
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
        gbc.insets = new Insets(6, 10, 6, 10);

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
        lblNome.setLabelFor(txtNome);
        lblCategoria.setLabelFor(txtCategoria);
        lblDescricao.setLabelFor(txtDescricao);
        lblValor.setLabelFor(txtValor);

        // Row 0: Nome / Categoria
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lblNome, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        formPanel.add(txtNome, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lblCategoria, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1;
        formPanel.add(txtCategoria, gbc);

        // Row 1: Descrição / Valor
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lblDescricao, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        formPanel.add(txtDescricao, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lblValor, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1;
        formPanel.add(txtValor, gbc);

        // Row 2: Disponível
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.weightx = 1;
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
        tabelaObjetos.getTableHeader().setDefaultRenderer(new ModernTableHeaderRenderer());
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
        scrollPane.setMinimumSize(new Dimension(0, 100));
        scrollPane.setPreferredSize(new Dimension(700, 170));
        
        return scrollPane;
    }
    
    public void refreshData() {
        carregarDados();
    }
    
    private void carregarDados() {
        try {
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
        } catch (RuntimeException exception) {
            showError(exception);
        }
    }
    
    private void salvarObjeto() {
        try {
            String nome = ValidationUtils.requireSafeText("Nome", txtNome.getText(), true);
            String categoria = ValidationUtils.requireSafeText("Categoria", txtCategoria.getText(), true);
            String descricao = ValidationUtils.requireSafeText("Descrição", txtDescricao.getText(), false);
            double valor = ValidationUtils.parseNonNegativeFinite("Valor", txtValor.getText());
            boolean disponivel = chkDisponivel.isSelected();
            if (selectedId != null && disponivel && loanService.objetoPossuiEmprestimoAtivo(selectedId)) {
                throw new IllegalArgumentException(
                        "O objeto possui um empréstimo ativo e deve permanecer indisponível."
                );
            }
            List<Objeto> objetos = objetoRepository.listar();

            if (selectedId == null) {
                objetos.add(new Objeto(
                        objetoRepository.proximoId(), nome, categoria, descricao, disponivel, valor
                ));
            } else {
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
        } catch (RuntimeException exception) {
            showError(exception);
        }
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
            try {
                if (loanService.objetoPossuiHistorico(selectedId)) {
                    throw new IllegalArgumentException(
                            "Este objeto não pode ser excluído porque existe no histórico de empréstimos."
                    );
                }
                List<Objeto> objetos = objetoRepository.listar();
                objetos.removeIf(o -> o.getId() == selectedId);
                objetoRepository.salvarTodos(objetos);
                carregarDados();
                JOptionPane.showMessageDialog(this, "Objeto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException exception) {
                showError(exception);
            }
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

    private void showError(RuntimeException exception) {
        JOptionPane.showMessageDialog(this, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
