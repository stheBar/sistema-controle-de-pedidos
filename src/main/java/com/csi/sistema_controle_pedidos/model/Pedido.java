package com.csi.sistema_controle_pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedido")
@Schema(description = "Entidade que representa um pedido feito por um usuário e associado a uma conta")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    @Schema(description = "ID único do pedido", example = "1")
    private Long idPedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @Schema(description = "Usuário que realizou o pedido")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_conta", nullable = false)
    @Schema(description = "Conta à qual o pedido está vinculado")
    private Conta conta;

    @Column(name = "data_hora_pedido", nullable = false)
    @Schema(description = "Data e hora exata em que o pedido foi realizado", example = "2025-10-23T14:35:10")
    private LocalDateTime dataHoraPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "pedido_status", nullable = false)
    @Schema(description = "Status atual do pedido", example = "EM_PREPARACAO")
    private PedidoStatus pedidoStatus;


    @OneToMany(mappedBy = "pedido")
    @Schema(description = "Lista de itens que compõem o pedido")
    private List<ItemPedido> itens;

}