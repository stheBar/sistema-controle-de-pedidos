package com.csi.sistema_controle_pedidos.repository;

import com.csi.sistema_controle_pedidos.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
}
