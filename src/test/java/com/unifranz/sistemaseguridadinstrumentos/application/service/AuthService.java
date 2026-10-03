package com.unifranz.sistemaseguridadinstrumentos.application.service;

import com.unifranz.sistemaseguridadinstrumentos.application.dto.LoginRequest;
import com.unifranz.sistemaseguridadinstrumentos.application.dto.LoginResponse;
import com.unifranz.sistemaseguridadinstrumentos.application.dto.PerfilResponse;
import com.unifranz.sistemaseguridadinstrumentos.application.port.GeneradorToken;
import com.unifranz.sistemaseguridadinstrumentos.domain.IntentoFallido;
import com.unifranz.sistemaseguridadinstrumentos.domain.Permiso;
import com.unifranz.sistemaseguridadinstrumentos.domain.Usuario;
import com.unifranz.sistemaseguridadinstrumentos.infrastructure.persistence.IntentoFallidoRepository;
import com.unifranz.sistemaseguridadinstrumentos.infrastructure.persistence.UsuarioRepository;
import com.unifranz.sistemaseguridadinstrumentos.web.exception.CredencialesInvalidasException;
import com.unifranz.sistemaseguridadinstrumentos.web.exception.RecursoNoEncontradoException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Inicio de sesion. No es @Transactional a proposito: los intentos fallidos y la bitacora
 * deben guardarse aunque despues se lance la excepcion de credenciales invalidas.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final IntentoFallidoRepository intentoRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeneradorToken generadorToken;
    private final AuditoriaService auditoriaService;

    public AuthService(UsuarioRepository usuarioRepository, IntentoFallidoRepository intentoRepository,
                       PasswordEncoder passwordEncoder, GeneradorToken generadorToken,
                       AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.intentoRepository = intentoRepository;
        this.passwordEncoder = passwordEncoder;
        this.generadorToken = generadorToken;
        this.auditoriaService = auditoriaService;
    }

    public LoginResponse login(LoginRequest solicitud, String ipOrigen) {
        String nombre = solicitud.nombreUsuario().trim();
        Optional<Usuario> encontrado = usuarioRepository.findByNombreUsuario(nombre);

        if (encontrado.isEmpty()
                || !encontrado.get().isActivo()
                || !passwordEncoder.matches(solicitud.password(), encontrado.get().getPasswordHash())) {
            intentoRepository.save(new IntentoFallido(nombre, ipOrigen, "Credenciales invalidas"));
            auditoriaService.registrar(null, nombre, AuditoriaService.LOGIN_FALLIDO, "usuarios",
                    "Intento de inicio de sesion fallido", ipOrigen);
            throw new CredencialesInvalidasException();
        }

        Usuario usuario = encontrado.get();
        String token = generadorToken.generar(usuario);
        auditoriaService.registrar(usuario.getIdUsuario(), usuario.getNombreUsuario(), AuditoriaService.LOGIN_OK,
                "usuarios", "Inicio de sesion correcto", ipOrigen);

        List<String> permisos = usuario.getTipoUsuario().getPermisos().stream()
                .map(Permiso::getNombrePermiso).sorted().toList();
        return new LoginResponse(token, "Bearer", generadorToken.expiracionSegundos(), usuario.getNombreUsuario(),
                usuario.getNombreCompleto(), usuario.getTipoUsuario().getNombreTipo(), permisos);
    }

    public PerfilResponse perfil(String nombreUsuario) {
        return PerfilResponse.desde(usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario " + nombreUsuario)));
    }
}
