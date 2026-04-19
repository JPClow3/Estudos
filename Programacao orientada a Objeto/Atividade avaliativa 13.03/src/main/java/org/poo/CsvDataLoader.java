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
    public List<Bicicleta> loadBicicletas(Path filePath) throws IOException {
        List<List<String>> rows = readRows(filePath);
        Map<String, Integer> header = headerMap(rows.get(0));

        int idIndex = requireColumn(header, "id bicicleta", filePath);
        int modeloIndex = requireColumn(header, "modelo bicicleta", filePath);

        List<Bicicleta> bicicletas = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            int id = parseInt(row, idIndex, "id bicicleta", filePath, i + 1);
            String modelo = getValue(row, modeloIndex, "modelo bicicleta", filePath, i + 1);
            bicicletas.add(new Bicicleta(id, modelo));
        }
        return bicicletas;
    }

    public List<Cliente> loadClientes(Path filePath) throws IOException {
        List<List<String>> rows = readRows(filePath);
        Map<String, Integer> header = headerMap(rows.get(0));

        int idIndex = requireColumn(header, "id cliente", filePath);
        int nomeIndex = requireColumn(header, "nome cliente", filePath);
        int idBicicletaIndex = requireColumn(header, "id bicicleta", filePath);

        List<Cliente> clientes = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            int id = parseInt(row, idIndex, "id cliente", filePath, i + 1);
            String nome = getValue(row, nomeIndex, "nome cliente", filePath, i + 1);
            int idBicicleta = parseInt(row, idBicicletaIndex, "id bicicleta", filePath, i + 1);
            clientes.add(new Cliente(id, nome, idBicicleta));
        }
        return clientes;
    }

    public List<Venda> loadVendas(Path filePath) throws IOException {
        List<List<String>> rows = readRows(filePath);
        Map<String, Integer> header = headerMap(rows.get(0));

        int idVendaIndex = requireColumn(header, "idvenda", filePath);
        int idClienteIndex = requireColumn(header, "idcliente", filePath);
        int idBicicletaIndex = requireColumn(header, "idbicicleta", filePath);
        int valorVendaIndex = requireColumn(header, "valorvenda", filePath);

        List<Venda> vendas = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            int idVenda = parseInt(row, idVendaIndex, "idvenda", filePath, i + 1);
            int idCliente = parseInt(row, idClienteIndex, "idcliente", filePath, i + 1);
            int idBicicleta = parseInt(row, idBicicletaIndex, "idbicicleta", filePath, i + 1);
            BigDecimal valorVenda = parseBigDecimal(row, valorVendaIndex, "valorvenda", filePath, i + 1);
            vendas.add(new Venda(idVenda, idCliente, idBicicleta, valorVenda));
        }
        return vendas;
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

