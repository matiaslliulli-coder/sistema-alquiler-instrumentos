package com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence;

import com.unifranz.sistemaalquilerinstrumentos.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}
