package ui;

import service.CategoriaService;
import service.ClienteService;
import service.ProdutoService;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    public MainFrame(ClienteService clienteService, CategoriaService categoriaService, ProdutoService produtoService) {
        super("ShopControl - Cadastro Comercial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 520));

        ProdutoPanel produtoPanel = new ProdutoPanel(produtoService);
        ClientePanel clientePanel = new ClientePanel(clienteService);
        CategoriaPanel categoriaPanel = new CategoriaPanel(categoriaService, produtoPanel::refreshData);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("Clientes", clientePanel);
        tabs.addTab("Categorias", categoriaPanel);
        tabs.addTab("Produtos", produtoPanel);

        JLabel topLabel = new JLabel("ShopControl - Sistema de Cadastro Comercial", JLabel.CENTER);
        topLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        topLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel footer = new JLabel("Dica: pressione Ctrl+Shift+G para um easter egg", JLabel.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        root.add(topLabel, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("control shift G"), "gustavoEgg"
        );
        root.getActionMap().put("gustavoEgg", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        MainFrame.this,
                        "Easter egg liberado!\nProf. Gustavo, obrigado por inspirar a turma!",
                        "ShopControl",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
    }
}


