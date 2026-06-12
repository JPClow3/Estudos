package view;

import model.Amigo;
import repository.AmigoRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.List;

public class AmigosPanel extends JPanel {
    private JTextField txtNome;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTable tabelaAmigos;
    private DefaultTableModel tableModel;
    
    private AmigoRepository amigoRepository;
    private Integer selectedId = null;

    public AmigosPanel(AmigoRepository amigoRepository) {
        this.amigoRepository = amigoRepository;
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
        JLabel lblTelefone = createLabel("Telefone:");
        JLabel lblEmail = createLabel("E-mail:");

        // Fields
        txtNome = createTextField(20);
        txtTelefone = createFormattedTextField("(##) #####-####");
        txtEmail = createTextField(20);

        // Row 0: Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lblNome, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        formPanel.add(txtNome, gbc);

        // Row 1: Telefone
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lblTelefone, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        formPanel.add(txtTelefone, gbc);

        // Row 2: Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(lblEmail, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        formPanel.add(txtEmail, gbc);

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

        btnSalvar.addActionListener(e -> salvarAmigo());
        btnEditar.addActionListener(e -> editarAmigo());
        btnExcluir.addActionListener(e -> excluirAmigo());
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
        String[] columns = {"ID", "Nome", "Telefone", "E-mail"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAmigos = new JTable(tableModel);
        tabelaAmigos.setFont(StyleGuide.FONTE_TABELA);
        tabelaAmigos.setRowHeight(30);
        tabelaAmigos.getTableHeader().setFont(StyleGuide.FONTE_TEXTO);
        tabelaAmigos.getTableHeader().setBackground(StyleGuide.COR_PRIMARIA);
        tabelaAmigos.getTableHeader().setForeground(StyleGuide.BRANCO);
        tabelaAmigos.setDefaultRenderer(Object.class, new ModernTableRenderer());
        tabelaAmigos.setShowGrid(false);
        tabelaAmigos.setIntercellSpacing(new Dimension(0, 0));
        
        // Ajuste de largura das colunas
        tabelaAmigos.getColumnModel().getColumn(0).setMaxWidth(60); // ID
        tabelaAmigos.getColumnModel().getColumn(0).setPreferredWidth(60);
        
        tabelaAmigos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaAmigos.getSelectedRow() != -1) {
                int row = tabelaAmigos.getSelectedRow();
                selectedId = (Integer) tableModel.getValueAt(row, 0);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaAmigos);
        scrollPane.getViewport().setBackground(StyleGuide.FUNDO_PRINCIPAL);
        
        return scrollPane;
    }
    
    public void refreshData() {
        carregarDados();
    }
    
    private void carregarDados() {
        tableModel.setRowCount(0);
        List<Amigo> amigos = amigoRepository.listar();
        for (Amigo amigo : amigos) {
            tableModel.addRow(new Object[]{
                amigo.getId(),
                amigo.getNome(),
                amigo.getTelefone(),
                amigo.getEmail()
            });
        }
        limparFormulario();
    }
    
    private void salvarAmigo() {
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        
        if (nome.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Telefone são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Amigo> amigos = amigoRepository.listar();
        
        if (selectedId == null) {
            // Novo amigo
            int novoId = amigoRepository.proximoId();
            amigos.add(new Amigo(novoId, nome, telefone, email));
        } else {
            // Atualizar amigo existente
            for (int i = 0; i < amigos.size(); i++) {
                if (amigos.get(i).getId() == selectedId) {
                    amigos.set(i, new Amigo(selectedId, nome, telefone, email));
                    break;
                }
            }
        }
        
        amigoRepository.salvarTodos(amigos);
        carregarDados();
        JOptionPane.showMessageDialog(this, "Amigo salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editarAmigo() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um amigo na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int row = tabelaAmigos.getSelectedRow();
        txtNome.setText((String) tableModel.getValueAt(row, 1));
        txtTelefone.setText((String) tableModel.getValueAt(row, 2));
        txtEmail.setText((String) tableModel.getValueAt(row, 3));
    }
    
    private void excluirAmigo() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um amigo na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este amigo?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Amigo> amigos = amigoRepository.listar();
            amigos.removeIf(a -> a.getId() == selectedId);
            amigoRepository.salvarTodos(amigos);
            carregarDados();
            JOptionPane.showMessageDialog(this, "Amigo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        selectedId = null;
        tabelaAmigos.clearSelection();
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

    private JFormattedTextField createFormattedTextField(String mask) {
        JFormattedTextField textField;
        try {
            MaskFormatter formatter = new MaskFormatter(mask);
            formatter.setPlaceholderCharacter('_');
            textField = new JFormattedTextField(formatter);
        } catch (ParseException e) {
            textField = new JFormattedTextField();
        }
        textField.setFont(StyleGuide.FONTE_TEXTO);
        textField.setColumns(20);
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
