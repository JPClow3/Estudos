package org.poo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VendaJoinService {
    private static final String CLIENTE_NAO_ENCONTRADO = "CLIENTE NAO ENCONTRADO";
    private static final String BICICLETA_NAO_ENCONTRADA = "BICICLETA NAO ENCONTRADA";

    public List<VendaDetalhada> montarVendasDetalhadas(
            List<Venda> vendas,
            List<Cliente> clientes,
            List<Bicicleta> bicicletas
    ) {
        Map<Integer, String> nomeClientePorId = new HashMap<>();
        for (Cliente cliente : clientes) {
            nomeClientePorId.put(cliente.id(), cliente.nome());
        }

        Map<Integer, String> modeloBicicletaPorId = new HashMap<>();
        for (Bicicleta bicicleta : bicicletas) {
            modeloBicicletaPorId.put(bicicleta.id(), bicicleta.modelo());
        }

        return vendas.stream()
                .map(venda -> new VendaDetalhada(
                        venda.idVenda(),
                        nomeClientePorId.getOrDefault(venda.idCliente(), CLIENTE_NAO_ENCONTRADO),
                        modeloBicicletaPorId.getOrDefault(venda.idBicicleta(), BICICLETA_NAO_ENCONTRADA),
                        venda.valorVenda()
                ))
                .collect(Collectors.toList());
    }
}

