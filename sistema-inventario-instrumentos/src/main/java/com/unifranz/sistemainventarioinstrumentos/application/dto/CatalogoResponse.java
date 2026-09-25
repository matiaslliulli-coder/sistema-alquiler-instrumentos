package com.unifranz.sistemainventarioinstrumentos.application.dto;

import java.util.List;

/** Listas de apoyo (categorias con sus subcategorias, marcas, ciudades) para los formularios. */
public final class CatalogoResponse {

    private CatalogoResponse() {
    }

    public record SubcategoriaItem(Integer codSubcategoria, String nombreSubcategoria) {
    }

    public record CategoriaItem(Integer codCategoria, String nombreCategoria, List<SubcategoriaItem> subcategorias) {
    }

    public record MarcaItem(Integer codMarca, String nombreMarca) {
    }

    public record CiudadItem(Integer codCiudad, String nombreCiudad) {
    }
}
