package repository;

import model.Amigo;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AmigoRepository {
    private static final String HEADER = "id;nome;telefone;email";
    private final Path filePath;

    public AmigoRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
        CsvUtils.readRows(filePath, HEADER, 4);
    }

    public List<Amigo> listar() {
        List<String[]> rows = CsvUtils.readRows(filePath, HEADER, 4);
        List<Amigo> amigos = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (int index = 0; index < rows.size(); index++) {
            int row = index + 2;
            String[] parts = rows.get(index);
            int id = CsvUtils.parsePositiveInt(filePath, row, "id", parts[0]);
            if (!ids.add(id)) {
                throw new CsvDataException(filePath, row, "id duplicado: " + id);
            }
            String nome = CsvUtils.requireSafeField(filePath, row, "nome", parts[1], true);
            String telefone = CsvUtils.requireSafeField(filePath, row, "telefone", parts[2], false);
            String email = CsvUtils.requireSafeField(filePath, row, "email", parts[3], false);
            if (telefone.isEmpty() && email.isEmpty()) {
                throw new CsvDataException(filePath, row, "telefone ou e-mail deve ser informado");
            }
            amigos.add(new Amigo(id, nome, telefone, email));
        }
        return amigos;
    }

    public void salvarTodos(List<Amigo> amigos) {
        CsvUtils.writeAll(filePath, HEADER, dataLines(amigos));
    }

    public int proximoId() {
        return listar().stream().mapToInt(Amigo::getId).max().orElse(0) + 1;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String fileContent(List<Amigo> amigos) {
        return CsvUtils.buildContent(HEADER, dataLines(amigos));
    }

    private List<String> dataLines(List<Amigo> amigos) {
        List<String> lines = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (int index = 0; index < amigos.size(); index++) {
            int row = index + 2;
            Amigo amigo = amigos.get(index);
            int id = CsvUtils.parsePositiveInt(filePath, row, "id", Integer.toString(amigo.getId()));
            if (!ids.add(id)) {
                throw new CsvDataException(filePath, row, "id duplicado: " + id);
            }
            String nome = CsvUtils.requireSafeField(filePath, row, "nome", amigo.getNome(), true);
            String telefone = CsvUtils.requireSafeField(filePath, row, "telefone", amigo.getTelefone(), false);
            String email = CsvUtils.requireSafeField(filePath, row, "email", amigo.getEmail(), false);
            if (telefone.isEmpty() && email.isEmpty()) {
                throw new CsvDataException(filePath, row, "telefone ou e-mail deve ser informado");
            }
            lines.add(id + ";" + nome + ";" + telefone + ";" + email);
        }
        return lines;
    }
}
