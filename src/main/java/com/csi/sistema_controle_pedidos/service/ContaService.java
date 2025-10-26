// src/main/java/com/csi/sistema_controle_pedidos/service/ContaService.java
package com.csi.sistema_controle_pedidos.service;

import com.csi.sistema_controle_pedidos.dto.ContaDetalhadaDTO;
import com.csi.sistema_controle_pedidos.dto.CriarPedidoRequest;
import com.csi.sistema_controle_pedidos.dto.PedidoResponseDTO;
import com.csi.sistema_controle_pedidos.mapper.ContaMapper;
import com.csi.sistema_controle_pedidos.mapper.PedidoMapper;
import com.csi.sistema_controle_pedidos.model.*;
import com.csi.sistema_controle_pedidos.repository.*;
// REMOVIDA A IMPORTAÇÃO: com.csi.sistema_controle_pedidos.security.PegaDadosJWT;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRepository;
    // REMOVIDO: private final UsuarioRepository usuarioRepository;
    // REMOVIDO: private final PegaDadosJWT pegaDadosJWT;

    // CONSTRUTOR ATUALIZADO
    public ContaService(ContaRepository contaRepository,
                        PedidoRepository pedidoRepository,
                        ItemPedidoRepository itemPedidoRepository,
                        ProdutoRepository produtoRepository) {
        this.contaRepository = contaRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    public Conta criarConta(Conta conta) {
        if (conta.getMesa() == null || conta.getMesa().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mesa é obrigatória");
        }
        if (contaRepository.existsByMesaAndStatus(conta.getMesa().getId(), ContaStatus.ABERTA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe conta ABERTA para esta mesa");
        }
        if (conta.getDataAbertura() == null) {
            conta.setDataAbertura(LocalDateTime.now());
        }
        if (conta.getContaStatus() == null) {
            conta.setContaStatus(ContaStatus.ABERTA);
        }
        conta.setValorTotal(BigDecimal.ZERO);
        conta.setFormaPagamento(null);
        conta.setContaStatus(ContaStatus.ABERTA);
        try {
            return contaRepository.save(conta);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Dados inválidos para criar conta", e);
        }
    }

    public void deletarConta(long id) {
        Conta c = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        contaRepository.delete(c);
    }

    public Conta mostrarConta(long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    public ContaDetalhadaDTO pedirConta(long id) {
        Conta conta = mostrarConta(id);

        // recalcula o total sempre
        BigDecimal total = safeTotal(id);
        conta.setValorTotal(total);

        // se for a primeira vez que está pedindo (ABERTA → PENDENTE)
        if (conta.getContaStatus() == ContaStatus.ABERTA) {
            conta.setContaStatus(ContaStatus.PENDENTE);
        }

        // não muda o status se já estiver PENDENTE ou FECHADA
        Conta atualizada = contaRepository.save(conta);

        // retorna conta detalhada com pedidos, itens e produtos
        return ContaMapper.toDetalhadaDto(atualizada);
    }

    public Conta pagarConta(long id, FormaPagamento formaPagamento) {
        if (formaPagamento == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forma de pagamento é obrigatória");
        }
        Conta c = mostrarConta(id);
        if (c.getContaStatus() == ContaStatus.FECHADA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conta já está FECHADA");
        }
        BigDecimal total = safeTotal(id);
        c.setValorTotal(total);
        c.setFormaPagamento(formaPagamento);
        c.setContaStatus(ContaStatus.FECHADA);
        c.setDataFechamento(LocalDateTime.now());
        return contaRepository.save(c);
    }

    @Transactional
    // MÉTODO ATUALIZADO
    public PedidoResponseDTO criarPedidoNaConta(long contaId, CriarPedidoRequest body, Usuario usuarioLogado) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
        if (conta.getContaStatus() != ContaStatus.ABERTA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível incluir pedido em conta não ABERTA");
        }

        // REMOVIDO: O bloco que usava pegaDadosJWT.getUsuarioId() e usuarioRepository.findById()
        // Agora, 'usuarioLogado' já é o objeto 'Usuario' completo

        Pedido pedido = new Pedido();
        pedido.setConta(conta);
        pedido.setUsuario(usuarioLogado); // <-- MUDANÇA AQUI: Usa o parâmetro
        pedido.setDataHoraPedido(LocalDateTime.now());
        pedido.setPedidoStatus(PedidoStatus.PENDENTE);
        pedido = pedidoRepository.save(pedido);

        for (CriarPedidoRequest.ItemRequest it : body.itens()) {
            Produto produto = produtoRepository.findById(it.produtoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Produto inválido"));
            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setPrecoUnitario(produto.getPreco()); // fonte da verdade
            item.setQuantidade(it.quantidade());
            item.setObservacao(it.observacao());
            itemPedidoRepository.save(item);
        }

        conta.setValorTotal(safeTotal(contaId));
        contaRepository.save(conta);

        Pedido pedidoFinal = pedidoRepository.findById(pedido.getIdPedido())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao recuperar pedido"));

        return PedidoMapper.toDto(pedidoFinal);
    }

    private BigDecimal safeTotal(long contaId) {
        BigDecimal total = contaRepository.totalByContaId(contaId);
        return total != null ? total : BigDecimal.ZERO;
    }
}