package com.unifranz.sistemaseguridadinstrumentos.application.dto;

import java.util.List;

public record LoginResponse(
        String token,
        String tipoToken,
        long expiraEnSegundos,
        String nombreUsuario,
        String nombreCompleto,
        String rol,
        List<String> permisos) {
}
