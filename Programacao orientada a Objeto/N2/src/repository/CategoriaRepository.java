package repository;

import model.Categoria;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {
    private static final String HEADER = "id,nome";
    private final Path filePath;

    public CategoriaRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
    }

    public List<Categoria> listar() {
        List<String> lines = CsvUtils.readDataLines(filePath, HEADER);
        List<Categoria> categorias = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length < 2) {
                continue;
            }
            try {
                categorias.add(new Categoria(
                        Integer.parseInt(CsvUtils.normalizeFirstToken(parts[0])),
                        parts[1].trim()
                ));
            } catch (NumberFormatException ex) {
                System.err.println("Linha ignorada em Categoria.csv: " + line);
            }
        }
        return categorias;
    }

    public void salvarTodos(List<Categoria> categorias) {
        List<String> lines = new ArrayList<>();
        for (Categoria categoria : categorias) {
            lines.add(categoria.getId() + "," + categoria.getNome());
        }
        CsvUtils.writeAll(filePath, HEADER, lines);
    }

    public int proximoId() {
        return listar().stream().mapToInt(Categoria::getId).max().orElse(0) + 1;
    }
}


