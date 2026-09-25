package com.unifranz.sistemainventarioinstrumentos.web.controller;

import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.InstrumentoRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint basico para comprobar la cadena completa: Postman -> API -> JPA -> PostgreSQL. */
@RestController
@RequestMapping("/api/ping")
public class PingController {

    private final InstrumentoRepository instrumentoRepository;

    public PingController(InstrumentoRepository instrumentoRepository) {
        this.instrumentoRepository = instrumentoRepository;
    }

    @GetMapping
    public Map<String, Object> ping() {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("estado", "OK");
        respuesta.put("mensaje", "API del Sistema de Inventario de Instrumentos funcionando");
        respuesta.put("totalInstrumentosEnBaseDeDatos", instrumentoRepository.count());
        return respuesta;
    }
}
