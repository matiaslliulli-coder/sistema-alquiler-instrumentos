package com.unifranz.sistemaalquilerinstrumentos.web.controller;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.DevolucionRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.DevolucionResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.service.DevolucionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alquileres/{codAlquiler}/devolucion")
public class DevolucionController {

    private final DevolucionService devolucionService;

    public DevolucionController(DevolucionService devolucionService) {
        this.devolucionService = devolucionService;
    }

    @PostMapping
    public ResponseEntity<DevolucionResponse> registrar(@PathVariable("codAlquiler") Integer codAlquiler,
                                                        @Valid @RequestBody DevolucionRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(devolucionService.registrar(codAlquiler, solicitud));
    }
}
