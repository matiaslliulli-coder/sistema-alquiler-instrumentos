package com.unifranz.sistemaseguridadinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_permiso")
    private Integer codPermiso;

    @Column(name = "nombre_permiso", nullable = false, length = 50, unique = true)
    private String nombrePermiso;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    protected Permiso() {
    }

    public Permiso(String nombrePermiso, String descripcion) {
        this.nombrePermiso = nombrePermiso;
        this.descripcion = descripcion;
    }

    public Integer getCodPermiso() { return codPermiso; }
    public String getNombrePermiso() { return nombrePermiso; }
    public String getDescripcion() { return descripcion; }
}
