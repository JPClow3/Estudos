package org.poo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CsvDataLoader {
    public List<Cavalo> loadCavalos(Path filePath) throws IOException {
        List<List<String>> rows = readRows(filePath);
        Map<String, Integer> header = headerMap(rows.get(0));

        int idIndex = requireColumn(header, "id", filePath);
        int nomeIndex = requireColumn(header, "nome", filePath);
        int idadeIndex = requireColumn(header, "idade", filePath);
        int pesoIndex = requireColumn(header, "peso", filePath);
        int velocidadeIndex = requireColumn(header, "velocidademaxima", filePath);
        int racaIndex = requireColumn(header, "raca", filePath);

        List<Cavalo> cavalos = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            int id = parseInt(row, idIndex, "id", filePath, i + 1);
            String nome = getValue(row, nomeIndex, "nome", filePath, i + 1);
            int idade = parseInt(row, idadeIndex, "idade", filePath, i + 1);
            double peso = parseBigDecimal(row, pesoIndex, "peso", filePath, i + 1).doubleValue();
            double velocidadeMaxima = parseBigDecimal(row, velocidadeIndex, "velocidadeMaxima", filePath, i + 1).doubleValue();
            String raca = getValue(row, racaIndex, "raca", filePath, i + 1);
            cavalos.add(new Cavalo(id, nome, idade, peso, velocidadeMaxima, raca));
        }
        return cavalos;
    }

    private List<List<String>> readRows(Path filePath) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            rows.add(splitCsvLine(line));
        }

        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio: " + filePath);
        }
        return rows;
    }

    private Map<String, Integer> headerMap(List<String> headerRow) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headerRow.size(); i++) {
            map.put(normalize(headerRow.get(i)), i);
        }
        return map;
    }

    private int requireColumn(Map<String, Integer> header, String columnName, Path filePath) {
        Integer index = header.get(normalize(columnName));
        if (index == null) {
            throw new IllegalArgumentException("Coluna obrigatoria ausente ('" + columnName + "') em " + filePath);
        }
        return index;
    }

    private String getValue(List<String> row, int index, String columnName, Path filePath, int lineNumber) {
        if (index >= row.size()) {
            throw new IllegalArgumentException(
                    "Linha " + lineNumber + " sem coluna '" + columnName + "' em " + filePath
            );
        }
        return row.get(index).trim();
    }

    private int parseInt(List<String> row, int index, String columnName, Path filePath, int lineNumber) {
        String value = getValue(row, index, columnName, filePath, lineNumber);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Valor invalido para '" + columnName + "' na linha " + lineNumber + " de " + filePath + ": " + value,
                    e
            );
        }
    }

    private BigDecimal parseBigDecimal(List<String> row, int index, String columnName, Path filePath, int lineNumber) {
        String value = getValue(row, index, columnName, filePath, lineNumber);
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Valor invalido para '" + columnName + "' na linha " + lineNumber + " de " + filePath + ": " + value,
                    e
            );
        }
    }

    private List<String> splitCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                insideQuotes = !insideQuotes;
                continue;
            }
            if (ch == ',' && !insideQuotes) {
                columns.add(current.toString().trim());
                current.setLength(0);
                continue;
            }
            current.append(ch);
        }

        columns.add(current.toString().trim());
        return columns;
    }

    private String normalize(String text) {
        return text.trim().toLowerCase(Locale.ROOT);
    }
}

