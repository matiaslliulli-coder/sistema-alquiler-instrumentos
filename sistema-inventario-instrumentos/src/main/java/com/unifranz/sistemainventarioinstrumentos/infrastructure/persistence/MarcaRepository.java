package com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence;

import com.unifranz.sistemainventarioinstrumentos.domain.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<Marca, Integer> {
}
