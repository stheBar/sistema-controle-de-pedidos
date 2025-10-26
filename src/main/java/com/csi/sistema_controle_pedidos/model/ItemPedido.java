package com.csi.sistema_controle_pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Table(name = "item_pedido")
@Data
@Schema(description = "Entidade que representa um item específico dentro de um pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_pedido")
    @Schema(description = "ID único do item do pedido", example = "1")
    private Long idItemPedido;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @Schema(description = "Pedido ao qual este item pertence")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    @Schema(description = "Produto associado a este item")
    private Produto produto;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Preço do produto no momento da inclusão no pedido", example = "25.50")
    private BigDecimal precoUnitario;

    @Column(name = "quantidade", nullable = false)
    @Schema(description = "Quantidade deste produto no pedido", example = "2")
    private int quantidade;

    @Column(name = "observacao")
    @Schema(description = "Observações adicionais para o item", example = "Sem cebola")
    private String observacao;

}