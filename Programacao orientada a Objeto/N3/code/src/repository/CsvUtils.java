package repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class CsvUtils {
    private CsvUtils() {
    }

    public static Path resolveFile(String fileName) {
        return Paths.get(fileName).toAbsolutePath();
    }

    public static List<String> readDataLines(Path filePath, String header) {
        ensureFileWithHeader(filePath, header);
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<String> data = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    data.add(line);
                }
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo CSV: " + filePath, e);
        }
    }

    public static void writeAll(Path filePath, String header, List<String> dataLines) {
        List<String> output = new ArrayList<>();
        output.add(header);
        output.addAll(dataLines);
        try {
            createParentDirIfNeeded(filePath);
            Files.write(filePath, output, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo CSV: " + filePath, e);
        }
    }

    public static String normalizeFirstToken(String token) {
        return token == null ? "" : token.replace("\uFEFF", "").trim();
    }

    public static String cleanField(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(";", ",")
                .replace("\r", " ")
                .replace("\n", " ")
                .trim();
    }

    private static void ensureFileWithHeader(Path filePath, String header) {
        if (Files.exists(filePath)) {
            return;
        }
        try {
            createParentDirIfNeeded(filePath);
            Files.write(filePath, List.of(header), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar arquivo CSV: " + filePath, e);
        }
    }

    private static void createParentDirIfNeeded(Path filePath) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }
}
