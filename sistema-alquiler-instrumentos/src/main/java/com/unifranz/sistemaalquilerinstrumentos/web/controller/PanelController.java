package com.unifranz.sistemaalquilerinstrumentos.web.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rutas de demostracion del control de acceso por rol (RBAC).
 * El frontend las usa para probar que cada rol solo entra a su panel.
 */
@RestController
@RequestMapping("/api")
public class PanelController {

    @GetMapping("/admin/panel")
    public Map<String, Object> panelAdministrador(Authentication autenticacion) {
        return respuesta("Panel de ADMINISTRADOR", autenticacion);
    }

    @GetMapping("/encargado/panel")
    public Map<String, Object> panelEncargado(Authentication autenticacion) {
        return respuesta("Panel de ENCARGADO DE TIENDA", autenticacion);
    }

    @GetMapping("/cliente/panel")
    public Map<String, Object> panelCliente(Authentication autenticacion) {
        return respuesta("Panel de CLIENTE", autenticacion);
    }

    private Map<String, Object> respuesta(String panel, Authentication autenticacion) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("panel", panel);
        cuerpo.put("usuario", autenticacion.getName());
        cuerpo.put("roles", autenticacion.getAuthorities().stream().map(a -> a.getAuthority()).toList());
        return cuerpo;
    }
}
