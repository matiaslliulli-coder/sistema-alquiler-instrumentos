package com.unifranz.sistemainventarioinstrumentos.application.dto;

import java.math.BigDecimal;
import java.util.List;

/** Comparacion de precios de un mismo instrumento entre tiendas, ordenada de menor a mayor precio. */
public record ComparacionPreciosResponse(
        String nombreInstrumento,
        String marca,
        String modelo,
        int cantidadTiendas,
        BigDecimal precioMinimo,
        BigDecimal precioMaximo,
        BigDecimal ahorroMaximo,
        List<PrecioTiendaResponse> tiendas) {
}
