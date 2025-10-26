package com.csi.sistema_controle_pedidos.controller;

import com.csi.sistema_controle_pedidos.service.AuthService;
import com.csi.sistema_controle_pedidos.dto.AuthDtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService service;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req){
    return ResponseEntity.status(HttpStatus.CREATED).body(service.register(req));
  }
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req){
    return ResponseEntity.ok(service.login(req));
  }
}
