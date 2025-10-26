package com.csi.sistema_controle_pedidos.service;

import com.csi.sistema_controle_pedidos.model.Usuario;
import com.csi.sistema_controle_pedidos.repository.UsuarioRepository;
import com.csi.sistema_controle_pedidos.infra.security.JwtService;
import com.csi.sistema_controle_pedidos.dto.AuthDtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service @RequiredArgsConstructor
public class AuthService {
  private final UsuarioRepository repo;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authManager;
  private final JwtService jwt;

  public AuthResponse register(RegisterRequest req){
    if (repo.existsByEmail(req.email)) throw new IllegalArgumentException("E-mail já cadastrado");
    var u = new Usuario();
    u.setNome(req.nome); u.setEmail(req.email);
    u.setSenha(encoder.encode(req.senha)); u.setUsuarioTipo(req.usuarioTipo);
    u = repo.save(u);
    var token = jwt.generate(u.getEmail(), Map.of("uid",u.getIdUsuario(),"role","ROLE_"+u.getUsuarioTipo(),"nome",u.getNome()));
    return new AuthResponse(token,u.getIdUsuario(),u.getNome(),u.getEmail(),u.getUsuarioTipo().name());
  }

  public AuthResponse login(LoginRequest req){
    Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email, req.senha));
    var u = repo.findByEmail(req.email).orElseThrow();
    var token = jwt.generate(u.getEmail(), Map.of("uid",u.getIdUsuario(),"role","ROLE_"+u.getUsuarioTipo(),"nome",u.getNome()));
    return new AuthResponse(token,u.getIdUsuario(),u.getNome(),u.getEmail(),u.getUsuarioTipo().name());
  }
}
