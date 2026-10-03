package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "El CI es obligatorio") @Size(max = 15, message = "El CI admite maximo 15 caracteres") String ciCliente,
        @NotBlank(message = "Los nombres son obligatorios") @Size(max = 80) String nombres,
        @NotBlank(message = "Los apellidos son obligatorios") @Size(max = 80) String apellidos,
        @Size(max = 15) String telefono,
        @Email(message = "El correo no es valido") @Size(max = 100) String correo,
        String direccion) {
}
