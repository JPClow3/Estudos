package org.poo;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstatisticasCavalosService {
    public EstatisticasCavalos calcularEstatisticas(List<Cavalo> cavalos) {
        if (cavalos == null || cavalos.isEmpty()) {
            return new EstatisticasCavalos(0, 0.0, null, 0.0, Collections.emptyMap());
        }

        int quantidade = cavalos.size();

        double velocidadeMedia = cavalos.stream()
                .mapToDouble(Cavalo::velocidadeMaxima)
                .average()
                .orElse(0.0);

        double pesoMedio = cavalos.stream()
                .mapToDouble(Cavalo::peso)
                .average()
                .orElse(0.0);

        Cavalo maisRapido = cavalos.stream()
                .max(Comparator.comparingDouble(Cavalo::velocidadeMaxima))
                .orElse(null);

        Map<String, Long> quantidadePorRaca = cavalos.stream()
                .collect(Collectors.groupingBy(Cavalo::raca, Collectors.counting()));

        Map<String, Long> quantidadePorRacaOrdenada = quantidadePorRaca.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));

        return new EstatisticasCavalos(
                quantidade,
                velocidadeMedia,
                maisRapido,
                pesoMedio,
                quantidadePorRacaOrdenada
        );
    }
}

