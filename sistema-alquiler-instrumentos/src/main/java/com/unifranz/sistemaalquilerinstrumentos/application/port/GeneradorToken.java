package com.unifranz.sistemaalquilerinstrumentos.application.port;

import com.unifranz.sistemaalquilerinstrumentos.domain.Usuario;

/** Puerto de salida: emite el token de sesion. Hoy lo implementa JwtService (adaptador JWT). */
public interface GeneradorToken {

    String generar(Usuario usuario);

    long expiracionSegundos();
}
