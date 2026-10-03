package com.unifranz.sistemaalquilerinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/** Intento fallido de inicio de sesion (base para las alertas de la semana 5). */
@Entity
@Table(name = "intentos_fallidos")
public class IntentoFallido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_intento")
    private Integer idIntento;

    @Column(name = "nombre_usuario", nullable = false, length = 50)
    private String nombreUsuario;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "motivo", length = 100)
    private String motivo;

    @Column(name = "fecha_intento", nullable = false)
    private LocalDateTime fechaIntento = LocalDateTime.now();

    protected IntentoFallido() {
    }

    public IntentoFallido(String nombreUsuario, String ipOrigen, String motivo) {
        this.nombreUsuario = nombreUsuario;
        this.ipOrigen = ipOrigen;
        this.motivo = motivo;
    }

    public Integer getIdIntento() { return idIntento; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getIpOrigen() { return ipOrigen; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getFechaIntento() { return fechaIntento; }
}
