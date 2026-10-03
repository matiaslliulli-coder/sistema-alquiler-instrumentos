package com.unifranz.sistemaseguridadinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaseguridadinstrumentos.domain.TipoUsuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Integer> {

    Optional<TipoUsuario> findByNombreTipo(String nombreTipo);
}
