package atividade1804;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BicicletaApp extends JFrame {
    private final BicicletaService service;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final String[] colunas = {"Marca", "Qtd Rodas", "Modelo", "Velocidade", "Num Marchas", "Bagageiro"};

    public BicicletaApp() {
        super("Gerenciador de Bicicletas");
        service = new BicicletaService("bicicletas.csv");
        tableModel = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JPanel botoes = new JPanel();
        botoes.add(btnAdicionar);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
        carregarTabela();

        btnAdicionar.addActionListener(e -> adicionar());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        for (Bicicleta b : service.lerBicicletas()) {
            tableModel.addRow(b.toArray());
        }
    }

    private void adicionar() {
        Bicicleta b = dialogoBicicleta(null);
        if (b != null) {
            List<Bicicleta> lista = service.lerBicicletas();
            lista.add(b);
            service.salvarBicicletas(lista);
            carregarTabela();
        }
    }

    private void editar() {
        int sel = table.getSelectedRow();
        if (sel == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para editar.");
            return;
        }
        List<Bicicleta> lista = service.lerBicicletas();
        Bicicleta atual = lista.get(sel);
        Bicicleta editada = dialogoBicicleta(atual);
        if (editada != null) {
            lista.set(sel, editada);
            service.salvarBicicletas(lista);
            carregarTabela();
        }
    }

    private void excluir() {
        int sel = table.getSelectedRow();
        if (sel == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para excluir.");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this, "Confirma exclusão?", "Excluir", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            List<Bicicleta> lista = service.lerBicicletas();
            lista.remove(sel);
            service.salvarBicicletas(lista);
            carregarTabela();
        }
    }

    private Bicicleta dialogoBicicleta(Bicicleta b) {
        JTextField marca = new JTextField(b != null ? b.getMarca() : "");
        JTextField qtdRodas = new JTextField(b != null ? String.valueOf(b.getQtdRodas()) : "");
        JTextField modelo = new JTextField(b != null ? b.getModelo() : "");
        JTextField velocidade = new JTextField(b != null ? String.valueOf(b.getVelocidade()) : "");
        JTextField numMarchas = new JTextField(b != null ? String.valueOf(b.getNumMarchas()) : "");
        JCheckBox bagageiro = new JCheckBox("Bagageiro", b != null && b.isBagageiro());
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Marca:")); panel.add(marca);
        panel.add(new JLabel("Qtd Rodas:")); panel.add(qtdRodas);
        panel.add(new JLabel("Modelo:")); panel.add(modelo);
        panel.add(new JLabel("Velocidade:")); panel.add(velocidade);
        panel.add(new JLabel("Num Marchas:")); panel.add(numMarchas);
        panel.add(bagageiro);
        int result = JOptionPane.showConfirmDialog(this, panel, b == null ? "Adicionar Bicicleta" : "Editar Bicicleta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                return new Bicicleta(
                    marca.getText(),
                    Integer.parseInt(qtdRodas.getText()),
                    modelo.getText(),
                    Integer.parseInt(velocidade.getText()),
                    Integer.parseInt(numMarchas.getText()),
                    bagageiro.isSelected()
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dados inválidos.");
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BicicletaApp::new);
    }
}
