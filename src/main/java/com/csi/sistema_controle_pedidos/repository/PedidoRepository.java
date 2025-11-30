package com.csi.sistema_controle_pedidos.repository;

import com.csi.sistema_controle_pedidos.model.Pedido;
import com.csi.sistema_controle_pedidos.model.PedidoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByContaMesaId(Long idMesa);
    long countByContaIdContaAndPedidoStatusIn(Long contaId, List<PedidoStatus> statuses);

    List<Pedido> findByContaIdConta(Long idConta);
}
