package com.unifranz.sistemainventarioinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Unidad fisica de un instrumento musical del inventario centralizado. */
@Entity
@Table(name = "instrumentos")
public class Instrumento {

    public static final String ACTIVO = "A";
    public static final String INACTIVO = "I";

    @Id
    @Column(name = "cod_instrumento", length = 10)
    private String codInstrumento;

    @Column(name = "nombre_instrumento", nullable = false, length = 100)
    private String nombreInstrumento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_subcategoria", nullable = false)
    private Subcategoria subcategoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_marca", nullable = false)
    private Marca marca;

    @Column(name = "modelo_instrumento", nullable = false, length = 50)
    private String modeloInstrumento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_tienda", nullable = false)
    private Tienda tienda;

    /** Precio de alquiler por dia, en bolivianos. */
    @Column(name = "precio_alquiler", nullable = false, precision = 7, scale = 2)
    private BigDecimal precioAlquiler;

    @Convert(converter = EstadoInstrumentoConverter.class)
    @Column(name = "estado_instrumento", nullable = false, length = 20)
    private EstadoInstrumento estadoInstrumento;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado;

    protected Instrumento() {
    }

    /** Un instrumento nuevo siempre queda DISPONIBLE, activo y con la fecha de hoy. */
    public Instrumento(String codInstrumento, String nombreInstrumento, Subcategoria subcategoria, Marca marca,
                       String modeloInstrumento, Tienda tienda, BigDecimal precioAlquiler) {
        this.codInstrumento = codInstrumento;
        this.nombreInstrumento = nombreInstrumento;
        this.subcategoria = subcategoria;
        this.marca = marca;
        this.modeloInstrumento = modeloInstrumento;
        this.tienda = tienda;
        this.precioAlquiler = precioAlquiler;
        this.estadoInstrumento = EstadoInstrumento.DISPONIBLE;
        this.fechaRegistro = LocalDate.now();
        this.estado = ACTIVO;
    }

    public boolean estaActivo() {
        return ACTIVO.equals(estado);
    }

    public String getCodInstrumento() {
        return codInstrumento;
    }

    public String getNombreInstrumento() {
        return nombreInstrumento;
    }

    public Subcategoria getSubcategoria() {
        return subcategoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public String getModeloInstrumento() {
        return modeloInstrumento;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public BigDecimal getPrecioAlquiler() {
        return precioAlquiler;
    }

    public EstadoInstrumento getEstadoInstrumento() {
        return estadoInstrumento;
    }

    public void setEstadoInstrumento(EstadoInstrumento estadoInstrumento) {
        this.estadoInstrumento = estadoInstrumento;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }
}
