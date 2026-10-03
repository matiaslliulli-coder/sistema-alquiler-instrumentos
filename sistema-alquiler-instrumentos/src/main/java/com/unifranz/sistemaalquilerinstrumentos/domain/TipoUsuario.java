package com.unifranz.sistemaalquilerinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/** Rol del sistema: ADMINISTRADOR, ENCARGADO o CLIENTE, con sus permisos. */
@Entity
@Table(name = "tipos_usuario")
public class TipoUsuario {

    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String ENCARGADO = "ENCARGADO";
    public static final String CLIENTE = "CLIENTE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_tipo_usuario")
    private Integer codTipoUsuario;

    @Column(name = "nombre_tipo", nullable = false, length = 20, unique = true)
    private String nombreTipo;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tipo_usuario_permiso",
            joinColumns = @JoinColumn(name = "cod_tipo_usuario"),
            inverseJoinColumns = @JoinColumn(name = "cod_permiso"))
    private Set<Permiso> permisos = new HashSet<>();

    protected TipoUsuario() {
    }

    public TipoUsuario(String nombreTipo, String descripcion) {
        this.nombreTipo = nombreTipo;
        this.descripcion = descripcion;
    }

    public void agregarPermiso(Permiso permiso) {
        permisos.add(permiso);
    }

    public Integer getCodTipoUsuario() { return codTipoUsuario; }
    public String getNombreTipo() { return nombreTipo; }
    public String getDescripcion() { return descripcion; }
    public Set<Permiso> getPermisos() { return permisos; }
}
