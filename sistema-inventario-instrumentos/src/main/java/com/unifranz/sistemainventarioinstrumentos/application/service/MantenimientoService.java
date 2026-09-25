package com.unifranz.sistemainventarioinstrumentos.application.service;

import com.unifranz.sistemainventarioinstrumentos.application.dto.CierreMantenimientoRequest;
import com.unifranz.sistemainventarioinstrumentos.application.dto.MantenimientoRequest;
import com.unifranz.sistemainventarioinstrumentos.application.dto.MantenimientoResponse;
import com.unifranz.sistemainventarioinstrumentos.domain.EstadoInstrumento;
import com.unifranz.sistemainventarioinstrumentos.domain.Instrumento;
import com.unifranz.sistemainventarioinstrumentos.domain.Mantenimiento;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.InstrumentoRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.MantenimientoRepository;
import com.unifranz.sistemainventarioinstrumentos.web.exception.RecursoNoEncontradoException;
import com.unifranz.sistemainventarioinstrumentos.web.exception.ReglaNegocioException;
import com.unifranz.sistemainventarioinstrumentos.web.exception.SolicitudInvalidaException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final InstrumentoRepository instrumentoRepository;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository,
                                InstrumentoRepository instrumentoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.instrumentoRepository = instrumentoRepository;
    }

    /** Registra el ingreso a mantenimiento: el instrumento pasa a EN MANTENIMIENTO y sale del catalogo. */
    @Transactional
    public MantenimientoResponse registrarIngreso(MantenimientoRequest solicitud) {
        Instrumento instrumento = instrumentoRepository.findByCodInstrumento(solicitud.codInstrumento().trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el instrumento con codigo " + solicitud.codInstrumento()));
        if (!instrumento.estaActivo()) {
            throw new ReglaNegocioException("El instrumento esta dado de baja");
        }
        if (instrumento.getEstadoInstrumento() == EstadoInstrumento.EN_MANTENIMIENTO) {
            throw new ReglaNegocioException("El instrumento ya esta en mantenimiento");
        }
        if (instrumento.getEstadoInstrumento() == EstadoInstrumento.ALQUILADO) {
            throw new ReglaNegocioException("El instrumento esta alquilado: registre primero su devolucion");
        }

        Mantenimiento mantenimiento = new Mantenimiento(
                instrumento,
                solicitud.descripcionDano().trim(),
                solicitud.codDevolucion(),
                solicitud.idUsuario());
        instrumento.setEstadoInstrumento(EstadoInstrumento.EN_MANTENIMIENTO);

        return MantenimientoResponse.desde(mantenimientoRepository.save(mantenimiento));
    }

    /** Cierra el registro: el instrumento vuelve a estar DISPONIBLE y reaparece en el catalogo. */
    @Transactional
    public MantenimientoResponse cerrar(Integer codMantenimiento, CierreMantenimientoRequest solicitud) {
        Mantenimiento mantenimiento = mantenimientoRepository.findById(codMantenimiento)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el mantenimiento con codigo " + codMantenimiento));
        if (mantenimiento.estaCerrado()) {
            throw new ReglaNegocioException("El registro de mantenimiento ya esta cerrado");
        }
        mantenimiento.cerrar(solicitud.trabajoRealizado().trim(), solicitud.costoReparacion());
        mantenimiento.getInstrumento().setEstadoInstrumento(EstadoInstrumento.DISPONIBLE);
        return MantenimientoResponse.desde(mantenimiento);
    }

    @Transactional(readOnly = true)
    public MantenimientoResponse obtener(Integer codMantenimiento) {
        return MantenimientoResponse.desde(mantenimientoRepository.findById(codMantenimiento)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el mantenimiento con codigo " + codMantenimiento)));
    }

    /** Lista los mantenimientos; con estado EN_PROCESO o CERRADO se filtra por ese estado. */
    @Transactional(readOnly = true)
    public List<MantenimientoResponse> listar(String estado) {
        List<Mantenimiento> lista;
        if (estado == null || estado.isBlank()) {
            lista = mantenimientoRepository.findAllByOrderByFechaIngresoDescCodMantenimientoDesc();
        } else {
            String valor = estado.trim().toUpperCase().replace('_', ' ');
            if (!Mantenimiento.EN_PROCESO.equals(valor) && !Mantenimiento.CERRADO.equals(valor)) {
                throw new SolicitudInvalidaException("El estado debe ser EN_PROCESO o CERRADO");
            }
            lista = mantenimientoRepository.findByEstadoMantenimientoOrderByFechaIngresoDescCodMantenimientoDesc(valor);
        }
        return lista.stream().map(MantenimientoResponse::desde).toList();
    }
}
