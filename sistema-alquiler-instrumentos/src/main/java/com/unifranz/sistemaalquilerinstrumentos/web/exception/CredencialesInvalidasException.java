package com.unifranz.sistemaalquilerinstrumentos.web.exception;

/** Usuario o contrasena incorrectos (HTTP 401). El mensaje no revela cual de los dos fallo. */
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Usuario o contrasena incorrectos");
    }
}
