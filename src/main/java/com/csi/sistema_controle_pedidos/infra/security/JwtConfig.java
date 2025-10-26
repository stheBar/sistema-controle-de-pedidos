package com.csi.sistema_controle_pedidos.infra.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String getSecret() {
        return secret;
    }

    public Long getExpiration() {
        return expiration;
    }
}
