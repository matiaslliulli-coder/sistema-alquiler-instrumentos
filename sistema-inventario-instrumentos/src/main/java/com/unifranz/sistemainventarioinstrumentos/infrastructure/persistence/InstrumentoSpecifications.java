package com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence;

import com.unifranz.sistemainventarioinstrumentos.domain.Ciudad;
import com.unifranz.sistemainventarioinstrumentos.domain.EstadoInstrumento;
import com.unifranz.sistemainventarioinstrumentos.domain.Instrumento;
import com.unifranz.sistemainventarioinstrumentos.domain.Subcategoria;
import com.unifranz.sistemainventarioinstrumentos.domain.Tienda;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/** Filtros dinamicos para consultar instrumentos. Solo se aplican los filtros que llegan con valor. */
public final class InstrumentoSpecifications {

    private InstrumentoSpecifications() {
    }

    /** Todos los instrumentos activos (cualquier estado de disponibilidad) en tiendas activas. */
    public static Specification<Instrumento> activos() {
        return (root, query, cb) -> {
            Join<Instrumento, Tienda> tienda = root.join("tienda");
            return cb.and(
                    cb.equal(root.get("estado"), Instrumento.ACTIVO),
                    cb.equal(tienda.get("estado"), Tienda.ACTIVA));
        };
    }

    /**
     * Instrumentos DISPONIBLES (los unicos que aparecen en el catalogo), con filtros opcionales:
     * tipo (subcategoria), categoria, ciudad y nombre del instrumento. Coincidencia parcial sin distinguir mayusculas.
     */
    public static Specification<Instrumento> disponibles(String tipo, String categoria, String ciudad, String nombre) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();
            Join<Instrumento, Tienda> tienda = root.join("tienda");

            filtros.add(cb.equal(root.get("estadoInstrumento"), EstadoInstrumento.DISPONIBLE));
            filtros.add(cb.equal(root.get("estado"), Instrumento.ACTIVO));
            filtros.add(cb.equal(tienda.get("estado"), Tienda.ACTIVA));

            if (tieneTexto(ciudad)) {
                Join<Tienda, Ciudad> ciu = tienda.join("ciudad");
                filtros.add(cb.like(cb.lower(ciu.<String>get("nombreCiudad")), patron(ciudad)));
            }
            if (tieneTexto(tipo) || tieneTexto(categoria)) {
                Join<Instrumento, Subcategoria> sub = root.join("subcategoria");
                if (tieneTexto(tipo)) {
                    filtros.add(cb.like(cb.lower(sub.<String>get("nombreSubcategoria")), patron(tipo)));
                }
                if (tieneTexto(categoria)) {
                    filtros.add(cb.like(cb.lower(sub.join("categoria").<String>get("nombreCategoria")), patron(categoria)));
                }
            }
            if (tieneTexto(nombre)) {
                filtros.add(cb.like(cb.lower(root.<String>get("nombreInstrumento")), patron(nombre)));
            }
            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }

    /** Mismo instrumento en distintas tiendas: mismo nombre y, si se indican, misma marca y modelo (exacto). */
    public static Specification<Instrumento> mismoInstrumento(String nombre, String marca, String modelo) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();
            filtros.add(cb.equal(cb.lower(root.<String>get("nombreInstrumento")), nombre.trim().toLowerCase()));
            if (tieneTexto(marca)) {
                filtros.add(cb.equal(cb.lower(root.join("marca").<String>get("nombreMarca")), marca.trim().toLowerCase()));
            }
            if (tieneTexto(modelo)) {
                filtros.add(cb.equal(cb.lower(root.<String>get("modeloInstrumento")), modelo.trim().toLowerCase()));
            }
            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }

    private static boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private static String patron(String valor) {
        return "%" + valor.trim().toLowerCase() + "%";
    }
}
