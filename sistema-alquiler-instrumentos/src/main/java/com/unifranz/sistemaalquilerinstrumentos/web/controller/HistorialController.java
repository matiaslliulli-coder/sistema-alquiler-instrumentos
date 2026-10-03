package com.unifranz.sistemaalquilerinstrumentos.web.controller;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.HistorialInstrumentoResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.service.HistorialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Historial de alquileres de un instrumento (modulo Alquileres). Requiere sesion de Encargado o Administrador. */
@RestController
@RequestMapping("/api/instrumentos")
public class HistorialController {

    private final HistorialService historialService;

    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/{codInstrumento}/historial")
    public HistorialInstrumentoResponse historial(@PathVariable("codInstrumento") String codInstrumento) {
        return historialService.historialDe(codInstrumento);
    }
}
