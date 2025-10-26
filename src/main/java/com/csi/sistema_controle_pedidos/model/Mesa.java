package com.csi.sistema_controle_pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mesa")
@Schema(description = "Entidade que representa uma mesa física no estabelecimento")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    @Schema(description = "ID único da mesa", example = "1")
    private Long id;

    @Column(name = "numero", nullable = false, unique = true)
    @Schema(description = "Número de identificação da mesa", example = "15")
    private Integer numero;

    @Column(name = "disponivel", nullable = false)
    @Schema(description = "Booleano que indica se a mesa está disponível ou ocupada", example = "true")
    private Boolean disponivel;

    @OneToMany(mappedBy = "mesa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore

    private List<Conta> contas = new ArrayList<>();

}