package com.unifranz.sistemaalquilerinstrumentos.infrastructure.security;

import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/** Reglas de acceso por rol (RBAC), sesion sin estado (JWT), BCrypt y CORS para el frontend. */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        // Publico: comprobacion, login y consulta del catalogo (lo usa el cliente sin iniciar sesion)
                        .requestMatchers("/api/ping", "/api/auth/login").permitAll()
                        // Datos internos del alquiler: solo personal de la tienda
                        .requestMatchers(HttpMethod.GET, "/api/instrumentos/*/historial").hasAnyRole("ADMINISTRADOR", "ENCARGADO")
                        .requestMatchers(HttpMethod.GET, "/api/instrumentos/**", "/api/tiendas/**", "/api/categorias/**",
                                "/api/marcas/**", "/api/ciudades/**").permitAll()
                        // Gestion del inventario, mantenimiento, clientes y alquileres: Encargado o Administrador
                        .requestMatchers("/api/instrumentos/**", "/api/mantenimientos/**", "/api/alquileres/**",
                                "/api/clientes/**", "/api/tarifas-penalidad/**", "/api/encargado/**")
                                .hasAnyRole("ADMINISTRADOR", "ENCARGADO")
                        // Alta de tiendas, usuarios, auditoria y cifrado: solo Administrador
                        .requestMatchers("/api/tiendas/**", "/api/admin/**", "/api/auditoria/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/cliente/**").hasRole("CLIENTE")
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((request, response, ex) ->
                                escribirError(response, HttpStatus.UNAUTHORIZED, "Debes iniciar sesion (token ausente, invalido o vencido)"))
                        .accessDeniedHandler((request, response, ex) ->
                                escribirError(response, HttpStatus.FORBIDDEN, "Tu rol no tiene permiso para esta operacion")))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracion.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/api/**", configuracion);
        return fuente;
    }

    private static void escribirError(jakarta.servlet.http.HttpServletResponse response, HttpStatus estado, String mensaje)
            throws java.io.IOException {
        response.setStatus(estado.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.getWriter().write("{\"status\":" + estado.value() + ",\"error\":\"" + estado.getReasonPhrase()
                + "\",\"mensaje\":\"" + mensaje + "\"}");
    }
}
