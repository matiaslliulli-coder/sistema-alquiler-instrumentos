package com.unifranz.sistemaalquilerinstrumentos.application.service;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.DevolucionRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.DevolucionResponse;
import com.unifranz.sistemaalquilerinstrumentos.domain.Alquiler;
import com.unifranz.sistemaalquilerinstrumentos.domain.CalculadoraPenalidades;
import com.unifranz.sistemaalquilerinstrumentos.domain.CalculadoraPenalidades.LineaPenalidad;
import com.unifranz.sistemaalquilerinstrumentos.domain.Devolucion;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoConservacion;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoInstrumento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Instrumento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Mantenimiento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Penalidad;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.AlquilerRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.DevolucionRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.MantenimientoRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.PenalidadRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.TarifaPenalidadRepository;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.RecursoNoEncontradoException;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.ReglaNegocioException;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.SolicitudInvalidaException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registra la devolucion, verifica el estado de conservacion y genera las penalidades.
 * Si el instrumento vuelve DANADO, abre automaticamente un registro de mantenimiento (modulo de inventario).
 */
@Service
@Transactional
public class DevolucionService {

    private final AlquilerRepository alquilerRepository;
    private final DevolucionRepository devolucionRepository;
    private final PenalidadRepository penalidadRepository;
    private final TarifaPenalidadRepository tarifaRepository;
    private final MantenimientoRepository mantenimientoRepository;

    public DevolucionService(AlquilerRepository alquilerRepository, DevolucionRepository devolucionRepository,
                             PenalidadRepository penalidadRepository, TarifaPenalidadRepository tarifaRepository,
                             MantenimientoRepository mantenimientoRepository) {
        this.alquilerRepository = alquilerRepository;
        this.devolucionRepository = devolucionRepository;
        this.penalidadRepository = penalidadRepository;
        this.tarifaRepository = tarifaRepository;
        this.mantenimientoRepository = mantenimientoRepository;
    }

    public DevolucionResponse registrar(Integer codAlquiler, DevolucionRequest solicitud) {
        Alquiler alquiler = alquilerRepository.findById(codAlquiler)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el alquiler " + codAlquiler));
        if (!alquiler.estaPendiente()) {
            throw new ReglaNegocioException("El alquiler " + codAlquiler + " ya fue devuelto");
        }

        LocalDate fechaReal = solicitud.fechaDevolucion() != null ? solicitud.fechaDevolucion() : LocalDate.now();
        if (fechaReal.isBefore(alquiler.getFechaAlquiler())) {
            throw new SolicitudInvalidaException("La fecha de devolucion no puede ser anterior a la fecha del alquiler");
        }

        int diasAtraso = CalculadoraPenalidades.diasAtraso(alquiler.getFechaDevolucionPrevista(), fechaReal);
        List<LineaPenalidad> lineas = CalculadoraPenalidades.calcular(
                alquiler.getPrecioDia(), diasAtraso, solicitud.estadoConservacion(), tarifaRepository.findByActivaTrue());

        Devolucion devolucion = devolucionRepository.save(new Devolucion(
                alquiler, fechaReal, solicitud.estadoConservacion(), solicitud.observaciones(), diasAtraso));
        for (LineaPenalidad linea : lineas) {
            penalidadRepository.save(new Penalidad(devolucion, linea.tarifa(), linea.concepto(), linea.monto()));
        }

        alquiler.marcarDevuelto();
        Instrumento instrumento = alquiler.getInstrumento();
        Integer codMantenimiento = null;
        if (solicitud.estadoConservacion() == EstadoConservacion.DANADO) {
            String descripcion = (solicitud.observaciones() != null && !solicitud.observaciones().isBlank())
                    ? solicitud.observaciones().trim()
                    : "Dano detectado en la devolucion del alquiler " + alquiler.getCodAlquiler();
            Mantenimiento mantenimiento = mantenimientoRepository.save(
                    new Mantenimiento(instrumento, descripcion, devolucion.getCodDevolucion(), null));
            codMantenimiento = mantenimiento.getCodMantenimiento();
            instrumento.setEstadoInstrumento(EstadoInstrumento.EN_MANTENIMIENTO);
        } else {
            instrumento.setEstadoInstrumento(EstadoInstrumento.DISPONIBLE);
        }

        List<DevolucionResponse.PenalidadItem> items = lineas.stream()
                .map(l -> new DevolucionResponse.PenalidadItem(l.tarifa().getTipoPenalidad().name(), l.concepto(), l.monto()))
                .toList();
        return new DevolucionResponse(
                devolucion.getCodDevolucion(),
                alquiler.getCodAlquiler(),
                instrumento.getCodInstrumento(),
                alquiler.getFechaDevolucionPrevista(),
                fechaReal,
                solicitud.estadoConservacion().name(),
                diasAtraso,
                items,
                CalculadoraPenalidades.total(lineas),
                instrumento.getEstadoInstrumento().getValorBd(),
                codMantenimiento);
    }
}
