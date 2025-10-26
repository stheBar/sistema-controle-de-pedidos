package com.csi.sistema_controle_pedidos.service;

import com.csi.sistema_controle_pedidos.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemPedidoService {

    private ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }


}
