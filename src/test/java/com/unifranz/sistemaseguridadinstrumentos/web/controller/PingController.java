package com.unifranz.sistemaseguridadinstrumentos.web.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Comprueba que la API y la conexion a PostgreSQL funcionan (publico, sin token). */
@RestController
@RequestMapping("/api/ping")
public class PingController {

    private final JdbcTemplate jdbcTemplate;

    public PingController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public Map<String, Object> ping() {
        Integer uno = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("modulo", "Seguridad");
        respuesta.put("api", "OK");
        respuesta.put("postgresql", uno != null && uno == 1 ? "OK" : "ERROR");
        respuesta.put("fecha", LocalDateTime.now().toString());
        return respuesta;
    }
}
