package com.unifranz.sistemainventarioinstrumentos.web.controller;

import com.unifranz.sistemainventarioinstrumentos.application.dto.CatalogoResponse.CategoriaItem;
import com.unifranz.sistemainventarioinstrumentos.application.dto.CatalogoResponse.CiudadItem;
import com.unifranz.sistemainventarioinstrumentos.application.dto.CatalogoResponse.MarcaItem;
import com.unifranz.sistemainventarioinstrumentos.application.service.CatalogoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Listas de apoyo para los formularios y para consultar los codigos que piden los otros endpoints. */
@RestController
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/api/categorias")
    public List<CategoriaItem> categorias() {
        return catalogoService.categoriasConSubcategorias();
    }

    @GetMapping("/api/marcas")
    public List<MarcaItem> marcas() {
        return catalogoService.marcas();
    }

    @GetMapping("/api/ciudades")
    public List<CiudadItem> ciudades() {
        return catalogoService.ciudades();
    }
}
