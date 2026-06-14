import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Altera o visual (Look and Feel) para se parecer mais com o sistema operacional atual
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bicicletaria - Associação de Clientes e Modelos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setBackground(new Color(245, 245, 245));

            JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setOpaque(false);

            // Título no topo
            JLabel titleLabel = new JLabel("Lista de Clientes e Bicicletas", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titleLabel.setForeground(new Color(40, 40, 40));
            
            // Configuração da Tabela
            String[] colunas = {"Nome do Cliente", "Modelo da Bicicleta"};
            DefaultTableModel tableModel = new DefaultTableModel(colunas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Desativa edição das células
                }
            };
            JTable table = new JTable(tableModel);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setShowGrid(true);
            table.setGridColor(new Color(230, 230, 230));
            table.setSelectionBackground(new Color(173, 216, 230));
            
            // Estilo do cabeçalho da tabela
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setBackground(new Color(70, 130, 180));
            header.setForeground(Color.WHITE);
            ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

            // Alinhamento das células
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

            // Painel inferior para o botão
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottomPanel.setOpaque(false);
            
            JButton importButton = new JButton("Importar Arquivos CSV");
            importButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            importButton.setBackground(new Color(60, 179, 113));
            importButton.setForeground(Color.WHITE);
            importButton.setFocusPainted(false);
            importButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            importButton.setPreferredSize(new Dimension(250, 45));
            
            JLabel statusLabel = new JLabel("Aguardando importação...");
            statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            statusLabel.setForeground(Color.GRAY);

            JPanel buttonAndStatusPanel = new JPanel(new BorderLayout(5, 5));
            buttonAndStatusPanel.setOpaque(false);
            buttonAndStatusPanel.add(importButton, BorderLayout.CENTER);
            buttonAndStatusPanel.add(statusLabel, BorderLayout.SOUTH);
            
            bottomPanel.add(buttonAndStatusPanel);

            importButton.addActionListener((ActionEvent e) -> {
                JFileChooser fileChooser = new JFileChooser(new File("."));
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setDialogTitle("Selecione os arquivos: clientes.csv e bicicletas.csv");

                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    
                    File clientesFile = null;
                    File bicicletasFile = null;
                    
                    for (File file : files) {
                        String name = file.getName().toLowerCase();
                        if (name.contains("cliente")) {
                            clientesFile = file;
                        } else if (name.contains("bicicleta")) {
                            bicicletasFile = file;
                        }
                    }

                    if (clientesFile == null || bicicletasFile == null) {
                        JOptionPane.showMessageDialog(frame, "Por favor, selecione ambos os arquivos (clientes.csv e bicicletas.csv) simultaneamente.", "Erro de Seleção", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        Map<Integer, Bicicleta> bicicletasMap = carregarBicicletas(bicicletasFile);
                        List<Cliente> clientesList = carregarClientes(clientesFile);

                        // Limpa a tabela atual
                        tableModel.setRowCount(0);

                        // Preenche a tabela com os novos dados correlacionados
                        for (Cliente cliente : clientesList) {
                            Bicicleta bicicleta = bicicletasMap.get(cliente.getIdBicicleta());
                            String modelo = (bicicleta != null) ? bicicleta.getModelo() : "Não Encontrado";
                            tableModel.addRow(new Object[]{cliente.getNome(), modelo});
                        }

                        statusLabel.setText(String.format("Sucesso: %d registros carregados.", clientesList.size()));
                        statusLabel.setForeground(new Color(60, 179, 113));

                    } catch (Exception ex) {
                        statusLabel.setText("Erro ao processar arquivos.");
                        statusLabel.setForeground(Color.RED);
                        JOptionPane.showMessageDialog(frame, "Erro ao processar os arquivos: " + ex.getMessage(), "Erro de Leitura", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);

            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }

    private static Map<Integer, Bicicleta> carregarBicicletas(File file) throws IOException {
        Map<Integer, Bicicleta> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Pula o cabeçalho
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] partes = line.split(",");
                if (partes.length >= 2) {
                    // Remove possíveis aspas ou espaços extras (caso o CSV tenha sido salvo estranho)
                    int id = Integer.parseInt(partes[0].replaceAll("[^0-9]", ""));
                    String modelo = partes[1].trim();
                    map.put(id, new Bicicleta(id, modelo));
                }
            }
        }
        return map;
    }

    private static List<Cliente> carregarClientes(File file) throws IOException {
        List<Cliente> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Pula o cabeçalho
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] partes = line.split(",");
                if (partes.length >= 3) {
                    int id = Integer.parseInt(partes[0].replaceAll("[^0-9]", ""));
                    String nome = partes[1].trim();
                    int idBicicleta = Integer.parseInt(partes[2].replaceAll("[^0-9]", ""));
                    list.add(new Cliente(id, nome, idBicicleta));
                }
            }
        }
        return list;
    }
}
