package com.csi.sistema_controle_pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ContaRequestDTO(
        @NotNull Long mesaId,
        @NotBlank String cpfTitular,
        @NotBlank String nomeTitular,
        String contaStatus,
        LocalDateTime dataAbertura
) {}
