package com.csi.sistema_controle_pedidos.dto;

import java.math.BigDecimal;

public record ProdutoDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Boolean disponivel,
    String categoria
) {}
