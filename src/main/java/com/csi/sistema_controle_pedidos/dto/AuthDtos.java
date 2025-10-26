package com.csi.sistema_controle_pedidos.dto;

import com.csi.sistema_controle_pedidos.model.UsuarioTipo;
import jakarta.validation.constraints.*;

public class AuthDtos {
    public static class RegisterRequest {
        @NotBlank public String nome;
        @NotBlank @Email public String email;
        @NotBlank public String senha;
        @NotNull  public UsuarioTipo usuarioTipo;
    }
    public static class LoginRequest {
        @NotBlank @Email public String email;
        @NotBlank public String senha;
    }
    public static class AuthResponse {
        public String token; public String tipo="Bearer";
        public Long idUsuario; public String nome; public String email; public String role;
        public AuthResponse(String t, Long id, String n, String e, String r){
            token=t; idUsuario=id; nome=n; email=e; role=r;
        }
    }
}
