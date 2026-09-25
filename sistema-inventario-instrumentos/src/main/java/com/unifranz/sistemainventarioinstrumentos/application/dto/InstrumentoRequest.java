package com.unifranz.sistemainventarioinstrumentos.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** Datos para registrar un instrumento. El codigo, el estado DISPONIBLE y la fecha los genera el sistema. */
public record InstrumentoRequest(
        @NotBlank(message = "El nombre del instrumento es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombreInstrumento,

        @NotNull(message = "Debe indicar la subcategoria (tipo de instrumento)")
        Integer codSubcategoria,

        @NotNull(message = "Debe indicar la marca")
        Integer codMarca,

        @NotBlank(message = "El modelo es obligatorio")
        @Size(max = 50, message = "El modelo no puede superar 50 caracteres")
        String modeloInstrumento,

        @NotNull(message = "Debe indicar la tienda")
        Integer codTienda,

        @NotNull(message = "El precio de alquiler es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio de alquiler debe ser mayor a 0")
        @Digits(integer = 5, fraction = 2, message = "El precio admite hasta 5 enteros y 2 decimales")
        BigDecimal precioAlquiler) {
}
