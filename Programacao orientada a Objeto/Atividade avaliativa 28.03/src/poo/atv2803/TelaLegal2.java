package poo.atv2803;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TelaLegal2 {
    private JLabel TituloPG;
    private JPanel Panel1;
    private JTable table1;
    private JButton deletarButton;
    private JButton cadastrarButton;
    private JButton importarButton;

    private static final String[] COLUNAS = {"ID", "Nome", "Email", "Telefone", "Cidade", "Estado", "Data", "Status", "Segmento", "Valor"};


    public TelaLegal2() {
        inicializarInterface();
        table1.setModel(new DefaultTableModel(COLUNAS, 0));

        importarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selecionarECarregarCSV();
            }
        });

        cadastrarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cadastrarCliente();
            }
        });
    }

    private void inicializarInterface() {
        if (Panel1 != null && table1 != null) {
            return;
        }

        Panel1 = new JPanel(new BorderLayout(8, 8));

        TituloPG = new JLabel("Lista de Cliente", SwingConstants.CENTER);
        Panel1.add(TituloPG, BorderLayout.NORTH);

        table1 = new JTable();
        JScrollPane scrollPane = new JScrollPane(table1);
        Panel1.add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        importarButton = new JButton("Importar");
        cadastrarButton = new JButton("Cadastrar");
        deletarButton = new JButton("Deletar");
        painelBotoes.add(importarButton);
        painelBotoes.add(cadastrarButton);
        painelBotoes.add(deletarButton);
        Panel1.add(painelBotoes, BorderLayout.SOUTH);
    }

    private void selecionarECarregarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        // Define para abrir na pasta atual ou uma específica
        fileChooser.setDialogTitle("Selecione o arquivo CSV");

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = fileChooser.getSelectedFile();
            carregarDadosTabela(arquivoSelecionado);
            //JOptionPane.showMessageDialog(null, "clientes carregados!");

        }
    }

    private final List<cliente> listaClientes = new ArrayList<>();

    private void carregarDadosTabela(File arquivo) {
        DefaultTableModel modelo = new DefaultTableModel(COLUNAS, 0);

        // Limpa a lista atual se estiver carregando um novo arquivo
        listaClientes.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                // Split tratando aspas (mesma lógica anterior)
                String[] dados = linha.split(",");
                for (int i = 0; i < dados.length; i++) {
                    dados[i] = dados[i].replace("\"", "");
                }

                try {
                    // --- CONVERSÃO DE TIPOS PARA O OBJETO ---
                    int id = Integer.parseInt(dados[0]);
                    String nome = dados[1];
                    String email = dados[2];
                    String telefone = dados[3];
                    String cidade = dados[4];
                    String estado = dados[5];
                    String data = dados[6];

                    // Converte "Ativo" para true e qualquer outra coisa para false
                    boolean status = dados[7].equalsIgnoreCase("Ativo");

                    String segmento = dados[8];
                    float valor = Float.parseFloat(dados[9]);

                    // 1. Criar o objeto cliente
                    cliente novoCliente = new cliente(id, nome, email, telefone, cidade, estado, data, status, segmento, valor);

                    // 2. Adicionar na nossa lista de controle
                    listaClientes.add(novoCliente);

                    // 3. Adicionar na JTable (Visualização)
                    adicionarClienteNaTabela(modelo, novoCliente);

                } catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
                    System.err.println("Erro ao processar linha: " + ex.getMessage());
                    // Pula linhas com erro de formato
                }
            }

            table1.setModel(modelo);
            JOptionPane.showMessageDialog(null, "Total de " + listaClientes.size() + " clientes carregados!");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao ler arquivo: " + ex.getMessage());
        }
    }

    private void cadastrarCliente() {
        JTextField campoNome = new JTextField();
        JTextField campoEmail = new JTextField();
        JTextField campoTelefone = new JTextField();
        JTextField campoCidade = new JTextField();
        JTextField campoEstado = new JTextField();
        JTextField campoData = new JTextField();
        JComboBox<String> campoStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
        JTextField campoSegmento = new JTextField();
        JTextField campoValor = new JTextField();

        JPanel painelFormulario = new JPanel(new GridLayout(0, 2, 8, 6));
        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(campoNome);
        painelFormulario.add(new JLabel("Email:"));
        painelFormulario.add(campoEmail);
        painelFormulario.add(new JLabel("Telefone:"));
        painelFormulario.add(campoTelefone);
        painelFormulario.add(new JLabel("Cidade:"));
        painelFormulario.add(campoCidade);
        painelFormulario.add(new JLabel("Estado:"));
        painelFormulario.add(campoEstado);
        painelFormulario.add(new JLabel("Data:"));
        painelFormulario.add(campoData);
        painelFormulario.add(new JLabel("Status:"));
        painelFormulario.add(campoStatus);
        painelFormulario.add(new JLabel("Segmento:"));
        painelFormulario.add(campoSegmento);
        painelFormulario.add(new JLabel("Valor:"));
        painelFormulario.add(campoValor);

        int resultado = JOptionPane.showConfirmDialog(
                null,
                painelFormulario,
                "Cadastrar cliente",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return;
        }

        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String cidade = campoCidade.getText().trim();
        String estado = campoEstado.getText().trim();
        String data = campoData.getText().trim();
        String segmento = campoSegmento.getText().trim();
        String valorTexto = campoValor.getText().trim().replace(',', '.');
        boolean status = "Ativo".equals(campoStatus.getSelectedItem());

        if (nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nome e Email sao obrigatorios.");
            return;
        }

        float valor;
        try {
            valor = Float.parseFloat(valorTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Valor invalido. Informe um numero, ex: 2500.50");
            return;
        }

        int novoId = gerarProximoId();
        cliente novoCliente = new cliente(novoId, nome, email, telefone, cidade, estado, data, status, segmento, valor);
        listaClientes.add(novoCliente);

        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
        adicionarClienteNaTabela(modelo, novoCliente);

        JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso! ID: " + novoId);
    }

    private int gerarProximoId() {
        int maxId = 0;
        for (cliente c : listaClientes) {
            if (c.getID() > maxId) {
                maxId = c.getID();
            }
        }
        return maxId + 1;
    }

    private void adicionarClienteNaTabela(DefaultTableModel modelo, cliente cliente) {
        Object[] linhaTabela = {
                cliente.getID(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getCidade(),
                cliente.getEstado(),
                cliente.getData(),
                cliente.isStatus() ? "Ativo" : "Inativo",
                cliente.getSegmento(),
                String.format("%.2f", cliente.getValor())
        };
        modelo.addRow(linhaTabela);
    }


    public JPanel getPainel1() {
        return Panel1;
    }
}
