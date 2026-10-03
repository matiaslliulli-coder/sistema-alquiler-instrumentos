package com.unifranz.sistemaalquilerinstrumentos.config;

import com.unifranz.sistemaalquilerinstrumentos.domain.Tienda;
import com.unifranz.sistemaalquilerinstrumentos.domain.TipoUsuario;
import com.unifranz.sistemaalquilerinstrumentos.domain.Usuario;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.TiendaRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.TipoUsuarioRepository;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.persistence.UsuarioRepository;
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
    private final TiendaRepository tiendaRepository;
    private final PasswordEncoder passwordEncoder;

    public DatosInicialesRunner(UsuarioRepository usuarioRepository, TipoUsuarioRepository tipoUsuarioRepository,
                                TiendaRepository tiendaRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.tiendaRepository = tiendaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.count() > 0) {
            return;
        }
        crear(TipoUsuario.ADMINISTRADOR, "admin", "admin@instrumentos.test", "Admin123*", "Administrador", "General", null);
        Integer primeraTienda = tiendaRepository.findAll().stream().map(Tienda::getCodTienda).sorted().findFirst().orElse(null);
        crear(TipoUsuario.ENCARGADO, "encargado1", "encargado1@instrumentos.test", "Encargado123*", "Encargado", "Tienda Uno", primeraTienda);
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
