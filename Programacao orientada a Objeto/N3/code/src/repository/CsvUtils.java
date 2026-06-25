package repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CsvUtils {
    private CsvUtils() {
    }

    public static Path resolveFile(String fileName) {
        return Paths.get(fileName).toAbsolutePath();
    }

    public static List<String[]> readRows(Path filePath, String header, int expectedColumns) {
        ensureFileWithHeader(filePath, header);
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                throw new CsvDataException(filePath, "arquivo vazio; cabeçalho esperado: " + header);
            }

            String actualHeader = normalizeFirstToken(lines.get(0));
            if (!header.equals(actualHeader)) {
                throw new CsvDataException(filePath, 1, "cabeçalho inválido; esperado: " + header);
            }

            List<String[]> rows = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                int rowNumber = i + 1;
                if (line.isBlank()) {
                    throw new CsvDataException(filePath, rowNumber, "linha vazia não é permitida");
                }
                if (line.indexOf('"') >= 0) {
                    throw new CsvDataException(filePath, rowNumber, "aspas e campos multilinha não são suportados");
                }
                String[] parts = line.split(";", -1);
                if (parts.length != expectedColumns) {
                    throw new CsvDataException(
                            filePath,
                            rowNumber,
                            "quantidade de colunas inválida; esperado " + expectedColumns + ", encontrado " + parts.length
                    );
                }
                rows.add(parts);
            }
            return rows;
        } catch (IOException e) {
            throw new CsvDataException(filePath, "não foi possível ler como UTF-8", e);
        }
    }

    public static void writeAll(Path filePath, String header, List<String> dataLines) {
        writeTransaction(Map.of(filePath, buildContent(header, dataLines)));
    }

    public static void writeTransaction(Map<Path, String> contents) {
        Map<Path, byte[]> originals = new LinkedHashMap<>();
        Map<Path, Path> temporaryFiles = new LinkedHashMap<>();
        try {
            for (Map.Entry<Path, String> entry : contents.entrySet()) {
                Path target = entry.getKey();
                createParentDirIfNeeded(target);
                originals.put(target, Files.exists(target) ? Files.readAllBytes(target) : null);
                Path temporary = Files.createTempFile(target.getParent(), target.getFileName().toString(), ".tmp");
                Files.writeString(temporary, entry.getValue(), StandardCharsets.UTF_8);
                temporaryFiles.put(target, temporary);
            }

            for (Map.Entry<Path, Path> entry : temporaryFiles.entrySet()) {
                replace(entry.getValue(), entry.getKey());
            }
        } catch (IOException e) {
            restoreOriginals(originals);
            Path first = contents.keySet().iterator().next();
            throw new CsvDataException(first, "erro ao salvar os arquivos CSV", e);
        } finally {
            for (Path temporary : temporaryFiles.values()) {
                try {
                    Files.deleteIfExists(temporary);
                } catch (IOException ignored) {
                    // Best-effort cleanup after a completed or rolled-back transaction.
                }
            }
        }
    }

    public static String normalizeFirstToken(String token) {
        if (token == null) {
            return "";
        }
        return token.startsWith("\uFEFF") ? token.substring(1) : token;
    }

    public static String requireSafeField(Path filePath, int row, String fieldName, String value, boolean required) {
        String normalized = value == null ? "" : value.trim();
        if (required && normalized.isEmpty()) {
            throw new CsvDataException(filePath, row, fieldName + " é obrigatório");
        }
        if (normalized.indexOf(';') >= 0 || normalized.indexOf('\r') >= 0 || normalized.indexOf('\n') >= 0) {
            throw new CsvDataException(filePath, row, fieldName + " contém caractere proibido");
        }
        return normalized;
    }

    public static int parsePositiveInt(Path filePath, int row, String fieldName, String value) {
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                throw new NumberFormatException("non-positive");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new CsvDataException(filePath, row, fieldName + " deve ser um inteiro positivo");
        }
    }

    public static String buildContent(String header, List<String> dataLines) {
        StringBuilder content = new StringBuilder(header).append('\n');
        for (String line : dataLines) {
            if (line.indexOf('\r') >= 0 || line.indexOf('\n') >= 0) {
                throw new IllegalArgumentException("Uma linha CSV não pode conter quebra de linha.");
            }
            content.append(line).append('\n');
        }
        return content.toString();
    }

    private static void ensureFileWithHeader(Path filePath, String header) {
        if (Files.exists(filePath)) {
            return;
        }
        try {
            createParentDirIfNeeded(filePath);
            Files.writeString(filePath, header + '\n', StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CsvDataException(filePath, "erro ao criar arquivo CSV", e);
        }
    }

    private static void createParentDirIfNeeded(Path filePath) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    private static void replace(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void restoreOriginals(Map<Path, byte[]> originals) {
        for (Map.Entry<Path, byte[]> entry : originals.entrySet()) {
            try {
                if (entry.getValue() == null) {
                    Files.deleteIfExists(entry.getKey());
                } else {
                    Files.write(entry.getKey(), entry.getValue());
                }
            } catch (IOException ignored) {
                // The original write error remains the primary failure.
            }
        }
    }
}
