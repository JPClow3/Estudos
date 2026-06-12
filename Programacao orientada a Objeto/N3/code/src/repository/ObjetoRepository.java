package repository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import model.Objeto;

public class ObjetoRepository {
    private static final String HEADER = "id;nome;categoria;descricao;disponivel;valor";
    private final Path filePath;

    public ObjetoRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
    }

    public List<Objeto> listar() {
        List<String> lines = CsvUtils.readDataLines(filePath, HEADER);
        List<Objeto> objetos = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(";", -1);
            if (parts.length < 6) {
                continue;
            }
            try {
                objetos.add(new Objeto(
                        Integer.parseInt(CsvUtils.normalizeFirstToken(parts[0])),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        Boolean.parseBoolean(parts[4].trim()),
                        parts[5].trim().isEmpty() ? 0.0 : Double.parseDouble(parts[5].trim())
                ));
            } catch (NumberFormatException ex) {
                System.err.println("Linha ignorada em objetos.csv: " + line);
            }
        }
        return objetos;
    }

    public void salvarTodos(List<Objeto> objetos) {
        List<String> lines = new ArrayList<>();
        for (Objeto objeto : objetos) {
            lines.add(objeto.getId() + ";"
                    + CsvUtils.cleanField(objeto.getNome()) + ";"
                    + CsvUtils.cleanField(objeto.getCategoria()) + ";"
                    + CsvUtils.cleanField(objeto.getDescricao()) + ";"
                    + objeto.isDisponivel() + ";"
                    + objeto.getValor());
        }
        CsvUtils.writeAll(filePath, HEADER, lines);
    }

    public int proximoId() {
        return listar().stream().mapToInt(Objeto::getId).max().orElse(0) + 1;
    }
}
