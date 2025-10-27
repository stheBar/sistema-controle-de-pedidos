package com.csi.sistema_controle_pedidos.repository;

import com.csi.sistema_controle_pedidos.model.Conta;
import com.csi.sistema_controle_pedidos.model.ContaStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    @Query("select count(c) > 0 from Conta c where c.mesa.id = :mesaId and c.contaStatus = :status")
    boolean existsByMesaAndStatus(@Param("mesaId") Long mesaId, @Param("status") ContaStatus status);

    @Query("select coalesce(sum(i.precoUnitario * i.quantidade), 0) " +
            "from Pedido p join p.itens i where p.conta.idConta = :contaId")
    BigDecimal totalByContaId(@Param("contaId") Long contaId);
}
