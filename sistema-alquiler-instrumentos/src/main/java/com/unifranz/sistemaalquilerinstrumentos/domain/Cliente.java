package com.unifranz.sistemaalquilerinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

/** Persona que alquila instrumentos. */
@Entity
@Table(name = "clientes")
public class Cliente {

    public static final String ACTIVO = "A";
    public static final String INACTIVO = "I";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_cliente")
    private Integer codCliente;

    @Column(name = "ci_cliente", nullable = false, length = 15, unique = true)
    private String ciCliente;

    @Column(name = "nombres", nullable = false, length = 80)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 80)
    private String apellidos;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "identidad_verificada", nullable = false)
    private boolean identidadVerificada = false;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro = LocalDate.now();

    @Column(name = "estado", nullable = false, length = 1)
    private String estado = ACTIVO;

    protected Cliente() {
    }

    public Cliente(String ciCliente, String nombres, String apellidos, String telefono, String correo, String direccion) {
        this.ciCliente = ciCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
    }

    public boolean estaActivo() {
        return ACTIVO.equals(estado);
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public Integer getCodCliente() { return codCliente; }
    public String getCiCliente() { return ciCliente; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public boolean isIdentidadVerificada() { return identidadVerificada; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public String getEstado() { return estado; }
}
