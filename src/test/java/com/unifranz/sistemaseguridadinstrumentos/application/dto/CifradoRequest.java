package com.unifranz.sistemaseguridadinstrumentos.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CifradoRequest(@NotBlank(message = "El texto es obligatorio") String texto) {
}
