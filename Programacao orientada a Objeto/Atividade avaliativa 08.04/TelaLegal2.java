package src;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TelaLegal2 {
    private JPanel Panel1;
    private JTable table1;
    private JButton deletarButton;
    private JButton cadastrarButton;
    private JButton importarButton;
    private JLabel TituloPG;
    private JButton alterarButton;
    private JTextField textFieldNome;
    private JTextField textFieldEmail;
    private JTextField textFieldTelefone;
    private JTextField textFieldCidade;
    private JButton Salvar;
    private JPanel PainelCad;

    private List<cliente> listaClientes = new ArrayList<>();
    private File arquivoAtual;

    public TelaLegal2() {
        inicializarComponentes();
        PainelCad.setVisible(false);
        configurarTabela();

        importarButton.addActionListener(e -> selecionarECarregarCSV());
        cadastrarButton.addActionListener(e -> cadastrarCliente());
        alterarButton.addActionListener(e -> abrirEdicaoCliente());
        Salvar.addActionListener(e -> salvarAlteracao());
        deletarButton.addActionListener(e -> deletarCliente());
    }

    private void inicializarComponentes() {
        Panel1 = new JPanel(new BorderLayout(10, 10));

        TituloPG = new JLabel("Lista de Cliente", SwingConstants.CENTER);
        Panel1.add(TituloPG, BorderLayout.NORTH);

        table1 = new JTable();
        JScrollPane scrollPane = new JScrollPane(table1);
        Panel1.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        alterarButton = new JButton("Alterar");
        importarButton = new JButton("Importar");
        cadastrarButton = new JButton("Cadastrar");
        deletarButton = new JButton("Deletar");
        alterarButton.setMnemonic('A');
        importarButton.setMnemonic('I');
        cadastrarButton.setMnemonic('C');
        deletarButton.setMnemonic('D');

        painelBotoes.add(alterarButton);
        painelBotoes.add(importarButton);
        painelBotoes.add(cadastrarButton);
        painelBotoes.add(deletarButton);
        Panel1.add(painelBotoes, BorderLayout.SOUTH);

        PainelCad = new JPanel(new GridLayout(5, 2, 6, 6));
        textFieldNome = new JTextField();
        textFieldEmail = new JTextField();
        textFieldTelefone = new JTextField();
        textFieldCidade = new JTextField();
        Salvar = new JButton("Salvar");
        Salvar.setMnemonic('S');

        PainelCad.add(new JLabel("Nome"));
        PainelCad.add(textFieldNome);
        PainelCad.add(new JLabel("Email"));
        PainelCad.add(textFieldEmail);
        PainelCad.add(new JLabel("Telefone"));
        PainelCad.add(textFieldTelefone);
        PainelCad.add(new JLabel("Cidade"));
        PainelCad.add(textFieldCidade);
        PainelCad.add(new JLabel(""));
        PainelCad.add(Salvar);

        JPanel painelEdicaoWrapper = new JPanel(new BorderLayout());
        painelEdicaoWrapper.add(PainelCad, BorderLayout.CENTER);
        Panel1.add(painelEdicaoWrapper, BorderLayout.EAST);
    }

    private void configurarTabela() {
        String[] colunas = {
                "ID", "Nome", "Email", "Telefone", "Cidade",
                "Estado", "Data", "Status", "Segmento", "Valor"
        };

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table1.setModel(modelo);
    }

    private void selecionarECarregarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo CSV");

        int escolha = fileChooser.showOpenDialog(null);

        if (escolha == JFileChooser.APPROVE_OPTION) {
            arquivoAtual = fileChooser.getSelectedFile();
            carregarDadosTabela(arquivoAtual);
        }
    }

    private void carregarDadosTabela(File arquivo) {
        String[] colunas = {
                "ID", "Nome", "Email", "Telefone", "Cidade",
                "Estado", "Data", "Status", "Segmento", "Valor"
        };

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        listaClientes.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(",");

                for (int i = 0; i < dados.length; i++) {
                    dados[i] = dados[i].replace("\"", "").trim();
                }

                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                String email = dados[2];
                String telefone = dados[3];
                String cidade = dados[4];
                String estado = dados[5];
                String data = dados[6];
                boolean status = dados[7].equalsIgnoreCase("Ativo");
                String segmento = dados[8];
                float valor = Float.parseFloat(dados[9]);

                cliente novoCliente = new cliente(
                        id, nome, email, telefone, cidade, estado, data, status, segmento, valor
                );

                listaClientes.add(novoCliente);

                Object[] linhaTabela = {
                        novoCliente.getID(),
                        novoCliente.getNome(),
                        novoCliente.getEmail(),
                        novoCliente.getTelefone(),
                        novoCliente.getCidade(),
                        novoCliente.getEstado(),
                        novoCliente.getData(),
                        novoCliente.isStatus() ? "Ativo" : "Inativo",
                        novoCliente.getSegmento(),
                        String.format("%.2f", novoCliente.getValor())
                };

                modelo.addRow(linhaTabela);
            }

            table1.setModel(modelo);
            JOptionPane.showMessageDialog(null, "Total de " + listaClientes.size() + " clientes carregados!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao ler arquivo: " + ex.getMessage());
        }
    }

    private void cadastrarCliente() {
        try {
            String idStr = JOptionPane.showInputDialog("ID:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr);

            String nome = JOptionPane.showInputDialog("Nome:");
            if (nome == null) return;

            String email = JOptionPane.showInputDialog("Email:");
            if (email == null) return;

            String telefone = JOptionPane.showInputDialog("Telefone:");
            if (telefone == null) return;

            String cidade = JOptionPane.showInputDialog("Cidade:");
            if (cidade == null) return;

            String estado = JOptionPane.showInputDialog("Estado:");
            if (estado == null) return;

            String data = JOptionPane.showInputDialog("Data:");
            if (data == null) return;

            String statusStr = JOptionPane.showInputDialog("Status (Ativo/Inativo):");
            if (statusStr == null) return;
            boolean status = statusStr.equalsIgnoreCase("Ativo");

            String segmento = JOptionPane.showInputDialog("Segmento:");
            if (segmento == null) return;

            String valorStr = JOptionPane.showInputDialog("Valor:");
            if (valorStr == null) return;
            float valor = Float.parseFloat(valorStr);

            cliente novoCliente = new cliente(id, nome, email, telefone, cidade, estado, data, status, segmento, valor);
            listaClientes.add(novoCliente);

            DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
            Object[] linha = {
                    novoCliente.getID(),
                    novoCliente.getNome(),
                    novoCliente.getEmail(),
                    novoCliente.getTelefone(),
                    novoCliente.getCidade(),
                    novoCliente.getEstado(),
                    novoCliente.getData(),
                    novoCliente.isStatus() ? "Ativo" : "Inativo",
                    novoCliente.getSegmento(),
                    String.format("%.2f", novoCliente.getValor())
            };

            modelo.addRow(linha);

            if (arquivoAtual != null) {
                salvarDadosNoCSV();
            }

            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    private void abrirEdicaoCliente() {
        int linhaSelecionada = table1.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente para alterar.");
            return;
        }

        PainelCad.setVisible(true);

        textFieldNome.setText(table1.getValueAt(linhaSelecionada, 1).toString());
        textFieldEmail.setText(table1.getValueAt(linhaSelecionada, 2).toString());
        textFieldTelefone.setText(table1.getValueAt(linhaSelecionada, 3).toString());
        textFieldCidade.setText(table1.getValueAt(linhaSelecionada, 4).toString());
    }

    private void salvarAlteracao() {
        int linhaSelecionada = table1.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecione uma linha da tabela.");
            return;
        }

        cliente c = listaClientes.get(linhaSelecionada);

        c.setNome(textFieldNome.getText());
        c.setEmail(textFieldEmail.getText());
        c.setTelefone(textFieldTelefone.getText());
        c.setCidade(textFieldCidade.getText());

        table1.setValueAt(c.getNome(), linhaSelecionada, 1);
        table1.setValueAt(c.getEmail(), linhaSelecionada, 2);
        table1.setValueAt(c.getTelefone(), linhaSelecionada, 3);
        table1.setValueAt(c.getCidade(), linhaSelecionada, 4);

        if (arquivoAtual != null) {
            salvarDadosNoCSV();
        }

        PainelCad.setVisible(false);
        limparCampos();

        JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso!");
    }

    private void deletarCliente() {
        int linhaSelecionada = table1.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecione um cliente para excluir.");
            return;
        }

        String nomeCliente = table1.getValueAt(linhaSelecionada, 1).toString();

        int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Deseja realmente excluir o cliente " + nomeCliente + "?",
                "Confirmação de exclusão",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacao == JOptionPane.YES_OPTION) {
            listaClientes.remove(linhaSelecionada);

            DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
            modelo.removeRow(linhaSelecionada);

            if (arquivoAtual != null) {
                salvarDadosNoCSV();
            }

            JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!");
        }
    }

    private void salvarDadosNoCSV() {
        if (arquivoAtual == null) {
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoAtual))) {
            bw.write("\"ID_Cliente\",\"Nome\",\"Email\",\"Telefone\",\"Cidade\",\"Estado\",\"Data_Cadastro\",\"Status\",\"Segmento\",\"Valor_Total_Compras\"");
            bw.newLine();

            for (cliente c : listaClientes) {
                String linha = String.format(
                        "\"%d\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%.2f\"",
                        c.getID(),
                        c.getNome(),
                        c.getEmail(),
                        c.getTelefone(),
                        c.getCidade(),
                        c.getEstado(),
                        c.getData(),
                        c.isStatus() ? "Ativo" : "Inativo",
                        c.getSegmento(),
                        c.getValor()
                );

                bw.write(linha);
                bw.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar CSV: " + e.getMessage());
        }
    }

    private void limparCampos() {
        textFieldNome.setText("");
        textFieldEmail.setText("");
        textFieldTelefone.setText("");
        textFieldCidade.setText("");
    }

    public JPanel getPainel1() {
        return Panel1;
    }
}