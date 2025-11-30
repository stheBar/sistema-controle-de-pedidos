package com.csi.sistema_controle_pedidos.dto;

import java.time.LocalDateTime;

public record PedidoDTO(
        Long idPedido,
        Long produtoId,
        Integer quantidade,
        String observacao,
        String pedidoStatus,
        LocalDateTime dataHoraPedido
) {}
