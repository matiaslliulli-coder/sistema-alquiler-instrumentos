package com.unifranz.sistemaalquilerinstrumentos.web.controller;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.AlquilerRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.AlquilerResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.service.AlquilerService;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoAlquiler;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @PostMapping
    public ResponseEntity<AlquilerResponse> registrar(@Valid @RequestBody AlquilerRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alquilerService.registrar(solicitud));
    }

    @GetMapping
    public List<AlquilerResponse> listar(@RequestParam(name = "estado", required = false) EstadoAlquiler estado) {
        return alquilerService.listar(estado);
    }

    @GetMapping("/atrasados")
    public List<AlquilerResponse> atrasados() {
        return alquilerService.atrasados();
    }

    @GetMapping("/{codAlquiler}")
    public AlquilerResponse obtener(@PathVariable("codAlquiler") Integer codAlquiler) {
        return alquilerService.obtener(codAlquiler);
    }
}
