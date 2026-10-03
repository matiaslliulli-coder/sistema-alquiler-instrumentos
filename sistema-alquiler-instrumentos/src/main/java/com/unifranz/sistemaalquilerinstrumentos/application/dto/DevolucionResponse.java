package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DevolucionResponse(
        Integer codDevolucion,
        Integer codAlquiler,
        String codInstrumento,
        LocalDate fechaDevolucionPrevista,
        LocalDate fechaDevolucionReal,
        String estadoConservacion,
        int diasAtraso,
        List<PenalidadItem> penalidades,
        BigDecimal totalPenalidades,
        String estadoInstrumento,
        Integer codMantenimiento) {

    public record PenalidadItem(String tipo, String concepto, BigDecimal monto) {
    }
}
