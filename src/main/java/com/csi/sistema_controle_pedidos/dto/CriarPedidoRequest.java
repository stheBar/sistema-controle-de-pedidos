package com.csi.sistema_controle_pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CriarPedidoRequest(
        @NotEmpty(message = "lista de itens não pode ser vazia")
        List<@Valid ItemRequest> itens
) {
    public static record ItemRequest(
            @NotNull(message = "produtoId é obrigatório")
            Long produtoId,

            @Positive(message = "quantidade deve ser > 0")
            int quantidade,

            String observacao
    ) {}
}
