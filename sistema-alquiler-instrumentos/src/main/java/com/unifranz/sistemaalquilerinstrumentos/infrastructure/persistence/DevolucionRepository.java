package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.Devolucion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevolucionRepository extends JpaRepository<Devolucion, Integer> {

    Optional<Devolucion> findByAlquilerCodAlquiler(Integer codAlquiler);
}
