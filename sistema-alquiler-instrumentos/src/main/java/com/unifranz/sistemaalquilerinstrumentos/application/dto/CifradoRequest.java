package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CifradoRequest(@NotBlank(message = "El texto es obligatorio") String texto) {
}
