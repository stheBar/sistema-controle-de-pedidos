package com.csi.sistema_controle_pedidos.dto;

public record ContaResumoDTO(
        Long idConta,
        String cpfTitular,
        String nomeTitular,
        MesaResumoDTO mesa
) {}
