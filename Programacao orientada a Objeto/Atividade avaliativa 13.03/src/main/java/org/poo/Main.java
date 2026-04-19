package org.poo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static final String ARQUIVO_BICICLETAS = "bicicletas_ficticias - bicicletas_ficticias.csv";
    private static final String ARQUIVO_CLIENTES = "clientes_ficticios - clientes_ficticios.csv";
    private static final String ARQUIVO_VENDAS = "vendas_ficticias - vendas_ficticias.csv";

    public static void main(String[] args) {
        Path raizProjeto = Paths.get("").toAbsolutePath();
        CsvDataLoader loader = new CsvDataLoader();
        VendaJoinService joinService = new VendaJoinService();

        try {
            List<Bicicleta> bicicletas = loader.loadBicicletas(raizProjeto.resolve(ARQUIVO_BICICLETAS));
            List<Cliente> clientes = loader.loadClientes(raizProjeto.resolve(ARQUIVO_CLIENTES));
            List<Venda> vendas = loader.loadVendas(raizProjeto.resolve(ARQUIVO_VENDAS));

            List<VendaDetalhada> vendasDetalhadas = joinService.montarVendasDetalhadas(vendas, clientes, bicicletas);
            imprimirVendas(vendasDetalhadas);
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivos CSV: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Erro ao processar os dados: " + e.getMessage());
        }
    }

    private static void imprimirVendas(List<VendaDetalhada> vendasDetalhadas) {
        System.out.printf("%-8s | %-22s | %-20s | %s%n", "IDVenda", "Cliente", "Modelo Bicicleta", "ValorVenda");
        System.out.println("--------------------------------------------------------------------------------");

        for (VendaDetalhada venda : vendasDetalhadas) {
            System.out.printf(
                    "%-8d | %-22s | %-20s | R$ %s%n",
                    venda.idVenda(),
                    venda.nomeCliente(),
                    venda.modeloBicicleta(),
                    venda.valorVenda().toPlainString()
            );
        }
    }
}