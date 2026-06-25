import java.nio.file.Path;
import java.nio.file.Paths;
import repository.AmigoRepository;
import repository.EmprestimoRepository;
import repository.ObjetoRepository;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        view.MainFrame.installLookAndFeel();
        Path baseDir = Paths.get("semana3", "dados").toAbsolutePath();
        SwingUtilities.invokeLater(() -> {
            try {
                ObjetoRepository objetoRepository = new ObjetoRepository(
                        baseDir.resolve("objetos.csv").toString()
                );
                AmigoRepository amigoRepository = new AmigoRepository(
                        baseDir.resolve("amigos.csv").toString()
                );
                EmprestimoRepository emprestimoRepository = new EmprestimoRepository(
                        baseDir.resolve("emprestimos.csv").toString()
                );
                view.MainFrame mainFrame = new view.MainFrame(
                        objetoRepository, amigoRepository, emprestimoRepository
                );
                mainFrame.setVisible(true);
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(
                        null,
                        exception.getMessage(),
                        "Erro ao abrir os dados",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
