package com.unifranz.sistemaalquilerinstrumentos.application.service;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.TarifaPenalidadResponse;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.TarifaPenalidadRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TarifaPenalidadService {

    private final TarifaPenalidadRepository tarifaRepository;

    public TarifaPenalidadService(TarifaPenalidadRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    public List<TarifaPenalidadResponse> listar() {
        return tarifaRepository.findAll().stream().map(TarifaPenalidadResponse::desde).toList();
    }
}
