package com.unifranz.sistemaalquilerinstrumentos.web.controller;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.LoginRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.LoginResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.PerfilResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest solicitud, HttpServletRequest request) {
        return authService.login(solicitud, request.getRemoteAddr());
    }

    /** Perfil del usuario autenticado (requiere token). */
    @GetMapping("/me")
    public PerfilResponse perfil(Authentication autenticacion) {
        return authService.perfil(autenticacion.getName());
    }
}
