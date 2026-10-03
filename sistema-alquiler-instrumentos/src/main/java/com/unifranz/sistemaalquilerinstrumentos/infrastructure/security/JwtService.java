package com.unifranz.sistemaalquilerinstrumentos.infrastructure.security;

import com.unifranz.sistemaalquilerinstrumentos.application.port.GeneradorToken;
import com.unifranz.sistemaalquilerinstrumentos.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Adaptador JWT: firma (HS256) y valida los tokens de sesion. */
@Component
public class JwtService implements GeneradorToken {

    private final SecretKey clave;
    private final long minutosExpiracion;

    public JwtService(@Value("${app.jwt.secreto}") String secreto,
                      @Value("${app.jwt.expiracion-minutos:60}") long minutosExpiracion) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalArgumentException("app.jwt.secreto debe tener al menos 32 caracteres");
        }
        this.clave = Keys.hmacShaKeyFor(bytes);
        this.minutosExpiracion = minutosExpiracion;
    }

    @Override
    public String generar(Usuario usuario) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(usuario.getNombreUsuario())
                .claim("rol", usuario.getTipoUsuario().getNombreTipo())
                .claim("uid", usuario.getIdUsuario())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(minutosExpiracion, ChronoUnit.MINUTES)))
                .signWith(clave)
                .compact();
    }

    @Override
    public long expiracionSegundos() {
        return minutosExpiracion * 60;
    }

    /** Valida la firma y la expiracion; lanza JwtException si el token no es valido. */
    public Claims leer(String token) {
        return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
    }
}
