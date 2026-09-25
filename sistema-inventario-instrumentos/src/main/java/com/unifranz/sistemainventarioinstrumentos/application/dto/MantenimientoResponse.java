package com.unifranz.sistemainventarioinstrumentos.application.dto;

import com.unifranz.sistemainventarioinstrumentos.domain.Mantenimiento;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MantenimientoResponse(
        Integer codMantenimiento,
        String codInstrumento,
        String nombreInstrumento,
        String estadoInstrumento,
        LocalDate fechaIngreso,
        LocalDate fechaSalida,
        String descripcionDano,
        String trabajoRealizado,
        BigDecimal costoReparacion,
        String estadoMantenimiento,
        Integer codDevolucion,
        Integer idUsuario) {

    public static MantenimientoResponse desde(Mantenimiento m) {
        return new MantenimientoResponse(
                m.getCodMantenimiento(),
                m.getInstrumento().getCodInstrumento(),
                m.getInstrumento().getNombreInstrumento(),
                m.getInstrumento().getEstadoInstrumento().name(),
                m.getFechaIngreso(),
                m.getFechaSalida(),
                m.getDescripcionDano(),
                m.getTrabajoRealizado(),
                m.getCostoReparacion(),
                m.getEstadoMantenimiento(),
                m.getCodDevolucion(),
                m.getIdUsuario());
    }
}
