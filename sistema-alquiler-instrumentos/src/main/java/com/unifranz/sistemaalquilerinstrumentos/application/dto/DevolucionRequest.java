package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoConservacion;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/** Datos para registrar la devolucion. fechaDevolucion es opcional (por defecto, hoy). */
public record DevolucionRequest(
        LocalDate fechaDevolucion,
        @NotNull(message = "El estado de conservacion es obligatorio (EXCELENTE, BUENO, REGULAR o DANADO)")
        EstadoConservacion estadoConservacion,
        String observaciones) {
}
