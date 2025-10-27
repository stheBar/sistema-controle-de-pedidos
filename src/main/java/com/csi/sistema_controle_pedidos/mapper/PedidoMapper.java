package com.csi.sistema_controle_pedidos.mapper;

import com.csi.sistema_controle_pedidos.dto.*;
import com.csi.sistema_controle_pedidos.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class PedidoMapper {

    private PedidoMapper() {}

    public static PedidoResponseDTO toDto(Pedido p) {
        if (p == null) return null;

        return new PedidoResponseDTO(
                p.getIdPedido(),
                toUsuarioResumoDTO(p.getUsuario()),
                toContaResumoDTO(p.getConta()),
                p.getDataHoraPedido(),
                p.getPedidoStatus() != null ? p.getPedidoStatus().name() : null,
                toItemDtoList(p.getItens())
        );
    }

    public static UsuarioResumoDTO toUsuarioResumoDTO(Usuario u) {
        if (u == null) return null;
        String tipo = u.getUsuarioTipo() != null ? u.getUsuarioTipo().name() : null;
        return new UsuarioResumoDTO(
                u.getIdUsuario(),
                u.getNome(),
                u.getEmail(),
                tipo
        );
    }

    public static ContaResumoDTO toContaResumoDTO(Conta c) {
        if (c == null) return null;
        return new ContaResumoDTO(
                c.getIdConta(),
                toMesaResumoDTO(c.getMesa())
        );
    }

    public static MesaResumoDTO toMesaResumoDTO(Mesa m) {
        if (m == null) return null;
        return new MesaResumoDTO(
                m.getId(),
                m.getNumero(),
                m.getDisponivel()
        );
    }

    public static ItemPedidoDTO toItemPedidoDTO(ItemPedido i) {
        if (i == null) return null;
        return new ItemPedidoDTO(
                i.getIdItemPedido(),
                toProdutoResumoDTO(i.getProduto()),
                i.getQuantidade(),
                i.getPrecoUnitario(),
                i.getObservacao()
        );
    }

    public static List<ItemPedidoDTO> toItemDtoList(List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) return Collections.emptyList();
        return itens.stream()
                .filter(Objects::nonNull)
                .map(PedidoMapper::toItemPedidoDTO)
                .collect(Collectors.toList());
    }

    public static ProdutoResumoDTO toProdutoResumoDTO(Produto p) {
        if (p == null) return null;
        return new ProdutoResumoDTO(
                p.getId(),
                p.getNome(),
                p.getPreco()
        );
    }
}
