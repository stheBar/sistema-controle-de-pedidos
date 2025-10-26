package com.csi.sistema_controle_pedidos.service;

import com.csi.sistema_controle_pedidos.model.Pedido;
import com.csi.sistema_controle_pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
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

}
