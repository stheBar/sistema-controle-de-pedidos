
package com.csi.sistema_controle_pedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long idPedido,
        UsuarioResumoDTO usuario,
        ContaResumoDTO conta,
        LocalDateTime dataHoraPedido,
        String pedidoStatus,
        List<ItemPedidoDTO> itens
) {}
