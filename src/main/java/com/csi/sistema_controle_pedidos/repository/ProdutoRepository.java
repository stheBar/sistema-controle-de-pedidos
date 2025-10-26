package com.csi.sistema_controle_pedidos.repository;

import com.csi.sistema_controle_pedidos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    public Produto findProdutoById(Long id);
}
