package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.Alquiler;
import com.unifranz.sistemaalquilerinstrumentos.domain.EstadoAlquiler;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlquilerRepository extends JpaRepository<Alquiler, Integer> {

    List<Alquiler> findByInstrumentoCodInstrumentoOrderByFechaAlquilerDesc(String codInstrumento);

    List<Alquiler> findByClienteCodClienteOrderByFechaAlquilerDesc(Integer codCliente);

    List<Alquiler> findByEstadoAlquiler(EstadoAlquiler estadoAlquiler);

    List<Alquiler> findByEstadoAlquilerAndFechaDevolucionPrevistaBefore(EstadoAlquiler estadoAlquiler, LocalDate fecha);
}
