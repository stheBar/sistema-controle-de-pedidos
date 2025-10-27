package com.csi.sistema_controle_pedidos.mapper;

import com.csi.sistema_controle_pedidos.dto.*;
import com.csi.sistema_controle_pedidos.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public final class ContaMapper {

    private ContaMapper() {}

    public static ContaResponseDTO toDto(Conta c) {
        if (c == null) return null;
        return new ContaResponseDTO(
                c.getIdConta(),
                c.getContaStatus() != null ? c.getContaStatus().name() : null,
                c.getValorTotal() != null ? c.getValorTotal() : BigDecimal.ZERO,
                c.getDataAbertura(),
                c.getDataFechamento(),
                c.getFormaPagamento() != null ? c.getFormaPagamento().name() : null,
                PedidoMapper.toMesaResumoDTO(c.getMesa())
        );
    }

    public static ContaDetalhadaDTO toDetalhadaDto(Conta conta) {
        return new ContaDetalhadaDTO(
                conta.getIdConta(),
                conta.getContaStatus() != null ? conta.getContaStatus().name() : null,
                conta.getValorTotal(),
                conta.getDataAbertura(),
                conta.getDataFechamento(),
                conta.getFormaPagamento() != null ? conta.getFormaPagamento().name() : null,
                PedidoMapper.toMesaResumoDTO(conta.getMesa()),
                conta.getPedidos() != null
                        ? conta.getPedidos().stream()
                        .map(PedidoMapper::toDto)
                        .collect(Collectors.toList())
                        : null
        );
    }


    public static Conta fromDto(ContaRequestDTO dto) {
        if (dto == null) return null;
        Conta c = new Conta();

        Mesa mesa = new Mesa();
        mesa.setId(dto.mesaId());
        c.setMesa(mesa);

        c.setCpf_titular(dto.cpfTitular());
        c.setNome_titular(dto.nomeTitular());
        c.setContaStatus(dto.contaStatus() != null ? ContaStatus.valueOf(dto.contaStatus()) : ContaStatus.ABERTA);
        c.setDataAbertura(dto.dataAbertura() != null ? dto.dataAbertura() : LocalDateTime.now());
        c.setValorTotal(BigDecimal.ZERO);
        c.setFormaPagamento(null);
        return c;
    }

}
