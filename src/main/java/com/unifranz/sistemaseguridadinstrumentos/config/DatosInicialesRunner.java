package com.unifranz.sistemaseguridadinstrumentos.config;

import com.unifranz.sistemaseguridadinstrumentos.domain.TipoUsuario;
import com.unifranz.sistemaseguridadinstrumentos.domain.Usuario;
import com.unifranz.sistemaseguridadinstrumentos.infrastructure.persistence.TipoUsuarioRepository;
import com.unifranz.sistemaseguridadinstrumentos.infrastructure.persistence.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea los usuarios de DEMOSTRACION la primera vez que arranca (tabla usuarios vacia).
 * Las contrasenas se guardan con BCrypt. Cambialas antes de desplegar.
 *   admin / Admin123*   encargado1 / Encargado123*   cliente1 / Cliente123*
 */
@Component
public class DatosInicialesRunner implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DatosInicialesRunner(UsuarioRepository usuarioRepository, TipoUsuarioRepository tipoUsuarioRepository,
                                PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.count() > 0) {
            return;
        }
        crear(TipoUsuario.ADMINISTRADOR, "admin", "admin@instrumentos.test", "Admin123*", "Administrador", "General", null);
        crear(TipoUsuario.ENCARGADO, "encargado1", "encargado1@instrumentos.test", "Encargado123*", "Encargado", "Tienda Uno", 1);
        crear(TipoUsuario.CLIENTE, "cliente1", "cliente1@instrumentos.test", "Cliente123*", "Cliente", "Demostracion", null);
    }

    private void crear(String tipo, String usuario, String correo, String clave, String nombres, String apellidos,
                       Integer codTienda) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findByNombreTipo(tipo)
                .orElseThrow(() -> new IllegalStateException("Falta el tipo de usuario " + tipo + " (revisa data.sql)"));
        usuarioRepository.save(new Usuario(tipoUsuario, usuario, correo, passwordEncoder.encode(clave),
                nombres, apellidos, codTienda));
    }
}
