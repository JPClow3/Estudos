package src;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cadastro de Clientes");
            TelaLegal2 tela = new TelaLegal2();
            frame.setContentPane(tela.getPainel1());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}