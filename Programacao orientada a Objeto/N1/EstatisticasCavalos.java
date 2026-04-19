package org.poo;

import java.util.Map;

public class EstatisticasCavalos {
    private final int quantidadeCavalos;
    private final double velocidadeMedia;
    private final Cavalo cavaloMaisRapido;
    private final double pesoMedio;
    private final Map<String, Long> quantidadePorRaca;

    public EstatisticasCavalos(
            int quantidadeCavalos,
            double velocidadeMedia,
            Cavalo cavaloMaisRapido,
            double pesoMedio,
            Map<String, Long> quantidadePorRaca
    ) {
        this.quantidadeCavalos = quantidadeCavalos;
        this.velocidadeMedia = velocidadeMedia;
        this.cavaloMaisRapido = cavaloMaisRapido;
        this.pesoMedio = pesoMedio;
        this.quantidadePorRaca = quantidadePorRaca;
    }

    public int quantidadeCavalos() {
        return quantidadeCavalos;
    }

    public double velocidadeMedia() {
        return velocidadeMedia;
    }

    public Cavalo cavaloMaisRapido() {
        return cavaloMaisRapido;
    }

    public double pesoMedio() {
        return pesoMedio;
    }

    public Map<String, Long> quantidadePorRaca() {
        return quantidadePorRaca;
    }
}

