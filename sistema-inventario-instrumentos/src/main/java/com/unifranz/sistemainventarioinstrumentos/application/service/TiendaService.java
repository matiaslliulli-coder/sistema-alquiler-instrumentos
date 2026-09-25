package com.unifranz.sistemainventarioinstrumentos.application.service;

import com.unifranz.sistemainventarioinstrumentos.application.dto.TiendaCercanaResponse;
import com.unifranz.sistemainventarioinstrumentos.application.dto.TiendaRequest;
import com.unifranz.sistemainventarioinstrumentos.application.dto.TiendaResponse;
import com.unifranz.sistemainventarioinstrumentos.domain.Ciudad;
import com.unifranz.sistemainventarioinstrumentos.domain.HaversineCalculator;
import com.unifranz.sistemainventarioinstrumentos.domain.Instrumento;
import com.unifranz.sistemainventarioinstrumentos.domain.Tienda;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.CiudadRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.InstrumentoRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.InstrumentoSpecifications;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.TiendaRepository;
import com.unifranz.sistemainventarioinstrumentos.web.exception.RecursoNoEncontradoException;
import com.unifranz.sistemainventarioinstrumentos.web.exception.SolicitudInvalidaException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TiendaService {

    private final TiendaRepository tiendaRepository;
    private final CiudadRepository ciudadRepository;
    private final InstrumentoRepository instrumentoRepository;

    public TiendaService(TiendaRepository tiendaRepository, CiudadRepository ciudadRepository,
                         InstrumentoRepository instrumentoRepository) {
        this.tiendaRepository = tiendaRepository;
        this.ciudadRepository = ciudadRepository;
        this.instrumentoRepository = instrumentoRepository;
    }

    @Transactional(readOnly = true)
    public List<TiendaResponse> listarActivas() {
        return tiendaRepository.findActivasConCiudad().stream().map(TiendaResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public TiendaResponse obtener(Integer codTienda) {
        return TiendaResponse.desde(tiendaRepository.findConCiudadById(codTienda)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la tienda con codigo " + codTienda)));
    }

    @Transactional
    public TiendaResponse registrar(TiendaRequest solicitud) {
        Ciudad ciudad = ciudadRepository.findById(solicitud.codCiudad())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la ciudad con codigo " + solicitud.codCiudad()));
        Tienda tienda = new Tienda(
                solicitud.nombreTienda().trim(),
                ciudad,
                solicitud.direccionTienda(),
                solicitud.telefonoTienda(),
                solicitud.latitudTienda(),
                solicitud.longitudTienda(),
                solicitud.responsableTienda());
        return TiendaResponse.desde(tiendaRepository.save(tienda));
    }

    /**
     * Sucursales mas cercanas a la ubicacion del cliente que tienen disponible el instrumento buscado.
     * La distancia se calcula con la formula de Haversine y el listado va de menor a mayor distancia.
     *
     * @param tipo     tipo de instrumento (subcategoria), opcional
     * @param nombre   nombre del instrumento, opcional
     * @param radioKm  distancia maxima en km, opcional
     * @param limite   cantidad maxima de tiendas a devolver, opcional
     */
    @Transactional(readOnly = true)
    public List<TiendaCercanaResponse> tiendasCercanas(double latitud, double longitud, String tipo, String nombre,
                                                       Double radioKm, Integer limite) {
        if (latitud < -90 || latitud > 90) {
            throw new SolicitudInvalidaException("La latitud debe estar entre -90 y 90");
        }
        if (longitud < -180 || longitud > 180) {
            throw new SolicitudInvalidaException("La longitud debe estar entre -180 y 180");
        }
        if (radioKm != null && radioKm <= 0) {
            throw new SolicitudInvalidaException("El radio debe ser mayor a 0 km");
        }
        if (limite != null && limite <= 0) {
            throw new SolicitudInvalidaException("El limite debe ser mayor a 0");
        }

        List<Instrumento> disponibles = instrumentoRepository.findAll(
                InstrumentoSpecifications.disponibles(tipo, null, null, nombre),
                Sort.by(Sort.Direction.ASC, "precioAlquiler"));

        Map<Integer, List<Instrumento>> porTienda = new LinkedHashMap<>();
        for (Instrumento i : disponibles) {
            porTienda.computeIfAbsent(i.getTienda().getCodTienda(), k -> new ArrayList<>()).add(i);
        }

        List<TiendaCercanaResponse> resultado = new ArrayList<>();
        for (List<Instrumento> deLaTienda : porTienda.values()) {
            Tienda tienda = deLaTienda.get(0).getTienda();
            double distancia = HaversineCalculator.distanciaKm(
                    latitud, longitud,
                    tienda.getLatitudTienda().doubleValue(),
                    tienda.getLongitudTienda().doubleValue());
            if (radioKm != null && distancia > radioKm) {
                continue;
            }
            resultado.add(TiendaCercanaResponse.desde(tienda, redondear(distancia), deLaTienda));
        }

        resultado.sort(Comparator.comparingDouble(TiendaCercanaResponse::distanciaKm));
        if (limite != null && resultado.size() > limite) {
            return new ArrayList<>(resultado.subList(0, limite));
        }
        return resultado;
    }

    private double redondear(double km) {
        return Math.round(km * 100.0) / 100.0;
    }
}
