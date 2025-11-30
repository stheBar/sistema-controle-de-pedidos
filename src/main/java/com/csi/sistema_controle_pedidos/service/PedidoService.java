package com.csi.sistema_controle_pedidos.service;

import com.csi.sistema_controle_pedidos.model.Pedido;
import com.csi.sistema_controle_pedidos.model.PedidoStatus;
import com.csi.sistema_controle_pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }
    public List<Pedido> listarPorConta(Long idConta) {
        return pedidoRepository.findByContaIdConta(idConta);
    }


    public void criarPedido(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public void deletarPedido(Pedido pedido){
        pedidoRepository.delete(pedido);
    }

    public List<Pedido> listarPedidosDeUmaMesa(long idMesa) {
        return pedidoRepository.findByContaMesaId(idMesa);
    }

    @Transactional
    public Pedido atualizarStatus(Long idPedido, PedidoStatus novoStatus) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + idPedido));

        pedido.setPedidoStatus(novoStatus);

        if (pedido.getConta() != null && pedido.getConta().getMesa() != null) {
            pedido.getConta().getMesa().getNumero();
        }

        return pedido;
    }

}
