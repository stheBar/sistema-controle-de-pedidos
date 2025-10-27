package com.csi.sistema_controle_pedidos.mapper;

import com.csi.sistema_controle_pedidos.dto.ProdutoDTO;
import com.csi.sistema_controle_pedidos.model.Produto;

public class ProdutoMapper {

    public static ProdutoDTO toDto(Produto p) {
        if (p == null) return null;
        return new ProdutoDTO(
            p.getId(),
            p.getNome(),
            p.getDescricao(),
            p.getPreco(),
            p.getDisponivel(),
            p.getProdutoCategoria() != null ? p.getProdutoCategoria() : null
        );
    }

    public static Produto fromDto(ProdutoDTO dto) {
        if (dto == null) return null;
        Produto p = new Produto();
        p.setId(dto.id());
        p.setNome(dto.nome());
        p.setDescricao(dto.descricao());
        p.setPreco(dto.preco());
        p.setDisponivel(dto.disponivel());
        p.setProdutoCategoria(dto.categoria());
        return p;
    }
}
