package com.csi.sistema_controle_pedidos.repository;

import com.csi.sistema_controle_pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByContaMesaId(Long idMesa);
}
