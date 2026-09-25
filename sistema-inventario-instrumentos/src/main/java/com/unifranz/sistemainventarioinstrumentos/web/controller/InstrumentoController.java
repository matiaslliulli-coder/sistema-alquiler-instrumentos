package com.unifranz.sistemainventarioinstrumentos.web.controller;

import com.unifranz.sistemainventarioinstrumentos.application.dto.ComparacionPreciosResponse;
import com.unifranz.sistemainventarioinstrumentos.application.dto.InstrumentoRequest;
import com.unifranz.sistemainventarioinstrumentos.application.dto.InstrumentoResponse;
import com.unifranz.sistemainventarioinstrumentos.application.service.InstrumentoService;
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
@RequestMapping("/api/instrumentos")
public class InstrumentoController {

    private final InstrumentoService instrumentoService;

    public InstrumentoController(InstrumentoService instrumentoService) {
        this.instrumentoService = instrumentoService;
    }

    /** Registrar un instrumento en el inventario. Responde 201 con el codigo generado. */
    @PostMapping
    public ResponseEntity<InstrumentoResponse> registrar(@Valid @RequestBody InstrumentoRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instrumentoService.registrar(solicitud));
    }

    /** Todos los instrumentos activos, en cualquier estado de disponibilidad. */
    @GetMapping
    public List<InstrumentoResponse> listar() {
        return instrumentoService.listarActivos();
    }

    /** Catalogo: instrumentos DISPONIBLES filtrados por tipo, categoria y ciudad (todos opcionales). */
    @GetMapping("/buscar")
    public List<InstrumentoResponse> buscar(
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "ciudad", required = false) String ciudad) {
        return instrumentoService.buscarDisponibles(tipo, categoria, ciudad);
    }

    /** Comparar el precio de un mismo instrumento entre tiendas (de menor a mayor precio). */
    @GetMapping("/comparar-precios")
    public ComparacionPreciosResponse compararPrecios(
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "marca", required = false) String marca,
            @RequestParam(name = "modelo", required = false) String modelo) {
        return instrumentoService.compararPrecios(nombre, marca, modelo);
    }

    @GetMapping("/{codInstrumento}")
    public InstrumentoResponse buscarPorCodigo(@PathVariable("codInstrumento") String codInstrumento) {
        return instrumentoService.buscarPorCodigo(codInstrumento);
    }

    /** Comparar precios tomando como referencia un instrumento concreto. */
    @GetMapping("/{codInstrumento}/comparar-precios")
    public ComparacionPreciosResponse compararPreciosDe(@PathVariable("codInstrumento") String codInstrumento) {
        return instrumentoService.compararPreciosDe(codInstrumento);
    }
}
