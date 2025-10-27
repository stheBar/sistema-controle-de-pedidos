package com.csi.sistema_controle_pedidos.dto;

import com.csi.sistema_controle_pedidos.model.ProdutoCategoria;

import java.math.BigDecimal;

public record ProdutoDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    Boolean disponivel,
    ProdutoCategoria categoria
) {}
