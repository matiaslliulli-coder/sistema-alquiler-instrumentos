package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import com.unifranz.sistemaalquilerinstrumentos.domain.TarifaPenalidad;
import java.math.BigDecimal;

public record TarifaPenalidadResponse(
        Integer codTarifa,
        String tipoPenalidad,
        String descripcion,
        String tipoCalculo,
        BigDecimal valor,
        boolean activa) {

    public static TarifaPenalidadResponse desde(TarifaPenalidad t) {
        return new TarifaPenalidadResponse(t.getCodTarifa(), t.getTipoPenalidad().name(), t.getDescripcion(),
                t.getTipoCalculo().name(), t.getValor(), t.isActiva());
    }
}
