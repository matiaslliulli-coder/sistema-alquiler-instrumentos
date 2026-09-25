package com.unifranz.sistemainventarioinstrumentos.application.service;

import com.unifranz.sistemainventarioinstrumentos.application.dto.CatalogoResponse.CategoriaItem;
import com.unifranz.sistemainventarioinstrumentos.application.dto.CatalogoResponse.CiudadItem;
import com.unifranz.sistemainventarioinstrumentos.application.dto.CatalogoResponse.MarcaItem;
import com.unifranz.sistemainventarioinstrumentos.application.dto.CatalogoResponse.SubcategoriaItem;
import com.unifranz.sistemainventarioinstrumentos.domain.Categoria;
import com.unifranz.sistemainventarioinstrumentos.domain.Subcategoria;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.CategoriaRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.CiudadRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.MarcaRepository;
import com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence.SubcategoriaRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Listas de apoyo para los formularios (listas dependientes categoria -> subcategoria). */
@Service
public class CatalogoService {

    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final MarcaRepository marcaRepository;
    private final CiudadRepository ciudadRepository;

    public CatalogoService(CategoriaRepository categoriaRepository, SubcategoriaRepository subcategoriaRepository,
                           MarcaRepository marcaRepository, CiudadRepository ciudadRepository) {
        this.categoriaRepository = categoriaRepository;
        this.subcategoriaRepository = subcategoriaRepository;
        this.marcaRepository = marcaRepository;
        this.ciudadRepository = ciudadRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaItem> categoriasConSubcategorias() {
        Map<Integer, List<SubcategoriaItem>> porCategoria = new LinkedHashMap<>();
        for (Categoria c : categoriaRepository.findAll(Sort.by("nombreCategoria"))) {
            porCategoria.put(c.getCodCategoria(), new ArrayList<>());
        }
        for (Subcategoria s : subcategoriaRepository.findAllConCategoria()) {
            porCategoria.get(s.getCategoria().getCodCategoria())
                    .add(new SubcategoriaItem(s.getCodSubcategoria(), s.getNombreSubcategoria()));
        }
        List<CategoriaItem> resultado = new ArrayList<>();
        for (Categoria c : categoriaRepository.findAll(Sort.by("nombreCategoria"))) {
            resultado.add(new CategoriaItem(c.getCodCategoria(), c.getNombreCategoria(),
                    porCategoria.get(c.getCodCategoria())));
        }
        return resultado;
    }

    @Transactional(readOnly = true)
    public List<MarcaItem> marcas() {
        return marcaRepository.findAll(Sort.by("nombreMarca")).stream()
                .map(m -> new MarcaItem(m.getCodMarca(), m.getNombreMarca()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CiudadItem> ciudades() {
        return ciudadRepository.findAll(Sort.by("nombreCiudad")).stream()
                .map(c -> new CiudadItem(c.getCodCiudad(), c.getNombreCiudad()))
                .toList();
    }
}
