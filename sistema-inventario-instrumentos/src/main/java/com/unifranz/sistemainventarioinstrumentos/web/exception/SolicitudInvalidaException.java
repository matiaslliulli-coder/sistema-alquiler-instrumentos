package com.unifranz.sistemainventarioinstrumentos.web.exception;

/** Datos de entrada incorrectos o incompletos (HTTP 400). */
public class SolicitudInvalidaException extends RuntimeException {

    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}
