package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.IntentoFallido;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntentoFallidoRepository extends JpaRepository<IntentoFallido, Integer> {

    long countByNombreUsuarioAndFechaIntentoAfter(String nombreUsuario, LocalDateTime desde);
}
