package com.unifranz.sistemainventarioinstrumentos.application.dto;

import com.unifranz.sistemainventarioinstrumentos.domain.Instrumento;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InstrumentoResponse(
        String codInstrumento,
        String nombreInstrumento,
        String categoria,
        String tipo,
        String marca,
        String modeloInstrumento,
        Integer codTienda,
        String tienda,
        String ciudad,
        BigDecimal precioAlquiler,
        String estadoInstrumento,
        LocalDate fechaRegistro) {

    public static InstrumentoResponse desde(Instrumento i) {
        return new InstrumentoResponse(
                i.getCodInstrumento(),
                i.getNombreInstrumento(),
                i.getSubcategoria().getCategoria().getNombreCategoria(),
                i.getSubcategoria().getNombreSubcategoria(),
                i.getMarca().getNombreMarca(),
                i.getModeloInstrumento(),
                i.getTienda().getCodTienda(),
                i.getTienda().getNombreTienda(),
                i.getTienda().getCiudad().getNombreCiudad(),
                i.getPrecioAlquiler(),
                i.getEstadoInstrumento().name(),
                i.getFechaRegistro());
    }
}
