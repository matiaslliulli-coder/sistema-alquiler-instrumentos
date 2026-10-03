package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import com.unifranz.sistemaalquilerinstrumentos.domain.Alquiler;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AlquilerResponse(
        Integer codAlquiler,
        Integer codCliente,
        String cliente,
        String ciCliente,
        String codInstrumento,
        String instrumento,
        LocalDate fechaAlquiler,
        LocalDate fechaDevolucionPrevista,
        int diasAlquiler,
        BigDecimal precioDia,
        BigDecimal montoTotal,
        String estadoAlquiler) {

    public static AlquilerResponse desde(Alquiler a) {
        return new AlquilerResponse(
                a.getCodAlquiler(),
                a.getCliente().getCodCliente(),
                a.getCliente().getNombreCompleto(),
                a.getCliente().getCiCliente(),
                a.getInstrumento().getCodInstrumento(),
                a.getInstrumento().getNombreInstrumento(),
                a.getFechaAlquiler(),
                a.getFechaDevolucionPrevista(),
                a.getDiasAlquiler(),
                a.getPrecioDia(),
                a.getMontoTotal(),
                a.getEstadoAlquiler().name());
    }
}
