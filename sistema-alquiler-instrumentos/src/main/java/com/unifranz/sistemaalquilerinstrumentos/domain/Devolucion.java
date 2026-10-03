package com.unifranz.sistemaalquilerinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

/** Registro de la devolucion de un instrumento y de su estado de conservacion. */
@Entity
@Table(name = "devoluciones")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_devolucion")
    private Integer codDevolucion;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_alquiler", nullable = false, unique = true)
    private Alquiler alquiler;

    @Column(name = "fecha_devolucion_real", nullable = false)
    private LocalDate fechaDevolucionReal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_conservacion", nullable = false, length = 10)
    private EstadoConservacion estadoConservacion;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "dias_atraso", nullable = false)
    private int diasAtraso;

    protected Devolucion() {
    }

    public Devolucion(Alquiler alquiler, LocalDate fechaDevolucionReal, EstadoConservacion estadoConservacion,
                      String observaciones, int diasAtraso) {
        this.alquiler = alquiler;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estadoConservacion = estadoConservacion;
        this.observaciones = observaciones;
        this.diasAtraso = diasAtraso;
    }

    public Integer getCodDevolucion() { return codDevolucion; }
    public Alquiler getAlquiler() { return alquiler; }
    public LocalDate getFechaDevolucionReal() { return fechaDevolucionReal; }
    public EstadoConservacion getEstadoConservacion() { return estadoConservacion; }
    public String getObservaciones() { return observaciones; }
    public int getDiasAtraso() { return diasAtraso; }
}
