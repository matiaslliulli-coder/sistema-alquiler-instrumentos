package com.unifranz.sistemaalquilerinstrumentos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.unifranz.sistemaalquilerinstrumentos.domain.CalculadoraPenalidades.LineaPenalidad;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class CalculadoraPenalidadesTest {

    private final List<TarifaPenalidad> tarifas = List.of(
            new TarifaPenalidad(TipoPenalidad.ATRASO, "Atraso", TipoCalculo.POR_DIA_ATRASO, new BigDecimal("0.50")),
            new TarifaPenalidad(TipoPenalidad.DANO_LEVE, "Dano leve", TipoCalculo.MONTO_FIJO, new BigDecimal("50.00")),
            new TarifaPenalidad(TipoPenalidad.DANO_GRAVE, "Dano grave", TipoCalculo.MONTO_FIJO, new BigDecimal("200.00")));

    private final BigDecimal precioDia = new BigDecimal("25.00");

    @Test
    void diasAtraso_esCeroSiSeDevuelveAntesOEnLaFecha() {
        LocalDate prevista = LocalDate.of(2026, 10, 10);
        assertThat(CalculadoraPenalidades.diasAtraso(prevista, LocalDate.of(2026, 10, 8))).isZero();
        assertThat(CalculadoraPenalidades.diasAtraso(prevista, prevista)).isZero();
    }

    @Test
    void diasAtraso_cuentaLosDiasDespuesDeLaFechaPrevista() {
        LocalDate prevista = LocalDate.of(2026, 10, 10);
        assertThat(CalculadoraPenalidades.diasAtraso(prevista, LocalDate.of(2026, 10, 13))).isEqualTo(3);
    }

    @Test
    void sinAtrasoYEnBuenEstado_noGeneraPenalidades() {
        assertThat(CalculadoraPenalidades.calcular(precioDia, 0, EstadoConservacion.EXCELENTE, tarifas)).isEmpty();
        assertThat(CalculadoraPenalidades.calcular(precioDia, 0, EstadoConservacion.BUENO, tarifas)).isEmpty();
    }

    @Test
    void atraso_seCobraPorDiaSobreElPrecioDiario() {
        List<LineaPenalidad> lineas = CalculadoraPenalidades.calcular(precioDia, 3, EstadoConservacion.BUENO, tarifas);

        assertThat(lineas).hasSize(1);
        // 25.00 x 0.50 x 3 dias = 37.50
        assertThat(lineas.get(0).monto()).isEqualByComparingTo("37.50");
        assertThat(lineas.get(0).tarifa().getTipoPenalidad()).isEqualTo(TipoPenalidad.ATRASO);
    }

    @Test
    void estadoRegular_cobraDanoLeve() {
        List<LineaPenalidad> lineas = CalculadoraPenalidades.calcular(precioDia, 0, EstadoConservacion.REGULAR, tarifas);

        assertThat(lineas).hasSize(1);
        assertThat(lineas.get(0).monto()).isEqualByComparingTo("50.00");
    }

    @Test
    void estadoDanado_cobraDanoGrave() {
        List<LineaPenalidad> lineas = CalculadoraPenalidades.calcular(precioDia, 0, EstadoConservacion.DANADO, tarifas);

        assertThat(lineas).hasSize(1);
        assertThat(lineas.get(0).monto()).isEqualByComparingTo("200.00");
    }

    @Test
    void atrasoMasDano_sumaAmbasPenalidades() {
        List<LineaPenalidad> lineas = CalculadoraPenalidades.calcular(precioDia, 3, EstadoConservacion.DANADO, tarifas);

        assertThat(lineas).hasSize(2);
        assertThat(CalculadoraPenalidades.total(lineas)).isEqualByComparingTo("237.50");
    }

    @Test
    void sinTarifaActivaParaElConcepto_lanzaError() {
        List<TarifaPenalidad> soloAtraso = List.of(tarifas.get(0));

        assertThatThrownBy(() -> CalculadoraPenalidades.calcular(precioDia, 0, EstadoConservacion.DANADO, soloAtraso))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DANO_GRAVE");
    }
}
