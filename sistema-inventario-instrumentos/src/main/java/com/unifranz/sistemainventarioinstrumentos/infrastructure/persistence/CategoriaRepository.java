package com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence;

import com.unifranz.sistemainventarioinstrumentos.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
