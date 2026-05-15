package repository;

import model.Produto;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    private static final String HEADER = "id,nome,preco,idCategoria";
    private final Path filePath;

    public ProdutoRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
    }

    public List<Produto> listar() {
        List<String> lines = CsvUtils.readDataLines(filePath, HEADER);
        List<Produto> produtos = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length < 4) {
                continue;
            }
            try {
                produtos.add(new Produto(
                        Integer.parseInt(CsvUtils.normalizeFirstToken(parts[0])),
                        parts[1].trim(),
                        Double.parseDouble(parts[2].trim()),
                        Integer.parseInt(parts[3].trim())
                ));
            } catch (NumberFormatException ex) {
                System.err.println("Linha ignorada em produtos.csv: " + line);
            }
        }
        return produtos;
    }

    public void salvarTodos(List<Produto> produtos) {
        List<String> lines = new ArrayList<>();
        for (Produto produto : produtos) {
            lines.add(produto.getId() + "," + produto.getNome() + "," + produto.getPreco() + "," + produto.getIdCategoria());
        }
        CsvUtils.writeAll(filePath, HEADER, lines);
    }

    public int proximoId() {
        return listar().stream().mapToInt(Produto::getId).max().orElse(0) + 1;
    }
}


