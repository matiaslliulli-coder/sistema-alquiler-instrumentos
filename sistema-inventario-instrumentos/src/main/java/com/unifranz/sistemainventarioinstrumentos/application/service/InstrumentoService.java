package com.unifranz.sistemainventarioinstrumentos.application.service;

import com.unifranz.sistemainventarioinstrumentos.application.dto.ComparacionPreciosResponse;
import com.unifranz.sistemainventarioinstrumentos.application.dto.InstrumentoRequest;
import com.unifranz.sistemainventarioinstrumentos.application.dto.InstrumentoResponse;
import com.unifranz.sistemainventarioinstrumentos.application.dto.PrecioTiendaResponse;
import com.unifranz.sistemainventarioinstrumentos.domain.Instrumento;
import com.unifranz.sistemainventarioinstrumentos.domain.Marca;
import com.unifranz.sistemainventarioinstrumentos.domain.Subcategoria;
import com.unifranz.sistemainventarioinstrumentos.domain.Tienda;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.InstrumentoRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.InstrumentoSpecifications;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.MarcaRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.SubcategoriaRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.TiendaRepository;
import com.unifranz.sistemainventarioinstrumentos.web.exception.RecursoNoEncontradoException;
import com.unifranz.sistemainventarioinstrumentos.web.exception.ReglaNegocioException;
import com.unifranz.sistemainventarioinstrumentos.web.exception.SolicitudInvalidaException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InstrumentoService {

    private static final Sort ORDEN_PRECIO = Sort.by(Sort.Direction.ASC, "precioAlquiler")
            .and(Sort.by(Sort.Direction.ASC, "codInstrumento"));

    private final InstrumentoRepository instrumentoRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final MarcaRepository marcaRepository;
    private final TiendaRepository tiendaRepository;

    public InstrumentoService(InstrumentoRepository instrumentoRepository, SubcategoriaRepository subcategoriaRepository,
                              MarcaRepository marcaRepository, TiendaRepository tiendaRepository) {
        this.instrumentoRepository = instrumentoRepository;
        this.subcategoriaRepository = subcategoriaRepository;
        this.marcaRepository = marcaRepository;
        this.tiendaRepository = tiendaRepository;
    }

    /** Registra un instrumento nuevo: codigo unico automatico, estado DISPONIBLE y categoria segun su subcategoria. */
    @Transactional
    public InstrumentoResponse registrar(InstrumentoRequest solicitud) {
        Subcategoria subcategoria = subcategoriaRepository.findById(solicitud.codSubcategoria())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la subcategoria con codigo " + solicitud.codSubcategoria()));
        Marca marca = marcaRepository.findById(solicitud.codMarca())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la marca con codigo " + solicitud.codMarca()));
        Tienda tienda = tiendaRepository.findConCiudadById(solicitud.codTienda())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la tienda con codigo " + solicitud.codTienda()));
        if (!tienda.estaActiva()) {
            throw new ReglaNegocioException("La tienda " + tienda.getNombreTienda() + " esta inactiva");
        }

        Instrumento instrumento = new Instrumento(
                generarCodigo(),
                solicitud.nombreInstrumento().trim(),
                subcategoria,
                marca,
                solicitud.modeloInstrumento().trim(),
                tienda,
                solicitud.precioAlquiler());

        return InstrumentoResponse.desde(instrumentoRepository.save(instrumento));
    }

    @Transactional(readOnly = true)
    public List<InstrumentoResponse> listarActivos() {
        return instrumentoRepository.findAll(InstrumentoSpecifications.activos(), ORDEN_PRECIO).stream()
                .map(InstrumentoResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public InstrumentoResponse buscarPorCodigo(String codInstrumento) {
        return InstrumentoResponse.desde(obtener(codInstrumento));
    }

    /** Catalogo: solo instrumentos DISPONIBLES, filtrados por tipo, categoria y ciudad, del mas barato al mas caro. */
    @Transactional(readOnly = true)
    public List<InstrumentoResponse> buscarDisponibles(String tipo, String categoria, String ciudad) {
        return instrumentoRepository
                .findAll(InstrumentoSpecifications.disponibles(tipo, categoria, ciudad, null), ORDEN_PRECIO)
                .stream()
                .map(InstrumentoResponse::desde)
                .toList();
    }

    /** Compara el precio de un mismo instrumento (nombre, y opcionalmente marca y modelo) entre las tiendas. */
    @Transactional(readOnly = true)
    public ComparacionPreciosResponse compararPrecios(String nombre, String marca, String modelo) {
        if (nombre == null || nombre.isBlank()) {
            throw new SolicitudInvalidaException("Debe indicar el nombre del instrumento a comparar");
        }
        List<Instrumento> encontrados = instrumentoRepository.findAll(
                InstrumentoSpecifications.disponibles(null, null, null, null)
                        .and(InstrumentoSpecifications.mismoInstrumento(nombre, marca, modelo)),
                ORDEN_PRECIO);

        List<PrecioTiendaResponse> filas = encontrados.stream().map(PrecioTiendaResponse::desde).toList();
        if (filas.isEmpty()) {
            return new ComparacionPreciosResponse(nombre.trim(), marca, modelo, 0, null, null, null, filas);
        }
        BigDecimal minimo = filas.get(0).precioAlquiler();
        BigDecimal maximo = filas.get(filas.size() - 1).precioAlquiler();
        Instrumento referencia = encontrados.get(0);
        return new ComparacionPreciosResponse(
                referencia.getNombreInstrumento(),
                referencia.getMarca().getNombreMarca(),
                referencia.getModeloInstrumento(),
                filas.size(),
                minimo,
                maximo,
                maximo.subtract(minimo),
                filas);
    }

    /** Compara los precios tomando como referencia un instrumento concreto (mismo nombre, marca y modelo). */
    @Transactional(readOnly = true)
    public ComparacionPreciosResponse compararPreciosDe(String codInstrumento) {
        Instrumento base = obtener(codInstrumento);
        return compararPrecios(base.getNombreInstrumento(), base.getMarca().getNombreMarca(), base.getModeloInstrumento());
    }

    private Instrumento obtener(String codInstrumento) {
        return instrumentoRepository.findByCodInstrumento(codInstrumento.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el instrumento con codigo " + codInstrumento));
    }

    private String generarCodigo() {
        return String.format("INS-%06d", instrumentoRepository.siguienteNumero());
    }
}
