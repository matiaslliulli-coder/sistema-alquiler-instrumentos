package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.Penalidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenalidadRepository extends JpaRepository<Penalidad, Integer> {

    List<Penalidad> findByDevolucionCodDevolucion(Integer codDevolucion);
}
