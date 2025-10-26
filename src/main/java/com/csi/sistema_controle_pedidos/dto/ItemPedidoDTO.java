package com.csi.sistema_controle_pedidos.dto;

import java.math.BigDecimal;

public record ItemPedidoDTO(
        Long idItem,
        ProdutoResumoDTO produto,
        Integer quantidade,
        BigDecimal precoUnitario,
        String observacao
) {}
