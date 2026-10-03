package com.unifranz.sistemaalquilerinstrumentos.application.service;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.AuditoriaResponse;
import com.unifranz.sistemaalquilerinstrumentos.domain.BitacoraAuditoria;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.BitacoraRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Bitacora de auditoria: solo se pueden agregar y consultar eventos (inmutable). */
@Service
public class AuditoriaService {

    public static final String LOGIN_OK = "LOGIN_OK";
    public static final String LOGIN_FALLIDO = "LOGIN_FALLIDO";
    public static final String ACCESO_DENEGADO = "ACCESO_DENEGADO";
    public static final String CIFRADO_PRUEBA = "CIFRADO_PRUEBA";

    private final BitacoraRepository bitacoraRepository;

    public AuditoriaService(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    @Transactional
    public void registrar(Integer idUsuario, String nombreUsuario, String accion, String entidad,
                          String detalle, String ipOrigen) {
        bitacoraRepository.save(new BitacoraAuditoria(idUsuario, nombreUsuario, accion, entidad, detalle, ipOrigen));
    }

    @Transactional(readOnly = true)
    public List<AuditoriaResponse> listar(String accion) {
        List<BitacoraAuditoria> eventos = (accion == null || accion.isBlank())
                ? bitacoraRepository.findTop100ByOrderByFechaDesc()
                : bitacoraRepository.findTop100ByAccionOrderByFechaDesc(accion.trim().toUpperCase());
        return eventos.stream().map(AuditoriaResponse::desde).toList();
    }
}
