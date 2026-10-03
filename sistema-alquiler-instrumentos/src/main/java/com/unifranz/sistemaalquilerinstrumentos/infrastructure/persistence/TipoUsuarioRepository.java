package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.TipoUsuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Integer> {

    Optional<TipoUsuario> findByNombreTipo(String nombreTipo);
}
