package com.unifranz.sistemaalquilerinstrumentos.web.controller;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.ClienteRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.ClienteResponse;
import com.unifranz.sistemaalquilerinstrumentos.application.service.ClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{codCliente}")
    public ClienteResponse obtener(@PathVariable("codCliente") Integer codCliente) {
        return clienteService.obtener(codCliente);
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody ClienteRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.registrar(solicitud));
    }
}
