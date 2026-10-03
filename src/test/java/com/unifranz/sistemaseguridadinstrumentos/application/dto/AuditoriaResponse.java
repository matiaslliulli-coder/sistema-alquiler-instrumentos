package com.unifranz.sistemaseguridadinstrumentos.application.dto;

import com.unifranz.sistemaseguridadinstrumentos.domain.BitacoraAuditoria;
import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long idBitacora,
        LocalDateTime fecha,
        Integer idUsuario,
        String nombreUsuario,
        String accion,
        String entidad,
        String detalle,
        String ipOrigen) {

    public static AuditoriaResponse desde(BitacoraAuditoria b) {
        return new AuditoriaResponse(b.getIdBitacora(), b.getFecha(), b.getIdUsuario(), b.getNombreUsuario(),
                b.getAccion(), b.getEntidad(), b.getDetalle(), b.getIpOrigen());
    }
}
