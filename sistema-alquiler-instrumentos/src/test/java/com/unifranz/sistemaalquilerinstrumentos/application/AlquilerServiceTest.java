package com.unifranz.sistemaalquilerinstrumentos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.AlquilerRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.AlquilerResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.service.AlquilerService;
import com.unifranz.sistemaalquilerinstrumentos.domain.Alquiler;
import com.unifranz.sistemaalquilerinstrumentos.domain.Cliente;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoInstrumento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Instrumento;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.AlquilerRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.ClienteRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.InstrumentoRepository;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.RecursoNoEncontradoException;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.ReglaNegocioException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlquilerServiceTest {

    @Mock
    private AlquilerRepository alquilerRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private InstrumentoRepository instrumentoRepository;

    private AlquilerService servicio;
    private Cliente cliente;
    private Instrumento guitarra;

    @BeforeEach
    void preparar() {
        servicio = new AlquilerService(alquilerRepository, clienteRepository, instrumentoRepository);
        cliente = new Cliente("7654321 LP", "Mariana", "Quispe", "71234567", "m@example.com", "La Paz");
        guitarra = InstrumentosDePrueba.guitarra("INS-000001", "25.00");
    }

    @Test
    void registrar_calculaFechaPrevistaMontoYMarcaElInstrumentoComoAlquilado() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(instrumentoRepository.findByCodInstrumento("INS-000001")).thenReturn(Optional.of(guitarra));
        when(alquilerRepository.save(any(Alquiler.class))).thenAnswer(inv -> inv.getArgument(0));

        AlquilerResponse respuesta = servicio.registrar(
                new AlquilerRequest(1, "INS-000001", 5, LocalDate.of(2026, 10, 1)));

        assertThat(respuesta.fechaDevolucionPrevista()).isEqualTo(LocalDate.of(2026, 10, 6));
        assertThat(respuesta.montoTotal()).isEqualByComparingTo("125.00");
        assertThat(respuesta.estadoAlquiler()).isEqualTo("ACTIVO");
        assertThat(guitarra.getEstadoInstrumento()).isEqualTo(EstadoInstrumento.ALQUILADO);
        verify(alquilerRepository).save(any(Alquiler.class));
    }

    @Test
    void registrar_rechazaUnInstrumentoQueYaEstaAlquilado() {
        guitarra.setEstadoInstrumento(EstadoInstrumento.ALQUILADO);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(instrumentoRepository.findByCodInstrumento("INS-000001")).thenReturn(Optional.of(guitarra));

        assertThatThrownBy(() -> servicio.registrar(new AlquilerRequest(1, "INS-000001", 3, null)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("no esta disponible");
    }

    @Test
    void registrar_rechazaUnInstrumentoEnMantenimiento() {
        guitarra.setEstadoInstrumento(EstadoInstrumento.EN_MANTENIMIENTO);
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(instrumentoRepository.findByCodInstrumento("INS-000001")).thenReturn(Optional.of(guitarra));

        assertThatThrownBy(() -> servicio.registrar(new AlquilerRequest(1, "INS-000001", 3, null)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void registrar_fallaSiElClienteNoExiste() {
        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.registrar(new AlquilerRequest(99, "INS-000001", 3, null)))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("cliente");
    }

    @Test
    void registrar_fallaSiElInstrumentoNoExiste() {
        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(instrumentoRepository.findByCodInstrumento("INS-999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.registrar(new AlquilerRequest(1, "INS-999999", 3, null)))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("instrumento");
    }
}
