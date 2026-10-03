package com.unifranz.sistemaseguridadinstrumentos.web.controller;

import com.unifranz.sistemaseguridadinstrumentos.application.dto.AuditoriaResponse;
import com.unifranz.sistemaseguridadinstrumentos.application.service.AuditoriaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Consulta de la bitacora (solo ADMINISTRADOR). Los filtros avanzados llegan en la semana 8. */
@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public List<AuditoriaResponse> listar(@RequestParam(name = "accion", required = false) String accion) {
        return auditoriaService.listar(accion);
    }
}
