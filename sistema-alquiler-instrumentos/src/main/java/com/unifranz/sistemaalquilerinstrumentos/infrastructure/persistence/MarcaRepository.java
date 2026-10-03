package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
}
