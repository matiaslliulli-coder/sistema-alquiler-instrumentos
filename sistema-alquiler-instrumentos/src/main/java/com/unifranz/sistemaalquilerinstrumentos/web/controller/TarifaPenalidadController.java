package com.unifranz.sistemaalquilerinstrumentos.web.controller;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.TarifaPenalidadResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.service.TarifaPenalidadService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tarifas-penalidad")
public class TarifaPenalidadController {

    private final TarifaPenalidadService tarifaService;

    public TarifaPenalidadController(TarifaPenalidadService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping
    public List<TarifaPenalidadResponse> listar() {
        return tarifaService.listar();
    }
}
