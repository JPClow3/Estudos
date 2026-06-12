import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Amigo;
import model.Emprestimo;
import model.Objeto;
import repository.AmigoRepository;
import repository.EmprestimoRepository;
import repository.ObjetoRepository;

public class Main {
    public static void main(String[] args) {
        Path baseDir = Paths.get("semana3", "dados").toAbsolutePath();
        limparDadosDeExemplo(baseDir);

        ObjetoRepository objetoRepository = new ObjetoRepository(baseDir.resolve("objetos.csv").toString());
        AmigoRepository amigoRepository = new AmigoRepository(baseDir.resolve("amigos.csv").toString());
        EmprestimoRepository emprestimoRepository = new EmprestimoRepository(baseDir.resolve("emprestimos.csv").toString());

        List<Objeto> objetos = new ArrayList<>();
        objetos.add(new Objeto(objetoRepository.proximoId(), "Furadeira Bosch", "Ferramenta", "Furadeira de impacto 500W", false, 250.50));
        objetos.add(new Objeto(objetoRepository.proximoId() + 1, "Livro Java", "Livro", "Livro de Programacao Orientada a Objeto", true, 89.90));
        objetoRepository.salvarTodos(objetos);

        List<Amigo> amigos = new ArrayList<>();
        amigos.add(new Amigo(amigoRepository.proximoId(), "Gabriel Moreira", "(64) 99999-0001", "gabriel@email.com"));
        amigos.add(new Amigo(amigoRepository.proximoId() + 1, "Rubens Cosmo", "(64) 99999-0002", "rubens@email.com"));
        amigoRepository.salvarTodos(amigos);

        List<Emprestimo> emprestimos = new ArrayList<>();
        emprestimos.add(new Emprestimo(
                emprestimoRepository.proximoId(),
                objetos.get(0).getId(),
                amigos.get(0).getId(),
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                null,
                "EMPRESTADO",
                "Devolver com a maleta"
        ));
        emprestimoRepository.salvarTodos(emprestimos);

        System.out.println("Objetos cadastrados: " + objetoRepository.listar().size());
        System.out.println("Amigos cadastrados: " + amigoRepository.listar().size());
        System.out.println("Emprestimos cadastrados: " + emprestimoRepository.listar().size());
        System.out.println("Arquivos CSV gerados em: " + baseDir);

        javax.swing.SwingUtilities.invokeLater(() -> {
            view.MainFrame mainFrame = new view.MainFrame(objetoRepository, amigoRepository, emprestimoRepository);
            mainFrame.setVisible(true);
        });
    }

    private static void limparDadosDeExemplo(Path baseDir) {
        try {
            Files.deleteIfExists(baseDir.resolve("objetos.csv"));
            Files.deleteIfExists(baseDir.resolve("amigos.csv"));
            Files.deleteIfExists(baseDir.resolve("emprestimos.csv"));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao limpar arquivos de exemplo.", e);
        }
    }
}
