package com.unifranz.sistemainventarioinstrumentos.domain;

/** Estado de disponibilidad de un instrumento. En la base se guarda el texto de valorBd. */
public enum EstadoInstrumento {
    DISPONIBLE("DISPONIBLE"),
    ALQUILADO("ALQUILADO"),
    EN_MANTENIMIENTO("EN MANTENIMIENTO");

    private final String valorBd;

    EstadoInstrumento(String valorBd) {
        this.valorBd = valorBd;
    }

    public String getValorBd() {
        return valorBd;
    }

    public static EstadoInstrumento desdeValorBd(String valor) {
        if (valor != null) {
            for (EstadoInstrumento e : values()) {
                if (e.valorBd.equalsIgnoreCase(valor.trim())) {
                    return e;
                }
            }
        }
        throw new IllegalArgumentException("Estado de instrumento desconocido: " + valor);
    }
}
