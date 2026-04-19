package org.poo;

import java.math.BigDecimal;

public record Venda(int idVenda, int idCliente, int idBicicleta, BigDecimal valorVenda) {
}

