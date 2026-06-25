package repository;

import model.Emprestimo;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EmprestimoRepository {
    private static final String HEADER = "id;objetoId;amigoId;dataEmprestimo;dataPrevistaDevolucao;dataDevolucao;status;observacoes";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private final Path filePath;

    public EmprestimoRepository(String fileName) {
        this.filePath = CsvUtils.resolveFile(fileName);
        CsvUtils.readRows(filePath, HEADER, 8);
    }

    public List<Emprestimo> listar() {
        List<String[]> rows = CsvUtils.readRows(filePath, HEADER, 8);
        List<Emprestimo> emprestimos = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (int index = 0; index < rows.size(); index++) {
            int row = index + 2;
            String[] parts = rows.get(index);
            try {
                int id = CsvUtils.parsePositiveInt(filePath, row, "id", parts[0]);
                if (!ids.add(id)) {
                    throw new CsvDataException(filePath, row, "id duplicado: " + id);
                }
                int objetoId = CsvUtils.parsePositiveInt(filePath, row, "objetoId", parts[1]);
                int amigoId = CsvUtils.parsePositiveInt(filePath, row, "amigoId", parts[2]);
                LocalDate dataEmprestimo = LocalDate.parse(parts[3].trim(), DATE_FORMAT);
                LocalDate dataPrevista = LocalDate.parse(parts[4].trim(), DATE_FORMAT);
                LocalDate dataDevolucao = parseNullableDate(parts[5].trim());
                String status = validateStatus(row, parts[6]);
                validateReturnDate(row, status, dataDevolucao);
                String observacoes = CsvUtils.requireSafeField(filePath, row, "observacoes", parts[7], false);
                emprestimos.add(new Emprestimo(
                        id, objetoId, amigoId, dataEmprestimo, dataPrevista, dataDevolucao, status, observacoes
                ));
            } catch (CsvDataException exception) {
                throw exception;
            } catch (RuntimeException exception) {
                throw new CsvDataException(filePath, row, "data inválida; use dd/MM/yyyy");
            }
        }
        return emprestimos;
    }

    public void salvarTodos(List<Emprestimo> emprestimos) {
        CsvUtils.writeAll(filePath, HEADER, dataLines(emprestimos));
    }

    public int proximoId() {
        return listar().stream().mapToInt(Emprestimo::getId).max().orElse(0) + 1;
    }

    public Path getFilePath() {
        return filePath;
    }

    public String fileContent(List<Emprestimo> emprestimos) {
        return CsvUtils.buildContent(HEADER, dataLines(emprestimos));
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

    private List<String> dataLines(List<Emprestimo> emprestimos) {
        List<String> lines = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (int index = 0; index < emprestimos.size(); index++) {
            int row = index + 2;
            Emprestimo emprestimo = emprestimos.get(index);
            int id = CsvUtils.parsePositiveInt(filePath, row, "id", Integer.toString(emprestimo.getId()));
            if (!ids.add(id)) {
                throw new CsvDataException(filePath, row, "id duplicado: " + id);
            }
            int objetoId = CsvUtils.parsePositiveInt(
                    filePath, row, "objetoId", Integer.toString(emprestimo.getObjetoId())
            );
            int amigoId = CsvUtils.parsePositiveInt(
                    filePath, row, "amigoId", Integer.toString(emprestimo.getAmigoId())
            );
            if (emprestimo.getDataEmprestimo() == null || emprestimo.getDataPrevistaDevolucao() == null) {
                throw new CsvDataException(filePath, row, "datas de empréstimo e prevista são obrigatórias");
            }
            String status = validateStatus(row, emprestimo.getStatus());
            validateReturnDate(row, status, emprestimo.getDataDevolucao());
            String observacoes = CsvUtils.requireSafeField(
                    filePath, row, "observacoes", emprestimo.getObservacoes(), false
            );
            lines.add(id + ";" + objetoId + ";" + amigoId + ";"
                    + formatDate(emprestimo.getDataEmprestimo()) + ";"
                    + formatDate(emprestimo.getDataPrevistaDevolucao()) + ";"
                    + formatDate(emprestimo.getDataDevolucao()) + ";"
                    + status + ";" + observacoes);
        }
        return lines;
    }

    private String validateStatus(int row, String value) {
        String status = CsvUtils.requireSafeField(filePath, row, "status", value, true);
        if (!Set.of("EMPRESTADO", "DEVOLVIDO", "ATRASADO").contains(status)) {
            throw new CsvDataException(filePath, row, "status inválido: " + status);
        }
        return status;
    }

    private void validateReturnDate(int row, String status, LocalDate returnDate) {
        if ("DEVOLVIDO".equals(status) && returnDate == null) {
            throw new CsvDataException(filePath, row, "dataDevolucao é obrigatória para status DEVOLVIDO");
        }
        if (!"DEVOLVIDO".equals(status) && returnDate != null) {
            throw new CsvDataException(
                    filePath, row, "dataDevolucao deve ficar vazia enquanto o empréstimo estiver ativo"
            );
        }
    }
}
