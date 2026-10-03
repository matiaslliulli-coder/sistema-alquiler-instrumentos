package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.Mantenimiento;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

    @EntityGraph(attributePaths = {"instrumento"})
    List<Mantenimiento> findAllByOrderByFechaIngresoDescCodMantenimientoDesc();

    @EntityGraph(attributePaths = {"instrumento"})
    List<Mantenimiento> findByEstadoMantenimientoOrderByFechaIngresoDescCodMantenimientoDesc(String estadoMantenimiento);
}
