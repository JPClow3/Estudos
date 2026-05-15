import repository.CategoriaRepository;
import repository.ClienteRepository;
import repository.ProdutoRepository;
import service.CategoriaService;
import service.ClienteService;
import service.ProdutoService;
import ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path baseDir = resolveProjectBaseDir();

        ClienteRepository clienteRepository = new ClienteRepository(baseDir.resolve("Clientes.csv").toString());
        CategoriaRepository categoriaRepository = new CategoriaRepository(baseDir.resolve("Categoria.csv").toString());
        ProdutoRepository produtoRepository = new ProdutoRepository(baseDir.resolve("produtos.csv").toString());

        ClienteService clienteService = new ClienteService(clienteRepository);
        CategoriaService categoriaService = new CategoriaService(categoriaRepository, produtoRepository);
        ProdutoService produtoService = new ProdutoService(produtoRepository, categoriaRepository);

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Se nao conseguir aplicar tema nativo, segue com o padrao.
            }
            MainFrame frame = new MainFrame(clienteService, categoriaService, produtoService);
            frame.setVisible(true);
        });
    }

    private static Path resolveProjectBaseDir() {
        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        if (current.resolve("Clientes.csv").toFile().exists()) {
            return current;
        }
        Path parent = current.getParent();
        if (parent != null && parent.resolve("Clientes.csv").toFile().exists()) {
            return parent;
        }
        return current;
    }
}