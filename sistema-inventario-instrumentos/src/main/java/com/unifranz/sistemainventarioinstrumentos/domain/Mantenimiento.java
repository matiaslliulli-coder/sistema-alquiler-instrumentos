package com.unifranz.sistemainventarioinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Ingreso de un instrumento a mantenimiento. Mientras este EN PROCESO el instrumento no sale en el catalogo. */
@Entity
@Table(name = "mantenimientos")
public class Mantenimiento {

    public static final String EN_PROCESO = "EN PROCESO";
    public static final String CERRADO = "CERRADO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_mantenimiento")
    private Integer codMantenimiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_instrumento", nullable = false)
    private Instrumento instrumento;

    /** Devolucion con danos que origino el mantenimiento (opcional; la tabla la maneja el modulo de Alquileres). */
    @Column(name = "cod_devolucion")
    private Integer codDevolucion;

    /** Encargado que registra el ingreso (opcional hasta integrar el modulo de Seguridad). */
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @Column(name = "descripcion_dano", nullable = false)
    private String descripcionDano;

    @Column(name = "trabajo_realizado")
    private String trabajoRealizado;

    @Column(name = "costo_reparacion", precision = 7, scale = 2)
    private BigDecimal costoReparacion;

    @Column(name = "estado_mantenimiento", nullable = false, length = 10)
    private String estadoMantenimiento;

    protected Mantenimiento() {
    }

    public Mantenimiento(Instrumento instrumento, String descripcionDano, Integer codDevolucion, Integer idUsuario) {
        this.instrumento = instrumento;
        this.descripcionDano = descripcionDano;
        this.codDevolucion = codDevolucion;
        this.idUsuario = idUsuario;
        this.fechaIngreso = LocalDate.now();
        this.estadoMantenimiento = EN_PROCESO;
    }

    /** Cierra el registro: guarda el trabajo realizado, el costo y la fecha de salida. */
    public void cerrar(String trabajoRealizado, BigDecimal costoReparacion) {
        this.trabajoRealizado = trabajoRealizado;
        this.costoReparacion = costoReparacion;
        this.fechaSalida = LocalDate.now();
        this.estadoMantenimiento = CERRADO;
    }

    public boolean estaCerrado() {
        return CERRADO.equals(estadoMantenimiento);
    }

    public Integer getCodMantenimiento() {
        return codMantenimiento;
    }

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public Integer getCodDevolucion() {
        return codDevolucion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public String getDescripcionDano() {
        return descripcionDano;
    }

    public String getTrabajoRealizado() {
        return trabajoRealizado;
    }

    public BigDecimal getCostoReparacion() {
        return costoReparacion;
    }

    public String getEstadoMantenimiento() {
        return estadoMantenimiento;
    }
}
