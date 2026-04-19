package org.poo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    private static final String ARQUIVO_CAVALO1 = "cavalos_regiao1 - cavalos_regiao1.csv";
    private static final String ARQUIVO_CAVALO2 = "cavalos_regiao2 - cavalos_regiao2.csv";
    private static final String ARQUIVO_CAVALO3 = "cavalos_regiao3 - cavalos_regiao3.csv";

    public static void main(String[] args) {
        Path raizProjeto = Paths.get("").toAbsolutePath();
        CsvDataLoader loader = new CsvDataLoader();
        EstatisticasCavalosService estatisticasService = new EstatisticasCavalosService();

        try {
            List<Cavalo> cavalo1 = loader.loadCavalos(raizProjeto.resolve(ARQUIVO_CAVALO1));
            List<Cavalo> cavalo2 = loader.loadCavalos(raizProjeto.resolve(ARQUIVO_CAVALO2));
            List<Cavalo> cavalo3 = loader.loadCavalos(raizProjeto.resolve(ARQUIVO_CAVALO3));

            List<Cavalo> todosCavalos = new ArrayList<>();
            todosCavalos.addAll(cavalo1);
            todosCavalos.addAll(cavalo2);
            todosCavalos.addAll(cavalo3);

            EstatisticasCavalos estatisticas = estatisticasService.calcularEstatisticas(todosCavalos);

            System.out.println("Relatorio final de cavalos");
            System.out.println("-------------------------");
            System.out.println("Quantidade de cavalos: " + estatisticas.quantidadeCavalos());
            System.out.printf(Locale.US, "Velocidade media: %.2f km/h%n", estatisticas.velocidadeMedia());
            System.out.printf(Locale.US, "Peso medio: %.2f kg%n", estatisticas.pesoMedio());

            Cavalo maisRapido = estatisticas.cavaloMaisRapido();
            if (maisRapido != null) {
                System.out.printf(
                        Locale.US,
                        "Cavalo mais rapido: id=%d, nome=%s, velocidade=%.2f km/h, raca=%s%n",
                        maisRapido.id(),
                        maisRapido.nome(),
                        maisRapido.velocidadeMaxima(),
                        maisRapido.raca()
                );
            }

            System.out.println("Quantidade por raca:");
            estatisticas.quantidadePorRaca().forEach(
                    (raca, quantidade) -> System.out.println("- " + raca + ": " + quantidade)
            );
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivos CSV: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Erro ao processar os dados: " + e.getMessage());
        }
    }
}

