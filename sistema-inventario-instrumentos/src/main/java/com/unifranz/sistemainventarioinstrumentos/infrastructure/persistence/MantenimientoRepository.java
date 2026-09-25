package com.unifranz.sistemainventarioinstrumentos.infrastructure.persistence;

import com.unifranz.sistemainventarioinstrumentos.domain.Mantenimiento;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

    @EntityGraph(attributePaths = {"instrumento"})
    List<Mantenimiento> findAllByOrderByFechaIngresoDescCodMantenimientoDesc();

    @EntityGraph(attributePaths = {"instrumento"})
    List<Mantenimiento> findByEstadoMantenimientoOrderByFechaIngresoDescCodMantenimientoDesc(String estadoMantenimiento);
}
