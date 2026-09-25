package com.unifranz.sistemainventarioinstrumentos.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Convierte el enum a los textos de la base (DISPONIBLE, ALQUILADO, EN MANTENIMIENTO). */
@Converter
public class EstadoInstrumentoConverter implements AttributeConverter<EstadoInstrumento, String> {

    @Override
    public String convertToDatabaseColumn(EstadoInstrumento atributo) {
        return atributo == null ? null : atributo.getValorBd();
    }

    @Override
    public EstadoInstrumento convertToEntityAttribute(String dato) {
        return dato == null ? null : EstadoInstrumento.desdeValorBd(dato);
    }
}
