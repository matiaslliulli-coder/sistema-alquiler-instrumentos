package com.unifranz.sistemainventarioinstrumentos.web.controller;

import com.unifranz.sistemainventarioinstrumentos.application.dto.TiendaCercanaResponse;
import com.unifranz.sistemainventarioinstrumentos.application.dto.TiendaRequest;
import com.unifranz.sistemainventarioinstrumentos.application.dto.TiendaResponse;
import com.unifranz.sistemainventarioinstrumentos.application.service.TiendaService;
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
@RequestMapping("/api/tiendas")
public class TiendaController {

    private final TiendaService tiendaService;

    public TiendaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    public List<TiendaResponse> listar() {
        return tiendaService.listarActivas();
    }

    @PostMapping
    public ResponseEntity<TiendaResponse> registrar(@Valid @RequestBody TiendaRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tiendaService.registrar(solicitud));
    }

    /**
     * Sucursales mas cercanas al cliente (formula de Haversine) que tienen disponible el instrumento buscado.
     * Ejemplo: /api/tiendas/cercanas?latitud=-16.5&longitud=-68.15&tipo=Guitarra
     */
    @GetMapping("/cercanas")
    public List<TiendaCercanaResponse> cercanas(
            @RequestParam(name = "latitud") double latitud,
            @RequestParam(name = "longitud") double longitud,
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "radioKm", required = false) Double radioKm,
            @RequestParam(name = "limite", required = false) Integer limite) {
        return tiendaService.tiendasCercanas(latitud, longitud, tipo, nombre, radioKm, limite);
    }

    @GetMapping("/{codTienda}")
    public TiendaResponse obtener(@PathVariable("codTienda") Integer codTienda) {
        return tiendaService.obtener(codTienda);
    }
}
