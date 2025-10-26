package com.csi.sistema_controle_pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "conta")
@Schema(description = "Entidade que representa uma conta de cliente em uma mesa")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    @Schema(description = "ID único da conta", example = "1")
    private Long idConta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mesa")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"contas"})
    @Schema(description = "Mesa associada a esta conta")
    private Mesa mesa;


    @NotBlank
    @Column(name = "cpf_titular", length = 14)
    @Size(min = 14, max = 14, message = "CPF deve ter 14 caracteres (formato xxx.xxx.xxx-xx)")
    @Schema(description = "CPF do titular da conta", example = "123.456.789-00")
    private String cpf_titular;

    @NotBlank
    @Column(name = "nome_titular", length = 100)
    @Schema(description = "Nome do titular da conta", example = "Fulano De Tal")
    private String nome_titular;

    @Enumerated(EnumType.STRING)
    @Column(name = "conta_status", nullable = false)
    @Schema(description = "Status atual da conta", example = "ABERTA")
    private ContaStatus contaStatus;

    @NotNull
    @Column(name = "data_abertura", nullable = false)
    @Schema(description = "Data e hora em que a conta foi aberta", example = "2025-10-23T14:30:00")
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    @Schema(description = "Data e hora em que a conta foi fechada (se aplicável)", example = "2025-10-23T16:45:00")
    private LocalDateTime dataFechamento;

    @Column(name = "valor_total", precision = 14, scale = 2)
    @Schema(description = "Valor total acumulado dos pedidos na conta", example = "120.50")
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento")
    @Schema(description = "Forma de pagamento escolhida ao fechar a conta", example = "PIX")
    private FormaPagamento formaPagamento;

    @OneToMany(mappedBy = "conta")
    @Schema(description = "Lista de pedidos associados a esta conta")
    private List<Pedido> pedidos;
}
