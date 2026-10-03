package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/** Datos para registrar un alquiler. fechaAlquiler es opcional (por defecto, hoy). */
public record AlquilerRequest(
        @NotNull(message = "El cliente es obligatorio") Integer codCliente,
        @NotBlank(message = "El instrumento es obligatorio") String codInstrumento,
        @NotNull(message = "El plazo en dias es obligatorio")
        @Min(value = 1, message = "El plazo minimo es 1 dia")
        @Max(value = 90, message = "El plazo maximo es 90 dias") Integer diasAlquiler,
        LocalDate fechaAlquiler) {
}
