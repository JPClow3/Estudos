package view;

import repository.AmigoRepository;
import repository.EmprestimoRepository;
import repository.ObjetoRepository;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private ObjetoRepository objetoRepository;
    private AmigoRepository amigoRepository;
    private EmprestimoRepository emprestimoRepository;

    public MainFrame(ObjetoRepository objetoRepository, AmigoRepository amigoRepository, EmprestimoRepository emprestimoRepository) {
        this.objetoRepository = objetoRepository;
        this.amigoRepository = amigoRepository;
        this.emprestimoRepository = emprestimoRepository;
        setTitle("EmprestaFacil - Controle de Empréstimos");
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        
        // Define a cor de fundo do content pane
        getContentPane().setBackground(StyleGuide.FUNDO_PRINCIPAL);

        // Set System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initUI();
    }

    private void initUI() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleGuide.COR_PRIMARIA);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("EmprestaFacil");
        lblTitle.setFont(StyleGuide.FONTE_TITULO);
        lblTitle.setForeground(StyleGuide.BRANCO);
        
        JLabel lblSubtitle = new JLabel("Controle de Empréstimos");
        lblSubtitle.setFont(StyleGuide.FONTE_TEXTO);
        lblSubtitle.setForeground(StyleGuide.BRANCO);
        
        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);
        titleBox.add(lblTitle);
        titleBox.add(lblSubtitle);
        
        headerPanel.add(titleBox, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(StyleGuide.FONTE_TEXTO);
        tabbedPane.setBackground(StyleGuide.FUNDO_PRINCIPAL);
        tabbedPane.setForeground(StyleGuide.TEXTO_PRINCIPAL);

        // Instancia os paineis
        ObjetosPanel objetosPanel = new ObjetosPanel(objetoRepository);
        AmigosPanel amigosPanel = new AmigosPanel(amigoRepository);
        EmprestimosPanel emprestimosPanel = new EmprestimosPanel(emprestimoRepository, objetoRepository, amigoRepository);

        // Adiciona as abas
        tabbedPane.addTab("Objetos", objetosPanel);
        tabbedPane.addTab("Amigos", amigosPanel);
        tabbedPane.addTab("Empréstimos", emprestimosPanel);

        // Wrapper for the tabbed pane to add margin
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(StyleGuide.FUNDO_PRINCIPAL);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentWrapper.add(tabbedPane, BorderLayout.CENTER);

        // Adiciona o painel de abas ao frame
        add(contentWrapper, BorderLayout.CENTER);

        // Atualiza os dados quando a aba é selecionada
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            if (index == 0) {
                objetosPanel.refreshData();
            } else if (index == 1) {
                amigosPanel.refreshData();
            } else if (index == 2) {
                emprestimosPanel.refreshData();
            }
        });

        // Adiciona o painel de abas ao frame
        add(tabbedPane, BorderLayout.CENTER);
    }
}
