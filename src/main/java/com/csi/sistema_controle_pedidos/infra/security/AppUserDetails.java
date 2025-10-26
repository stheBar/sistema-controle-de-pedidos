package com.csi.sistema_controle_pedidos.infra.security;

import com.csi.sistema_controle_pedidos.model.Usuario;
import com.csi.sistema_controle_pedidos.model.UsuarioTipo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails {
    private final Usuario user;

    public AppUserDetails(Usuario u) { this.user = u; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + (user.getUsuarioTipo() == null ? UsuarioTipo.GARCOM : user.getUsuarioTipo()).name();
        return List.of(new SimpleGrantedAuthority(role));
    }
    @Override public String getPassword() { return user.getSenha(); }
    @Override public String getUsername() { return user.getEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public Usuario getDomain() { return user; }
}
