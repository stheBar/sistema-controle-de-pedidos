package com.csi.sistema_controle_pedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
@Schema(description = "Entidade que representa um usuário do sistema (ex: atendente, gerente)")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @Schema(description = "ID único do usuário", example = "1")
    private Long idUsuario;

    @Column(name = "nome", nullable = false, length = 100)
    @Schema(description = "Nome completo do usuário", example = "Ana Pereira")
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Schema(description = "Email de login do usuário", example = "ana.pereira@exemplo.com")
    private String email;

    @Column(name = "senha", nullable = false, length = 72)
    @Schema(description = "Senha criptografada do usuário")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "usuario_tipo", nullable = false)
    @Schema(description = "Tipo/perfil de permissão do usuário", example = "ADMIN")
    private UsuarioTipo usuarioTipo;

}