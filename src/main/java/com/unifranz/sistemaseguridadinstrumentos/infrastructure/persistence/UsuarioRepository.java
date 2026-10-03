package com.unifranz.sistemaseguridadinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaseguridadinstrumentos.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
