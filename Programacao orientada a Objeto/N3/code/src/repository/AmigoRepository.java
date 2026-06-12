package repository;

import model.Amigo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AmigoRepository {
    private static final String HEADER = "id;nome;telefone;email";
    private final Path filePath;

    public AmigoRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
    }

    public List<Amigo> listar() {
        List<String> lines = CsvUtils.readDataLines(filePath, HEADER);
        List<Amigo> amigos = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(";", -1);
            if (parts.length < 4) {
                continue;
            }
            try {
                amigos.add(new Amigo(
                        Integer.parseInt(CsvUtils.normalizeFirstToken(parts[0])),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim()
                ));
            } catch (NumberFormatException ex) {
                System.err.println("Linha ignorada em amigos.csv: " + line);
            }
        }
        return amigos;
    }

    public void salvarTodos(List<Amigo> amigos) {
        List<String> lines = new ArrayList<>();
        for (Amigo amigo : amigos) {
            lines.add(amigo.getId() + ";"
                    + CsvUtils.cleanField(amigo.getNome()) + ";"
                    + CsvUtils.cleanField(amigo.getTelefone()) + ";"
                    + CsvUtils.cleanField(amigo.getEmail()));
        }
        CsvUtils.writeAll(filePath, HEADER, lines);
    }

    public int proximoId() {
        return listar().stream().mapToInt(Amigo::getId).max().orElse(0) + 1;
    }
}
