package com.unifranz.sistemaalquilerinstrumentos.application.service;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.HistorialInstrumentoResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.HistorialInstrumentoResponse.Item;
import com.unifranz.sistemaalquilerinstrumentos.domain.Alquiler;
import com.unifranz.sistemaalquilerinstrumentos.domain.Devolucion;
import com.unifranz.sistemaalquilerinstrumentos.domain.Instrumento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Penalidad;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.AlquilerRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.DevolucionRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.InstrumentoRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.PenalidadRepository;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.RecursoNoEncontradoException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Consulta del historial de alquileres de un instrumento. */
@Service
@Transactional(readOnly = true)
public class HistorialService {

    private final InstrumentoRepository instrumentoRepository;
    private final AlquilerRepository alquilerRepository;
    private final DevolucionRepository devolucionRepository;
    private final PenalidadRepository penalidadRepository;

    public HistorialService(InstrumentoRepository instrumentoRepository, AlquilerRepository alquilerRepository,
                            DevolucionRepository devolucionRepository, PenalidadRepository penalidadRepository) {
        this.instrumentoRepository = instrumentoRepository;
        this.alquilerRepository = alquilerRepository;
        this.devolucionRepository = devolucionRepository;
        this.penalidadRepository = penalidadRepository;
    }

    public HistorialInstrumentoResponse historialDe(String codInstrumento) {
        Instrumento instrumento = instrumentoRepository.findByCodInstrumento(codInstrumento)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el instrumento " + codInstrumento));

        List<Item> items = alquilerRepository.findByInstrumentoCodInstrumentoOrderByFechaAlquilerDesc(codInstrumento)
                .stream().map(this::aItem).toList();

        return new HistorialInstrumentoResponse(instrumento.getCodInstrumento(), instrumento.getNombreInstrumento(),
                instrumento.getEstadoInstrumento().getValorBd(), items.size(), items);
    }

    private Item aItem(Alquiler a) {
        Optional<Devolucion> devolucion = devolucionRepository.findByAlquilerCodAlquiler(a.getCodAlquiler());
        BigDecimal totalPenalidades = devolucion
                .map(d -> penalidadRepository.findByDevolucionCodDevolucion(d.getCodDevolucion()).stream()
                        .map(Penalidad::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add))
                .orElse(BigDecimal.ZERO);
        return new Item(
                a.getCodAlquiler(),
                a.getCliente().getNombreCompleto(),
                a.getCliente().getCiCliente(),
                a.getFechaAlquiler(),
                a.getFechaDevolucionPrevista(),
                devolucion.map(Devolucion::getFechaDevolucionReal).orElse(null),
                a.getEstadoAlquiler().name(),
                devolucion.map(d -> d.getEstadoConservacion().name()).orElse(null),
                totalPenalidades);
    }
}
