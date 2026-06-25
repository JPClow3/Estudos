package view;

import model.Amigo;
import model.Emprestimo;
import model.Objeto;
import repository.AmigoRepository;
import repository.ObjetoRepository;
import service.LoanService;
import service.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

@SuppressWarnings({"serial", "this-escape"})
public class EmprestimosPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JComboBox<ComboItem> cbObjeto;
    private JComboBox<ComboItem> cbAmigo;
    private JFormattedTextField txtDataEmprestimo;
    private JFormattedTextField txtDataPrevista;
    private JComboBox<String> cbStatus;
    private JTextField txtObservacoes;
    private JTable tabelaEmprestimos;
    private DefaultTableModel tableModel;
    
    private final LoanService loanService;
    private final ObjetoRepository objetoRepository;
    private final AmigoRepository amigoRepository;
    private Integer selectedId = null;
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public EmprestimosPanel(LoanService loanService, ObjetoRepository objetoRepository, AmigoRepository amigoRepository) {
        this.loanService = loanService;
        this.objetoRepository = objetoRepository;
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
        
        carregarComboBoxes();
        carregarDados();
    }

    private JPanel initForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(StyleGuide.FUNDO_PRINCIPAL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels
        JLabel lblObjeto = createLabel("Objeto:");
        JLabel lblAmigo = createLabel("Amigo:");
        JLabel lblDataEmp = createLabel("Data Empréstimo:");
        JLabel lblDataPrev = createLabel("Data Prevista:");
        JLabel lblStatus = createLabel("Status:");
        JLabel lblObservacoes = createLabel("Observações:");

        // Fields
        cbObjeto = new JComboBox<>();
        cbObjeto.setFont(StyleGuide.FONTE_TEXTO);
        
        cbAmigo = new JComboBox<>();
        cbAmigo.setFont(StyleGuide.FONTE_TEXTO);
        
        txtDataEmprestimo = createFormattedTextField("##/##/####");
        txtDataEmprestimo.setText(LocalDate.now().format(DATE_FORMAT));
        txtDataPrevista = createFormattedTextField("##/##/####");
        txtDataPrevista.setText(LocalDate.now().plusDays(7).format(DATE_FORMAT));
        
        String[] statusOptions = {"EMPRESTADO", "DEVOLVIDO", "ATRASADO"};
        cbStatus = new JComboBox<>(statusOptions);
        cbStatus.setFont(StyleGuide.FONTE_TEXTO);
        
        txtObservacoes = createTextField(20);
        lblObjeto.setLabelFor(cbObjeto);
        lblAmigo.setLabelFor(cbAmigo);
        lblDataEmp.setLabelFor(txtDataEmprestimo);
        lblDataPrev.setLabelFor(txtDataPrevista);
        lblStatus.setLabelFor(cbStatus);
        lblObservacoes.setLabelFor(txtObservacoes);

        // Row 0: Objeto / Amigo
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lblObjeto, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        formPanel.add(cbObjeto, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(lblAmigo, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1;
        formPanel.add(cbAmigo, gbc);

        // Row 1: Data Emp / Data Prevista
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lblDataEmp, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        formPanel.add(txtDataEmprestimo, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(lblDataPrev, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1;
        formPanel.add(txtDataPrevista, gbc);

        // Row 2: Status / Observacoes
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(lblStatus, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        formPanel.add(cbStatus, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(lblObservacoes, gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1;
        formPanel.add(txtObservacoes, gbc);

        return formPanel;
    }

    private JPanel initButtons() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(StyleGuide.FUNDO_PRINCIPAL);

        JButton btnSalvar = createButton("Salvar", StyleGuide.COR_PRIMARIA, StyleGuide.BRANCO);
        JButton btnEditar = createButton("Editar", StyleGuide.COR_SECUNDARIA, StyleGuide.BRANCO);
        JButton btnExcluir = createButton("Excluir", StyleGuide.ERRO_ATRASO, StyleGuide.BRANCO);
        JButton btnLimpar = createButton("Limpar", StyleGuide.TEXTO_SECUNDARIO, StyleGuide.BRANCO);
        JButton btnAtualizar = createButton("Atualizar", StyleGuide.TEXTO_PRINCIPAL, StyleGuide.BRANCO);

        btnSalvar.addActionListener(e -> salvarEmprestimo());
        btnEditar.addActionListener(e -> editarEmprestimo());
        btnExcluir.addActionListener(e -> excluirEmprestimo());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnAtualizar.addActionListener(e -> {
            carregarComboBoxes();
            carregarDados();
        });

        btnPanel.add(btnSalvar);
        btnPanel.add(btnEditar);
        btnPanel.add(btnExcluir);
        btnPanel.add(btnLimpar);
        btnPanel.add(btnAtualizar);

        return btnPanel;
    }

    private JScrollPane initTable() {
        String[] columns = {"ID", "Objeto", "Amigo", "Data Emp.", "Data Prev.", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaEmprestimos = new JTable(tableModel);
        tabelaEmprestimos.setFont(StyleGuide.FONTE_TABELA);
        tabelaEmprestimos.setRowHeight(25);
        tabelaEmprestimos.getTableHeader().setFont(StyleGuide.FONTE_TEXTO);
        tabelaEmprestimos.getTableHeader().setDefaultRenderer(new ModernTableHeaderRenderer());
        tabelaEmprestimos.setDefaultRenderer(Object.class, new ModernTableRenderer());
        tabelaEmprestimos.setShowGrid(false);
        tabelaEmprestimos.setIntercellSpacing(new Dimension(0, 0));
        
        // Ajuste de largura das colunas
        tabelaEmprestimos.getColumnModel().getColumn(0).setMaxWidth(60); // ID
        tabelaEmprestimos.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabelaEmprestimos.getColumnModel().getColumn(3).setPreferredWidth(90); // Data Emp
        tabelaEmprestimos.getColumnModel().getColumn(4).setPreferredWidth(90); // Data Prev
        
        tabelaEmprestimos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaEmprestimos.getSelectedRow() != -1) {
                int row = tabelaEmprestimos.getSelectedRow();
                selectedId = (Integer) tableModel.getValueAt(row, 0);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaEmprestimos);
        scrollPane.getViewport().setBackground(StyleGuide.FUNDO_PRINCIPAL);
        scrollPane.setMinimumSize(new Dimension(0, 100));
        
        return scrollPane;
    }
    
    public void refreshData() {
        carregarComboBoxes();
        carregarDados();
    }
    
    private void carregarComboBoxes() {
        try {
            cbObjeto.removeAllItems();
            cbAmigo.removeAllItems();
            for (Objeto obj : objetoRepository.listar()) {
                cbObjeto.addItem(new ComboItem(obj.getId(), obj.getNome()));
            }
            for (Amigo amigo : amigoRepository.listar()) {
                cbAmigo.addItem(new ComboItem(amigo.getId(), amigo.getNome()));
            }
        } catch (RuntimeException exception) {
            showError(exception);
        }
    }
    
    private void carregarDados() {
        try {
            tableModel.setRowCount(0);
            List<Emprestimo> emprestimos = loanService.listarValidado();
            List<Objeto> objetos = objetoRepository.listar();
            List<Amigo> amigos = amigoRepository.listar();
            for (Emprestimo emp : emprestimos) {
                String nomeObjeto = objetos.stream().filter(o -> o.getId() == emp.getObjetoId()).map(Objeto::getNome).findFirst().orElseThrow();
                String nomeAmigo = amigos.stream().filter(a -> a.getId() == emp.getAmigoId()).map(Amigo::getNome).findFirst().orElseThrow();
                tableModel.addRow(new Object[]{
                    emp.getId(),
                    nomeObjeto,
                    nomeAmigo,
                    emp.getDataEmprestimo().format(DATE_FORMAT),
                    emp.getDataPrevistaDevolucao().format(DATE_FORMAT),
                    emp.getStatus()
                });
            }
            limparFormulario();
        } catch (RuntimeException exception) {
            showError(exception);
        }
    }
    
    private void salvarEmprestimo() {
        ComboItem objetoSelecionado = (ComboItem) cbObjeto.getSelectedItem();
        ComboItem amigoSelecionado = (ComboItem) cbAmigo.getSelectedItem();
        
        if (objetoSelecionado == null || amigoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Objeto e um Amigo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalDate dataEmp;
        LocalDate dataPrev;
        try {
            dataEmp = LocalDate.parse(txtDataEmprestimo.getText().trim(), DATE_FORMAT);
            dataPrev = LocalDate.parse(txtDataPrevista.getText().trim(), DATE_FORMAT);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String status = (String) cbStatus.getSelectedItem();
            String observacoes = ValidationUtils.requireSafeText(
                    "Observações", txtObservacoes.getText(), false
            );
            if (selectedId == null) {
                loanService.criar(
                        objetoSelecionado.getId(), amigoSelecionado.getId(), dataEmp, dataPrev, status, observacoes
                );
            } else {
                loanService.atualizar(
                        selectedId, objetoSelecionado.getId(), amigoSelecionado.getId(),
                        dataEmp, dataPrev, status, observacoes
                );
            }
            carregarComboBoxes();
            carregarDados();
            JOptionPane.showMessageDialog(this, "Empréstimo salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException exception) {
            showError(exception);
        }
    }
    
    private void editarEmprestimo() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        carregarComboBoxes();
        
        List<Emprestimo> emprestimos = loanService.listarValidado();
        for (Emprestimo emp : emprestimos) {
            if (emp.getId() == selectedId) {
                // Set cbObjeto
                for (int i = 0; i < cbObjeto.getItemCount(); i++) {
                    if (cbObjeto.getItemAt(i).getId() == emp.getObjetoId()) {
                        cbObjeto.setSelectedIndex(i);
                        break;
                    }
                }
                // Set cbAmigo
                for (int i = 0; i < cbAmigo.getItemCount(); i++) {
                    if (cbAmigo.getItemAt(i).getId() == emp.getAmigoId()) {
                        cbAmigo.setSelectedIndex(i);
                        break;
                    }
                }
                
                txtDataEmprestimo.setText(emp.getDataEmprestimo().format(DATE_FORMAT));
                txtDataPrevista.setText(emp.getDataPrevistaDevolucao().format(DATE_FORMAT));
                cbStatus.setSelectedItem(emp.getStatus());
                txtObservacoes.setText(emp.getObservacoes());
                break;
            }
        }
    }
    
    private void excluirEmprestimo() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este empréstimo?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                loanService.excluir(selectedId);
                carregarComboBoxes();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Empréstimo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException exception) {
                showError(exception);
            }
        }
    }
    
    private void limparFormulario() {
        if (cbObjeto.getItemCount() > 0) cbObjeto.setSelectedIndex(0);
        if (cbAmigo.getItemCount() > 0) cbAmigo.setSelectedIndex(0);
        txtDataEmprestimo.setText(LocalDate.now().format(DATE_FORMAT));
        txtDataPrevista.setText(LocalDate.now().plusDays(7).format(DATE_FORMAT));
        cbStatus.setSelectedIndex(0);
        txtObservacoes.setText("");
        selectedId = null;
        tabelaEmprestimos.clearSelection();
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
        textField.setColumns(15);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return textField;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        return new ModernButton(text, bg, bg.darker(), fg);
    }

    private void showError(RuntimeException exception) {
        JOptionPane.showMessageDialog(this, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    // Helper class para armazenar ID e Nome no JComboBox
    private static class ComboItem {
        private int id;
        private String label;

        public ComboItem(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
