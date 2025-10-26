package com.csi.sistema_controle_pedidos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaResponseDTO(
        Long idConta,
        String contaStatus,
        BigDecimal valorTotal,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        String formaPagamento,
        MesaResumoDTO mesa
) {}
