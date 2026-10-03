package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Historial de alquileres de un instrumento (del mas reciente al mas antiguo). */
public record HistorialInstrumentoResponse(
        String codInstrumento,
        String nombreInstrumento,
        String estadoActual,
        int totalAlquileres,
        List<Item> alquileres) {

    public record Item(
            Integer codAlquiler,
            String cliente,
            String ciCliente,
            LocalDate fechaAlquiler,
            LocalDate fechaDevolucionPrevista,
            LocalDate fechaDevolucionReal,
            String estadoAlquiler,
            String estadoConservacion,
            BigDecimal totalPenalidades) {
    }
}
