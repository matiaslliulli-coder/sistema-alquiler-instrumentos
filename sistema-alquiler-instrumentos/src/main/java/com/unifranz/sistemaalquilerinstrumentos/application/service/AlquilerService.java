package com.unifranz.sistemaalquilerinstrumentos.application.service;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.AlquilerRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.AlquilerResponse;
import com.unifranz.sistemaalquilerinstrumentos.domain.Alquiler;
import com.unifranz.sistemaalquilerinstrumentos.domain.Cliente;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoAlquiler;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoInstrumento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Instrumento;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.AlquilerRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.ClienteRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.InstrumentoRepository;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.RecursoNoEncontradoException;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.ReglaNegocioException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Casos de uso del alquiler: registrar y consultar. */
@Service
@Transactional
public class AlquilerService {

    private final AlquilerRepository alquilerRepository;
    private final ClienteRepository clienteRepository;
    private final InstrumentoRepository instrumentoRepository;

    public AlquilerService(AlquilerRepository alquilerRepository, ClienteRepository clienteRepository,
                           InstrumentoRepository instrumentoRepository) {
        this.alquilerRepository = alquilerRepository;
        this.clienteRepository = clienteRepository;
        this.instrumentoRepository = instrumentoRepository;
    }

    /** Registra un alquiler (cliente, instrumento, plazo) y deja el instrumento en estado ALQUILADO. */
    public AlquilerResponse registrar(AlquilerRequest solicitud) {
        Cliente cliente = clienteRepository.findById(solicitud.codCliente())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente " + solicitud.codCliente()));
        if (!cliente.estaActivo()) {
            throw new ReglaNegocioException("El cliente " + cliente.getNombreCompleto() + " esta inactivo");
        }

        Instrumento instrumento = instrumentoRepository.findByCodInstrumento(solicitud.codInstrumento().trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el instrumento " + solicitud.codInstrumento()));
        if (!instrumento.estaActivo()) {
            throw new ReglaNegocioException("El instrumento " + instrumento.getCodInstrumento() + " esta dado de baja");
        }
        if (instrumento.getEstadoInstrumento() != EstadoInstrumento.DISPONIBLE) {
            throw new ReglaNegocioException("El instrumento " + instrumento.getCodInstrumento()
                    + " no esta disponible (estado actual: " + instrumento.getEstadoInstrumento().getValorBd() + ")");
        }

        LocalDate fecha = solicitud.fechaAlquiler() != null ? solicitud.fechaAlquiler() : LocalDate.now();
        Alquiler alquiler = new Alquiler(cliente, instrumento, fecha, solicitud.diasAlquiler());

        instrumento.setEstadoInstrumento(EstadoInstrumento.ALQUILADO);
        return AlquilerResponse.desde(alquilerRepository.save(alquiler));
    }

    @Transactional(readOnly = true)
    public List<AlquilerResponse> listar(EstadoAlquiler estado) {
        List<Alquiler> alquileres = estado == null
                ? alquilerRepository.findAll()
                : alquilerRepository.findByEstadoAlquiler(estado);
        return alquileres.stream().map(AlquilerResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public AlquilerResponse obtener(Integer codAlquiler) {
        return AlquilerResponse.desde(alquilerRepository.findById(codAlquiler)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el alquiler " + codAlquiler)));
    }

    /** Marca como ATRASADO los alquileres activos cuyo plazo ya vencio y devuelve todos los atrasados. */
    public List<AlquilerResponse> atrasados() {
        alquilerRepository.findByEstadoAlquilerAndFechaDevolucionPrevistaBefore(EstadoAlquiler.ACTIVO, LocalDate.now())
                .forEach(Alquiler::marcarAtrasado);
        return alquilerRepository.findByEstadoAlquiler(EstadoAlquiler.ATRASADO).stream()
                .map(AlquilerResponse::desde).toList();
    }
}
