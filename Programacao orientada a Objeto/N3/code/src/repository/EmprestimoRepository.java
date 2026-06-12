package repository;

import model.Emprestimo;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoRepository {
    private static final String HEADER = "id;objetoId;amigoId;dataEmprestimo;dataPrevistaDevolucao;dataDevolucao;status;observacoes";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Path filePath;

    public EmprestimoRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
    }

    public List<Emprestimo> listar() {
        List<String> lines = CsvUtils.readDataLines(filePath, HEADER);
        List<Emprestimo> emprestimos = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(";", -1);
            if (parts.length < 8) {
                continue;
            }
            try {
                emprestimos.add(new Emprestimo(
                        Integer.parseInt(CsvUtils.normalizeFirstToken(parts[0])),
                        Integer.parseInt(parts[1].trim()),
                        Integer.parseInt(parts[2].trim()),
                        LocalDate.parse(parts[3].trim(), DATE_FORMAT),
                        LocalDate.parse(parts[4].trim(), DATE_FORMAT),
                        parseNullableDate(parts[5].trim()),
                        parts[6].trim(),
                        parts[7].trim()
                ));
            } catch (RuntimeException ex) {
                System.err.println("Linha ignorada em emprestimos.csv: " + line);
            }
        }
        return emprestimos;
    }

    public void salvarTodos(List<Emprestimo> emprestimos) {
        List<String> lines = new ArrayList<>();
        for (Emprestimo emprestimo : emprestimos) {
            lines.add(emprestimo.getId() + ";"
                    + emprestimo.getObjetoId() + ";"
                    + emprestimo.getAmigoId() + ";"
                    + formatDate(emprestimo.getDataEmprestimo()) + ";"
                    + formatDate(emprestimo.getDataPrevistaDevolucao()) + ";"
                    + formatDate(emprestimo.getDataDevolucao()) + ";"
                    + CsvUtils.cleanField(emprestimo.getStatus()) + ";"
                    + CsvUtils.cleanField(emprestimo.getObservacoes()));
        }
        CsvUtils.writeAll(filePath, HEADER, lines);
    }

    public int proximoId() {
        return listar().stream().mapToInt(Emprestimo::getId).max().orElse(0) + 1;
    }

    private static LocalDate parseNullableDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value, DATE_FORMAT);
    }

    private static String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DATE_FORMAT);
    }
}
