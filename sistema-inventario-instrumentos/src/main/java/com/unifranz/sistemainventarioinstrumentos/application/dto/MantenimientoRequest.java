package com.unifranz.sistemainventarioinstrumentos.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Datos para registrar el ingreso de un instrumento a mantenimiento (por ejemplo, devuelto con danos). */
public record MantenimientoRequest(
        @NotBlank(message = "Debe indicar el codigo del instrumento")
        @Size(max = 10, message = "El codigo del instrumento no puede superar 10 caracteres")
        String codInstrumento,

        @NotBlank(message = "La descripcion del dano es obligatoria")
        String descripcionDano,

        Integer codDevolucion,

        Integer idUsuario) {
}
