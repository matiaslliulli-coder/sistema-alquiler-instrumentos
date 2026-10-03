package com.unifranz.sistemaseguridadinstrumentos.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El usuario es obligatorio") String nombreUsuario,
        @NotBlank(message = "La contrasena es obligatoria") String password) {
}
