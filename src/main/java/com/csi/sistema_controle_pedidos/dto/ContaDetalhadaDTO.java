package com.csi.sistema_controle_pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ContaDetalhadaDTO(
        Long idConta,
        String contaStatus,
        BigDecimal valorTotal,

        @NotNull
        LocalDateTime dataAbertura,

        LocalDateTime dataFechamento,
        String formaPagamento,

        @NotBlank
        @Size(min = 14, max = 14, message = "CPF deve ter 14 caracteres (formato xxx.xxx.xxx-xx)")
        String cpfTitular,

        @NotBlank
        String nomeTitular,

        MesaResumoDTO mesa,
        List<PedidoResponseDTO> pedidos
) {}
