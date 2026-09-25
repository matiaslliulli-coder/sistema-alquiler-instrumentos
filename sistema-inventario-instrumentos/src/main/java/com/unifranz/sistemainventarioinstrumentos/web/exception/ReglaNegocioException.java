package com.unifranz.sistemainventarioinstrumentos.web.exception;

/** La operacion contradice una regla del negocio, por ejemplo el estado actual del instrumento (HTTP 409). */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
