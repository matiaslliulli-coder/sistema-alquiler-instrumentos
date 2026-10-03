package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.Subcategoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Integer> {

    @Query("select s from Subcategoria s join fetch s.categoria c order by c.nombreCategoria, s.nombreSubcategoria")
    List<Subcategoria> findAllConCategoria();
}
