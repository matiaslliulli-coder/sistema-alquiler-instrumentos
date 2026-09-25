package com.unifranz.sistemainventarioinstrumentos.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record TiendaRequest(
        @NotBlank(message = "El nombre de la tienda es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombreTienda,

        @NotNull(message = "Debe indicar la ciudad")
        Integer codCiudad,

        String direccionTienda,

        @Pattern(regexp = "\\d{8}", message = "El telefono debe tener 8 digitos")
        String telefonoTienda,

        @NotNull(message = "La latitud es obligatoria")
        @DecimalMin(value = "-90.0", message = "La latitud debe estar entre -90 y 90")
        @DecimalMax(value = "90.0", message = "La latitud debe estar entre -90 y 90")
        BigDecimal latitudTienda,

        @NotNull(message = "La longitud es obligatoria")
        @DecimalMin(value = "-180.0", message = "La longitud debe estar entre -180 y 180")
        @DecimalMax(value = "180.0", message = "La longitud debe estar entre -180 y 180")
        BigDecimal longitudTienda,

        @Size(max = 100, message = "El responsable no puede superar 100 caracteres")
        String responsableTienda) {
}
