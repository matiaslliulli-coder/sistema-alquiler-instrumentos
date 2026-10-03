package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.TarifaPenalidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifaPenalidadRepository extends JpaRepository<TarifaPenalidad, Integer> {

    List<TarifaPenalidad> findByActivaTrue();
}
