package repository;

import model.Cliente;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private static final String HEADER = "id,nome,email";
    private final Path filePath;

    public ClienteRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
    }

    public List<Cliente> listar() {
        List<String> lines = CsvUtils.readDataLines(filePath, HEADER);
        List<Cliente> clientes = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length < 3) {
                continue;
            }
            try {
                clientes.add(new Cliente(
                        Integer.parseInt(CsvUtils.normalizeFirstToken(parts[0])),
                        parts[1].trim(),
                        parts[2].trim()
                ));
            } catch (NumberFormatException ex) {
                System.err.println("Linha ignorada em Clientes.csv: " + line);
            }
        }
        return clientes;
    }

    public void salvarTodos(List<Cliente> clientes) {
        List<String> lines = new ArrayList<>();
        for (Cliente cliente : clientes) {
            lines.add(cliente.getId() + "," + cliente.getNome() + "," + cliente.getEmail());
        }
        CsvUtils.writeAll(filePath, HEADER, lines);
    }

    public int proximoId() {
        return listar().stream().mapToInt(Cliente::getId).max().orElse(0) + 1;
    }
}


