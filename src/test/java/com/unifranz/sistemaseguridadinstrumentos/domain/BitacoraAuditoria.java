package com.unifranz.sistemaseguridadinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Evento de auditoria. Es INMUTABLE: no tiene setters, las columnas no son actualizables
 * y la base de datos ignora UPDATE y DELETE (reglas en schema.sql).
 */
@Entity
@Table(name = "bitacora_auditoria")
public class BitacoraAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora", updatable = false)
    private Long idBitacora;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "id_usuario", updatable = false)
    private Integer idUsuario;

    @Column(name = "nombre_usuario", length = 50, updatable = false)
    private String nombreUsuario;

    @Column(name = "accion", nullable = false, length = 40, updatable = false)
    private String accion;

    @Column(name = "entidad", length = 40, updatable = false)
    private String entidad;

    @Column(name = "detalle", updatable = false)
    private String detalle;

    @Column(name = "ip_origen", length = 45, updatable = false)
    private String ipOrigen;

    protected BitacoraAuditoria() {
    }

    public BitacoraAuditoria(Integer idUsuario, String nombreUsuario, String accion, String entidad,
                             String detalle, String ipOrigen) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.accion = accion;
        this.entidad = entidad;
        this.detalle = detalle;
        this.ipOrigen = ipOrigen;
    }

    public Long getIdBitacora() { return idBitacora; }
    public LocalDateTime getFecha() { return fecha; }
    public Integer getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getAccion() { return accion; }
    public String getEntidad() { return entidad; }
    public String getDetalle() { return detalle; }
    public String getIpOrigen() { return ipOrigen; }
}
