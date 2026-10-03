package com.unifranz.sistemaseguridadinstrumentos.application.port;

import com.unifranz.sistemaseguridadinstrumentos.domain.Usuario;

/** Puerto de salida: emite el token de sesion. Hoy lo implementa JwtService (adaptador JWT). */
public interface GeneradorToken {

    String generar(Usuario usuario);

    long expiracionSegundos();
}
