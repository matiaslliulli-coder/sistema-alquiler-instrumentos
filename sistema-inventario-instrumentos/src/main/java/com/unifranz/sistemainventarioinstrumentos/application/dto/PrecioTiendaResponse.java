package com.unifranz.sistemainventarioinstrumentos.application.dto;

import com.unifranz.sistemainventarioinstrumentos.domain.Instrumento;
import java.math.BigDecimal;

/** Una fila de la tabla comparativa de precios. */
public record PrecioTiendaResponse(
        String codInstrumento,
        Integer codTienda,
        String tienda,
        String ciudad,
        String direccion,
        BigDecimal precioAlquiler) {

    public static PrecioTiendaResponse desde(Instrumento i) {
        return new PrecioTiendaResponse(
                i.getCodInstrumento(),
                i.getTienda().getCodTienda(),
                i.getTienda().getNombreTienda(),
                i.getTienda().getCiudad().getNombreCiudad(),
                i.getTienda().getDireccionTienda(),
                i.getPrecioAlquiler());
    }
}
