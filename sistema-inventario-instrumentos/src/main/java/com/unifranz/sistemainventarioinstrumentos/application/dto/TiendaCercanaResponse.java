package com.unifranz.sistemainventarioinstrumentos.application.dto;

import com.unifranz.sistemainventarioinstrumentos.domain.Instrumento;
import com.unifranz.sistemainventarioinstrumentos.domain.Tienda;
import java.math.BigDecimal;
import java.util.List;

/** Sucursal ordenada por cercania al cliente, con los instrumentos disponibles que coinciden con la busqueda. */
public record TiendaCercanaResponse(
        Integer codTienda,
        String nombreTienda,
        String ciudad,
        String direccionTienda,
        String telefonoTienda,
        BigDecimal latitudTienda,
        BigDecimal longitudTienda,
        double distanciaKm,
        int cantidadInstrumentosDisponibles,
        List<InstrumentoResumen> instrumentos) {

    public record InstrumentoResumen(
            String codInstrumento,
            String nombreInstrumento,
            String marca,
            String modeloInstrumento,
            BigDecimal precioAlquiler) {

        public static InstrumentoResumen desde(Instrumento i) {
            return new InstrumentoResumen(
                    i.getCodInstrumento(),
                    i.getNombreInstrumento(),
                    i.getMarca().getNombreMarca(),
                    i.getModeloInstrumento(),
                    i.getPrecioAlquiler());
        }
    }

    public static TiendaCercanaResponse desde(Tienda t, double distanciaKm, List<Instrumento> instrumentos) {
        return new TiendaCercanaResponse(
                t.getCodTienda(),
                t.getNombreTienda(),
                t.getCiudad().getNombreCiudad(),
                t.getDireccionTienda(),
                t.getTelefonoTienda(),
                t.getLatitudTienda(),
                t.getLongitudTienda(),
                distanciaKm,
                instrumentos.size(),
                instrumentos.stream().map(InstrumentoResumen::desde).toList());
    }
}
