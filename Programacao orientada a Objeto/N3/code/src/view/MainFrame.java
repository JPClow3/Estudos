package view;

import repository.AmigoRepository;
import repository.EmprestimoRepository;
import repository.ObjetoRepository;
import service.LoanService;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("this-escape")
public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public MainFrame(ObjetoRepository objetoRepository, AmigoRepository amigoRepository, EmprestimoRepository emprestimoRepository) {
        installLookAndFeel();
        setTitle("EmprestaFacil - Controle de Empréstimos");
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        
        // Define a cor de fundo do content pane
        getContentPane().setBackground(StyleGuide.FUNDO_PRINCIPAL);

        initUI(objetoRepository, amigoRepository, emprestimoRepository);
    }

    public static void installLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException exception) {
            System.err.println("Não foi possível aplicar o tema do sistema: " + exception.getMessage());
        }
    }

    private void initUI(
            ObjetoRepository objetoRepository,
            AmigoRepository amigoRepository,
            EmprestimoRepository emprestimoRepository
    ) {
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

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(StyleGuide.FONTE_TEXTO);
        tabbedPane.setBackground(StyleGuide.FUNDO_PRINCIPAL);
        tabbedPane.setForeground(StyleGuide.TEXTO_PRINCIPAL);

        // Instancia os paineis
        LoanService loanService = new LoanService(emprestimoRepository, objetoRepository, amigoRepository);
        ObjetosPanel objetosPanel = new ObjetosPanel(objetoRepository, loanService);
        AmigosPanel amigosPanel = new AmigosPanel(amigoRepository, loanService);
        EmprestimosPanel emprestimosPanel = new EmprestimosPanel(
                loanService, objetoRepository, amigoRepository
        );

        // Adiciona as abas
        tabbedPane.addTab("Objetos", objetosPanel);
        tabbedPane.addTab("Amigos", amigosPanel);
        tabbedPane.addTab("Empréstimos", emprestimosPanel);

        // Wrapper for the tabbed pane to add margin
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(StyleGuide.FUNDO_PRINCIPAL);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentWrapper.add(tabbedPane, BorderLayout.CENTER);

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

        add(contentWrapper, BorderLayout.CENTER);
    }
}
