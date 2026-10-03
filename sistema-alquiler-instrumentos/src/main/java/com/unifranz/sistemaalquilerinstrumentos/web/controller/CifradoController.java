package com.unifranz.sistemaalquilerinstrumentos.web.controller;

import com.unifranz.sistemaalquilerinstrumentos.application.dto.CifradoRequest;
import com.unifranz.sistemaalquilerinstrumentos.application.service.AuditoriaService;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.crypto.ServicioCifrado;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Demostracion del cifrado en reposo AES-256-GCM (solo ADMINISTRADOR). */
@RestController
@RequestMapping("/api/admin/cifrado")
public class CifradoController {

    private final ServicioCifrado servicioCifrado;
    private final AuditoriaService auditoriaService;

    public CifradoController(ServicioCifrado servicioCifrado, AuditoriaService auditoriaService) {
        this.servicioCifrado = servicioCifrado;
        this.auditoriaService = auditoriaService;
    }

    @PostMapping("/probar")
    public Map<String, String> probar(@Valid @RequestBody CifradoRequest solicitud, Authentication autenticacion,
                                      HttpServletRequest request) {
        String cifrado = servicioCifrado.cifrarTexto(solicitud.texto());
        auditoriaService.registrar(null, autenticacion.getName(), AuditoriaService.CIFRADO_PRUEBA, "cifrado",
                "Prueba de cifrado AES-256-GCM", request.getRemoteAddr());

        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("original", solicitud.texto());
        respuesta.put("cifradoBase64", cifrado);
        respuesta.put("descifrado", servicioCifrado.descifrarTexto(cifrado));
        return respuesta;
    }
}
