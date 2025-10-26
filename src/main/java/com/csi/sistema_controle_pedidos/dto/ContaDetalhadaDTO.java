package com.csi.sistema_controle_pedidos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ContaDetalhadaDTO(
        Long idConta,
        String contaStatus,
        BigDecimal valorTotal,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        String formaPagamento,
        MesaResumoDTO mesa,
        List<PedidoResponseDTO> pedidos
) {}
