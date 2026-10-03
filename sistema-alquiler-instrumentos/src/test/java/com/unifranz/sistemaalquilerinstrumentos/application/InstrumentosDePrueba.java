package com.unifranz.sistemaalquilerinstrumentos.application;

import com.unifranz.sistemaalquilerinstrumentos.domain.Categoria;
import com.unifranz.sistemaalquilerinstrumentos.domain.Ciudad;
import com.unifranz.sistemaalquilerinstrumentos.domain.Instrumento;
import com.unifranz.sistemaalquilerinstrumentos.domain.Marca;
import com.unifranz.sistemaalquilerinstrumentos.domain.Subcategoria;
import com.unifranz.sistemaalquilerinstrumentos.domain.Tienda;
import java.math.BigDecimal;

/** Crea instrumentos reales (DISPONIBLES) para las pruebas unitarias. */
final class InstrumentosDePrueba {

    private InstrumentosDePrueba() {
    }

    static Instrumento guitarra(String codigo, String precioDia) {
        Tienda tienda = new Tienda("Tienda Centro", new Ciudad("La Paz"), "Av. Camacho 1234", "22001111",
                new BigDecimal("-16.495800"), new BigDecimal("-68.133500"), "Encargado");
        return new Instrumento(codigo, "Guitarra acustica", new Subcategoria(new Categoria("Cuerdas"), "Guitarra"),
                new Marca("Yamaha"), "C40", tienda, new BigDecimal(precioDia));
    }
}
