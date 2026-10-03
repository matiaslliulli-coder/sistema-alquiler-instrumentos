package com.unifranz.sistemaalquilerinstrumentos.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.unifranz.sistemaalquilerinstrumentos.domain.TipoUsuario;
import com.unifranz.sistemaalquilerinstrumentos.domain.Usuario;
import com.unifranz.sistemaalquilerinstrumentos.infrastructure.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRETO = "una-clave-de-prueba-con-mas-de-32-caracteres-123";

    private Usuario encargado() {
        TipoUsuario tipo = new TipoUsuario(TipoUsuario.ENCARGADO, "Encargado de tienda");
        return new Usuario(tipo, "encargado1", "e@instrumentos.test", "hash", "Encargado", "Uno", 1);
    }

    @Test
    void tokenGenerado_sePuedeLeerYTraeUsuarioYRol() {
        JwtService jwt = new JwtService(SECRETO, 60);

        Claims claims = jwt.leer(jwt.generar(encargado()));

        assertThat(claims.getSubject()).isEqualTo("encargado1");
        assertThat(claims.get("rol", String.class)).isEqualTo("ENCARGADO");
    }

    @Test
    void tokenFirmadoConOtraClave_esRechazado() {
        String token = new JwtService(SECRETO, 60).generar(encargado());
        JwtService otro = new JwtService("otra-clave-distinta-con-mas-de-32-caracteres-999", 60);

        assertThatThrownBy(() -> otro.leer(token)).isInstanceOf(JwtException.class);
    }

    @Test
    void tokenVencido_esRechazado() {
        JwtService jwt = new JwtService(SECRETO, -1);

        assertThatThrownBy(() -> jwt.leer(jwt.generar(encargado()))).isInstanceOf(JwtException.class);
    }

    @Test
    void secretoCorto_noSePermite() {
        assertThatThrownBy(() -> new JwtService("corto", 60)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void expiracionEnSegundos() {
        assertThat(new JwtService(SECRETO, 30).expiracionSegundos()).isEqualTo(1800L);
    }
}
