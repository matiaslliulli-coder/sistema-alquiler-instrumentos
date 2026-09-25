package com.unifranz.sistemainventarioinstrumentos.web.controller;

import com.unifranz.sistemainventarioinstrumentos.application.dto.CierreMantenimientoRequest;
import com.unifranz.sistemainventarioinstrumentos.application.dto.MantenimientoRequest;
import com.unifranz.sistemainventarioinstrumentos.application.dto.MantenimientoResponse;
import com.unifranz.sistemainventarioinstrumentos.application.service.MantenimientoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    /** Registrar un instrumento en mantenimiento (por ejemplo, devuelto con danos). */
    @PostMapping
    public ResponseEntity<MantenimientoResponse> registrarIngreso(@Valid @RequestBody MantenimientoRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mantenimientoService.registrarIngreso(solicitud));
    }

    /** Cerrar el registro: el instrumento vuelve a estar DISPONIBLE. */
    @PutMapping("/{codMantenimiento}/cerrar")
    public MantenimientoResponse cerrar(@PathVariable("codMantenimiento") Integer codMantenimiento,
                                        @Valid @RequestBody CierreMantenimientoRequest solicitud) {
        return mantenimientoService.cerrar(codMantenimiento, solicitud);
    }

    @GetMapping
    public List<MantenimientoResponse> listar(@RequestParam(name = "estado", required = false) String estado) {
        return mantenimientoService.listar(estado);
    }

    @GetMapping("/{codMantenimiento}")
    public MantenimientoResponse obtener(@PathVariable("codMantenimiento") Integer codMantenimiento) {
        return mantenimientoService.obtener(codMantenimiento);
    }
}
