package org.poo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvDataLoaderAndJoinServiceTest {

    public static void main(String[] args) throws IOException {
        deveMontarVendaDetalhadaComReferenciasEncontradasENaoEncontradas();
        deveLancarExcecaoQuandoValorNumericoInvalido();
        System.out.println("Auto-teste concluido com sucesso.");
    }

    static void deveMontarVendaDetalhadaComReferenciasEncontradasENaoEncontradas() throws IOException {
        Path tempDir = Files.createTempDirectory("csv-test-");
        Path bicicletas = tempDir.resolve("bicicletas.csv");
        Path clientes = tempDir.resolve("clientes.csv");
        Path vendas = tempDir.resolve("vendas.csv");

        Files.writeString(
                bicicletas,
                "Id bicicleta,modelo bicicleta\n1,Urban Ride\n2,Master Pedal\n",
                StandardCharsets.UTF_8
        );

        Files.writeString(
                clientes,
                "ID cliente,nome cliente,ID bicicleta\n1,Ana,1\n",
                StandardCharsets.UTF_8
        );

        Files.writeString(
                vendas,
                "IDVenda,IDCliente,IDBicicleta,ValorVenda\n10,1,2,1500.50\n11,99,77,300.00\n",
                StandardCharsets.UTF_8
        );

        CsvDataLoader loader = new CsvDataLoader();
        VendaJoinService joinService = new VendaJoinService();

        List<VendaDetalhada> resultado = joinService.montarVendasDetalhadas(
                loader.loadVendas(vendas),
                loader.loadClientes(clientes),
                loader.loadBicicletas(bicicletas)
        );

        assertEquals(2, resultado.size(), "Quantidade de vendas detalhadas");

        assertEquals(10, resultado.get(0).idVenda(), "ID da venda 1");
        assertEquals("Ana", resultado.get(0).nomeCliente(), "Cliente encontrado");
        assertEquals("Master Pedal", resultado.get(0).modeloBicicleta(), "Bicicleta encontrada");
        assertEquals(new BigDecimal("1500.50"), resultado.get(0).valorVenda(), "Valor da venda 1");

        assertEquals(11, resultado.get(1).idVenda(), "ID da venda 2");
        assertEquals("CLIENTE NAO ENCONTRADO", resultado.get(1).nomeCliente(), "Cliente inexistente");
        assertEquals("BICICLETA NAO ENCONTRADA", resultado.get(1).modeloBicicleta(), "Bicicleta inexistente");
        assertEquals(new BigDecimal("300.00"), resultado.get(1).valorVenda(), "Valor da venda 2");
    }

    static void deveLancarExcecaoQuandoValorNumericoInvalido() throws IOException {
        Path tempDir = Files.createTempDirectory("csv-test-invalid-");
        Path vendas = tempDir.resolve("vendas.csv");

        Files.writeString(
                vendas,
                "IDVenda,IDCliente,IDBicicleta,ValorVenda\n1,2,3,abc\n",
                StandardCharsets.UTF_8
        );

        CsvDataLoader loader = new CsvDataLoader();

        boolean exceptionThrown = false;
        try {
            loader.loadVendas(vendas);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            throw new AssertionError("Era esperada IllegalArgumentException para valor numerico invalido.");
        }
    }

    private static void assertEquals(Object expected, Object actual, String context) {
        if ((expected == null && actual != null) || (expected != null && !expected.equals(actual))) {
            throw new AssertionError(context + " - esperado: " + expected + ", atual: " + actual);
        }
    }
}
