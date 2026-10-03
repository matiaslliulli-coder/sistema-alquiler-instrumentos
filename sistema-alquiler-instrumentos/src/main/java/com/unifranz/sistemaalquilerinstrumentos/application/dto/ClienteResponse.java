package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import com.unifranz.sistemaalquilerinstrumentos.domain.Cliente;
import java.time.LocalDate;

public record ClienteResponse(
        Integer codCliente,
        String ciCliente,
        String nombres,
        String apellidos,
        String telefono,
        String correo,
        String direccion,
        boolean identidadVerificada,
        LocalDate fechaRegistro) {

    public static ClienteResponse desde(Cliente c) {
        return new ClienteResponse(c.getCodCliente(), c.getCiCliente(), c.getNombres(), c.getApellidos(),
                c.getTelefono(), c.getCorreo(), c.getDireccion(), c.isIdentidadVerificada(), c.getFechaRegistro());
    }
}
