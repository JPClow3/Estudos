package repository;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.Objeto;

public class ObjetoRepository {
    private static final String HEADER = "id;nome;categoria;descricao;disponivel;valor";
    private final Path filePath;

    public ObjetoRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
        CsvUtils.readRows(filePath, HEADER, 6);
    }

    public List<Objeto> listar() {
        List<String[]> rows = CsvUtils.readRows(filePath, HEADER, 6);
        List<Objeto> objetos = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (int index = 0; index < rows.size(); index++) {
            int row = index + 2;
            String[] parts = rows.get(index);
            int id = CsvUtils.parsePositiveInt(filePath, row, "id", parts[0]);
            if (!ids.add(id)) {
                throw new CsvDataException(filePath, row, "id duplicado: " + id);
            }
            String nome = CsvUtils.requireSafeField(filePath, row, "nome", parts[1], true);
            String categoria = CsvUtils.requireSafeField(filePath, row, "categoria", parts[2], true);
            String descricao = CsvUtils.requireSafeField(filePath, row, "descricao", parts[3], false);
            String booleanValue = parts[4].trim();
            if (!"true".equals(booleanValue) && !"false".equals(booleanValue)) {
                throw new CsvDataException(filePath, row, "disponivel deve ser true ou false");
            }
            double valor = parseValue(row, parts[5]);
            objetos.add(new Objeto(id, nome, categoria, descricao, Boolean.parseBoolean(booleanValue), valor));
        }
        return objetos;
    }

    public void salvarTodos(List<Objeto> objetos) {
        CsvUtils.writeAll(filePath, HEADER, dataLines(objetos));
    }

    public int proximoId() {
        return listar().stream().mapToInt(Objeto::getId).max().orElse(0) + 1;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String fileContent(List<Objeto> objetos) {
        return CsvUtils.buildContent(HEADER, dataLines(objetos));
    }

    private List<String> dataLines(List<Objeto> objetos) {
        List<String> lines = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (int index = 0; index < objetos.size(); index++) {
            int row = index + 2;
            Objeto objeto = objetos.get(index);
            int id = CsvUtils.parsePositiveInt(filePath, row, "id", Integer.toString(objeto.getId()));
            if (!ids.add(id)) {
                throw new CsvDataException(filePath, row, "id duplicado: " + id);
            }
            String nome = CsvUtils.requireSafeField(filePath, row, "nome", objeto.getNome(), true);
            String categoria = CsvUtils.requireSafeField(filePath, row, "categoria", objeto.getCategoria(), true);
            String descricao = CsvUtils.requireSafeField(filePath, row, "descricao", objeto.getDescricao(), false);
            double valor = objeto.getValor() == null ? Double.NaN : objeto.getValor();
            if (!Double.isFinite(valor) || valor < 0) {
                throw new CsvDataException(filePath, row, "valor deve ser finito e não negativo");
            }
            lines.add(id + ";" + nome + ";" + categoria + ";" + descricao + ";"
                    + objeto.isDisponivel() + ";" + valor);
        }
        return lines;
    }

    private double parseValue(int row, String value) {
        try {
            double parsed = Double.parseDouble(value.trim());
            if (!Double.isFinite(parsed) || parsed < 0) {
                throw new NumberFormatException("non-finite or negative");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new CsvDataException(filePath, row, "valor deve ser finito e não negativo");
        }
    }
}
