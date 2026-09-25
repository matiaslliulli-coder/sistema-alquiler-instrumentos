package com.unifranz.sistemainventarioinstrumentos.application.dto;

import com.unifranz.sistemainventarioinstrumentos.domain.Tienda;
import java.math.BigDecimal;

public record TiendaResponse(
        Integer codTienda,
        String nombreTienda,
        Integer codCiudad,
        String ciudad,
        String direccionTienda,
        String telefonoTienda,
        BigDecimal latitudTienda,
        BigDecimal longitudTienda,
        String responsableTienda,
        String estado) {

    public static TiendaResponse desde(Tienda t) {
        return new TiendaResponse(
                t.getCodTienda(),
                t.getNombreTienda(),
                t.getCiudad().getCodCiudad(),
                t.getCiudad().getNombreCiudad(),
                t.getDireccionTienda(),
                t.getTelefonoTienda(),
                t.getLatitudTienda(),
                t.getLongitudTienda(),
                t.getResponsableTienda(),
                t.getEstado());
    }
}
