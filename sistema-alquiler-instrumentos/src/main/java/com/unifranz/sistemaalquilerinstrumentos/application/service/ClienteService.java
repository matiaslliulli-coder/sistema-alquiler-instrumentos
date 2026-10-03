package com.unifranz.sistemaalquilerinstrumentos.application.service;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.ClienteRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.dto.ClienteResponse;
import com.unifranz.sistemaalquilerinstrumentos.domain.Cliente;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.ClienteRepository;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.RecursoNoEncontradoException;
import com.unifranz.sistemaalquilerinstrumentos.web.exception.ReglaNegocioException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(ClienteResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtener(Integer codCliente) {
        return ClienteResponse.desde(clienteRepository.findById(codCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el cliente " + codCliente)));
    }

    public ClienteResponse registrar(ClienteRequest solicitud) {
        String ci = solicitud.ciCliente().trim();
        if (clienteRepository.existsByCiCliente(ci)) {
            throw new ReglaNegocioException("Ya existe un cliente con el CI " + ci);
        }
        Cliente cliente = new Cliente(ci, solicitud.nombres().trim(), solicitud.apellidos().trim(),
                solicitud.telefono(), solicitud.correo(), solicitud.direccion());
        return ClienteResponse.desde(clienteRepository.save(cliente));
    }
}
