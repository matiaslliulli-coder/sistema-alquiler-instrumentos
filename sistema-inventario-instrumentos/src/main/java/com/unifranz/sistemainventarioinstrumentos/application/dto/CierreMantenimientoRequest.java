package com.unifranz.sistemainventarioinstrumentos.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

/** Datos para cerrar un registro de mantenimiento; el instrumento vuelve a estar DISPONIBLE. */
public record CierreMantenimientoRequest(
        @NotBlank(message = "Debe describir el trabajo realizado")
        String trabajoRealizado,

        @DecimalMin(value = "0.00", message = "El costo no puede ser negativo")
        @Digits(integer = 5, fraction = 2, message = "El costo admite hasta 5 enteros y 2 decimales")
        BigDecimal costoReparacion) {
}
