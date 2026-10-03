package com.unifranz.sistemaalquilerinstrumentos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.DevolucionRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.DevolucionResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.service.DevolucionService;
import com.unifranz.sistemaalquilerinstrumentos.domain.Alquiler;
import com.unifranz.sistemaalquilerinstrumentos.domain.Cliente;
import com.unifranz.sistemaalquilerinstrumentos.domain.Devolucion;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoAlquiler;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoConservacion;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoInstrumento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Instrumento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Mantenimiento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Penalidad;
import com.unifranz.sistemaalquilerinstrumentos.domain.TarifaPenalidad;
import com.unifranz.sistemaalquilerinstrumentos.domain.TipoCalculo;
import com.unifranz.sistemaalquilerinstrumentos.domain.TipoPenalidad;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.AlquilerRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.DevolucionRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.MantenimientoRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.PenalidadRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.TarifaPenalidadRepository;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.RecursoNoEncontradoException;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.ReglaNegocioException;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.SolicitudInvalidaException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DevolucionServiceTest {

    @Mock
    private AlquilerRepository alquilerRepository;
    @Mock
    private DevolucionRepository devolucionRepository;
    @Mock
    private PenalidadRepository penalidadRepository;
    @Mock
    private TarifaPenalidadRepository tarifaRepository;
    @Mock
    private MantenimientoRepository mantenimientoRepository;

    private DevolucionService servicio;
    private Instrumento guitarra;
    private Alquiler alquiler;

    @BeforeEach
    void preparar() {
        servicio = new DevolucionService(alquilerRepository, devolucionRepository, penalidadRepository, tarifaRepository,
                mantenimientoRepository);

        Cliente cliente = new Cliente("7654321 LP", "Mariana", "Quispe", null, null, null);
        guitarra = InstrumentosDePrueba.guitarra("INS-000001", "25.00");
        guitarra.setEstadoInstrumento(EstadoInstrumento.ALQUILADO);
        // Alquiler del 01/10/2026 por 5 dias -> debe devolverse el 06/10/2026
        alquiler = new Alquiler(cliente, guitarra, LocalDate.of(2026, 10, 1), 5);

        lenient().when(devolucionRepository.save(any(Devolucion.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(penalidadRepository.save(any(Penalidad.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(mantenimientoRepository.save(any(Mantenimiento.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(tarifaRepository.findByActivaTrue()).thenReturn(List.of(
                new TarifaPenalidad(TipoPenalidad.ATRASO, "Atraso", TipoCalculo.POR_DIA_ATRASO, new BigDecimal("0.50")),
                new TarifaPenalidad(TipoPenalidad.DANO_LEVE, "Dano leve", TipoCalculo.MONTO_FIJO, new BigDecimal("50.00")),
                new TarifaPenalidad(TipoPenalidad.DANO_GRAVE, "Dano grave", TipoCalculo.MONTO_FIJO, new BigDecimal("200.00"))));
    }

    @Test
    void devolucionATiempoYEnBuenEstado_noTienePenalidadesYLiberaElInstrumento() {
        when(alquilerRepository.findById(1)).thenReturn(Optional.of(alquiler));

        DevolucionResponse r = servicio.registrar(1,
                new DevolucionRequest(LocalDate.of(2026, 10, 6), EstadoConservacion.BUENO, null));

        assertThat(r.diasAtraso()).isZero();
        assertThat(r.penalidades()).isEmpty();
        assertThat(r.totalPenalidades()).isEqualByComparingTo("0");
        assertThat(guitarra.getEstadoInstrumento()).isEqualTo(EstadoInstrumento.DISPONIBLE);
        assertThat(alquiler.getEstadoAlquiler()).isEqualTo(EstadoAlquiler.DEVUELTO);
        verify(mantenimientoRepository, never()).save(any(Mantenimiento.class));
    }

    @Test
    void devolucionConAtrasoYDanoGrave_sumaPenalidadesYEnviaAMantenimiento() {
        when(alquilerRepository.findById(1)).thenReturn(Optional.of(alquiler));

        // 3 dias tarde: 25 x 0.50 x 3 = 37.50, mas dano grave 200.00
        DevolucionResponse r = servicio.registrar(1,
                new DevolucionRequest(LocalDate.of(2026, 10, 9), EstadoConservacion.DANADO, "Cuerda y tapa rotas"));

        assertThat(r.diasAtraso()).isEqualTo(3);
        assertThat(r.penalidades()).hasSize(2);
        assertThat(r.totalPenalidades()).isEqualByComparingTo("237.50");
        assertThat(r.estadoInstrumento()).isEqualTo("EN MANTENIMIENTO");
        assertThat(guitarra.getEstadoInstrumento()).isEqualTo(EstadoInstrumento.EN_MANTENIMIENTO);
        verify(mantenimientoRepository).save(any(Mantenimiento.class));
    }

    @Test
    void noSePuedeDevolverDosVecesElMismoAlquiler() {
        alquiler.marcarDevuelto();
        when(alquilerRepository.findById(1)).thenReturn(Optional.of(alquiler));

        assertThatThrownBy(() -> servicio.registrar(1,
                new DevolucionRequest(LocalDate.of(2026, 10, 6), EstadoConservacion.BUENO, null)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya fue devuelto");
    }

    @Test
    void laFechaDeDevolucionNoPuedeSerAnteriorAlAlquiler() {
        when(alquilerRepository.findById(1)).thenReturn(Optional.of(alquiler));

        assertThatThrownBy(() -> servicio.registrar(1,
                new DevolucionRequest(LocalDate.of(2026, 9, 20), EstadoConservacion.BUENO, null)))
                .isInstanceOf(SolicitudInvalidaException.class);
    }

    @Test
    void fallaSiElAlquilerNoExiste() {
        when(alquilerRepository.findById(77)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.registrar(77,
                new DevolucionRequest(null, EstadoConservacion.BUENO, null)))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }
}
