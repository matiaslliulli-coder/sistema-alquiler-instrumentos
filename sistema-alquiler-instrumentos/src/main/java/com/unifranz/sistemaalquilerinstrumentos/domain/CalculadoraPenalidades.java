package com.unifranz.sistemaalquilerinstrumentos.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Regla de negocio de las penalidades. No depende de Spring ni de la base de datos.
 *  - Atraso: valor x precio diario x dias de atraso.
 *  - Estado REGULAR: tarifa DANO_LEVE.   Estado DANADO: tarifa DANO_GRAVE.
 */
public final class CalculadoraPenalidades {

    /** Una penalidad calculada, aun sin guardar. */
    public record LineaPenalidad(TarifaPenalidad tarifa, String concepto, BigDecimal monto) {
    }

    private CalculadoraPenalidades() {
    }

    public static int diasAtraso(LocalDate fechaPrevista, LocalDate fechaReal) {
        long dias = ChronoUnit.DAYS.between(fechaPrevista, fechaReal);
        return (int) Math.max(0, dias);
    }

    public static List<LineaPenalidad> calcular(BigDecimal precioDia, int diasAtraso,
                                                EstadoConservacion estado, List<TarifaPenalidad> tarifas) {
        List<LineaPenalidad> lineas = new ArrayList<>();

        if (diasAtraso > 0) {
            TarifaPenalidad tarifa = buscar(tarifas, TipoPenalidad.ATRASO);
            BigDecimal monto = tarifa.getTipoCalculo() == TipoCalculo.POR_DIA_ATRASO
                    ? precioDia.multiply(tarifa.getValor()).multiply(BigDecimal.valueOf(diasAtraso))
                    : tarifa.getValor();
            lineas.add(new LineaPenalidad(tarifa, "Atraso de " + diasAtraso + " dia(s)", redondear(monto)));
        }

        if (estado == EstadoConservacion.REGULAR) {
            TarifaPenalidad tarifa = buscar(tarifas, TipoPenalidad.DANO_LEVE);
            lineas.add(new LineaPenalidad(tarifa, "Danos leves o desgaste (estado REGULAR)", redondear(tarifa.getValor())));
        } else if (estado == EstadoConservacion.DANADO) {
            TarifaPenalidad tarifa = buscar(tarifas, TipoPenalidad.DANO_GRAVE);
            lineas.add(new LineaPenalidad(tarifa, "Danos graves (estado DANADO)", redondear(tarifa.getValor())));
        }
        return lineas;
    }

    public static BigDecimal total(List<LineaPenalidad> lineas) {
        return lineas.stream().map(LineaPenalidad::monto).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static TarifaPenalidad buscar(List<TarifaPenalidad> tarifas, TipoPenalidad tipo) {
        return tarifas.stream()
                .filter(t -> t.getTipoPenalidad() == tipo && t.isActiva())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay una tarifa de penalidad activa para " + tipo));
    }

    private static BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
