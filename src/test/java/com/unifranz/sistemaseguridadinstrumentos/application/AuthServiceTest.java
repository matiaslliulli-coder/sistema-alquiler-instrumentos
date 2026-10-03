package com.unifranz.sistemaseguridadinstrumentos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.unifranz.sistemaseguridadinstrumentos.application.dto.LoginRequest;
import com.unifranz.sistemaseguridadinstrumentos.application.dto.LoginResponse;
import com.unifranz.sistemaseguridadinstrumentos.application.port.GeneradorToken;
import com.unifranz.sistemaseguridadinstrumentos.application.service.AuditoriaService;
import com.unifranz.sistemaseguridadinstrumentos.application.service.AuthService;
import com.unifranz.sistemaseguridadinstrumentos.domain.IntentoFallido;
import com.unifranz.sistemaseguridadinstrumentos.domain.Permiso;
import com.unifranz.sistemaseguridadinstrumentos.domain.TipoUsuario;
import com.unifranz.sistemaseguridadinstrumentos.domain.Usuario;
import com.unifranz.sistemaseguridadinstrumentos.infrastructure.persistence.IntentoFallidoRepository;
import com.unifranz.sistemaseguridadinstrumentos.infrastructure.persistence.UsuarioRepository;
import com.unifranz.sistemaseguridadinstrumentos.web.exception.CredencialesInvalidasException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private IntentoFallidoRepository intentoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private GeneradorToken generadorToken;
    @Mock
    private AuditoriaService auditoriaService;

    private AuthService servicio;
    private Usuario admin;

    @BeforeEach
    void preparar() {
        servicio = new AuthService(usuarioRepository, intentoRepository, passwordEncoder, generadorToken, auditoriaService);
        TipoUsuario tipo = new TipoUsuario(TipoUsuario.ADMINISTRADOR, "Administrador del sistema");
        tipo.agregarPermiso(new Permiso("USUARIOS_GESTIONAR", "Gestionar usuarios"));
        tipo.agregarPermiso(new Permiso("AUDITORIA_CONSULTAR", "Ver bitacora"));
        admin = new Usuario(tipo, "admin", "admin@instrumentos.test", "hash-bcrypt", "Administrador", "General", null);
    }

    @Test
    void loginCorrecto_devuelveTokenRolYPermisosOrdenados_yRegistraLaBitacora() {
        when(usuarioRepository.findByNombreUsuario("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("Admin123*", "hash-bcrypt")).thenReturn(true);
        when(generadorToken.generar(admin)).thenReturn("token-de-prueba");
        when(generadorToken.expiracionSegundos()).thenReturn(3600L);

        LoginResponse respuesta = servicio.login(new LoginRequest("admin", "Admin123*"), "127.0.0.1");

        assertThat(respuesta.token()).isEqualTo("token-de-prueba");
        assertThat(respuesta.tipoToken()).isEqualTo("Bearer");
        assertThat(respuesta.rol()).isEqualTo("ADMINISTRADOR");
        assertThat(respuesta.expiraEnSegundos()).isEqualTo(3600L);
        assertThat(respuesta.permisos()).containsExactly("AUDITORIA_CONSULTAR", "USUARIOS_GESTIONAR");
        verify(auditoriaService).registrar(any(), eq("admin"), eq(AuditoriaService.LOGIN_OK), any(), any(), eq("127.0.0.1"));
    }

    @Test
    void contrasenaIncorrecta_registraIntentoFallidoYNoEmiteToken() {
        when(usuarioRepository.findByNombreUsuario("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("mala", "hash-bcrypt")).thenReturn(false);

        assertThatThrownBy(() -> servicio.login(new LoginRequest("admin", "mala"), "10.0.0.5"))
                .isInstanceOf(CredencialesInvalidasException.class);

        verify(intentoRepository).save(any(IntentoFallido.class));
        verify(auditoriaService).registrar(any(), eq("admin"), eq(AuditoriaService.LOGIN_FALLIDO), any(), any(), eq("10.0.0.5"));
        verify(generadorToken, never()).generar(any(Usuario.class));
    }

    @Test
    void usuarioInexistente_dalaMismaRespuestaQueContrasenaIncorrecta() {
        when(usuarioRepository.findByNombreUsuario("fantasma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.login(new LoginRequest("fantasma", "x"), "10.0.0.5"))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessage("Usuario o contrasena incorrectos");

        verify(intentoRepository).save(any(IntentoFallido.class));
    }
}
