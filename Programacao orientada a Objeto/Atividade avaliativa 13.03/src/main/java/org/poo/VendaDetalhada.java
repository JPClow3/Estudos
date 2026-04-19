package org.poo;

import java.math.BigDecimal;

public record VendaDetalhada(int idVenda, String nomeCliente, String modeloBicicleta, BigDecimal valorVenda) {
}

