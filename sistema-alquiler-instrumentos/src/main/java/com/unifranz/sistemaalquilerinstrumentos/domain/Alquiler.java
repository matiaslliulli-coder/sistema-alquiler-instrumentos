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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Alquiler de un instrumento a un cliente por un plazo de dias. */
@Entity
@Table(name = "alquileres")
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_alquiler")
    private Integer codAlquiler;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_instrumento", nullable = false)
    private Instrumento instrumento;

    @Column(name = "fecha_alquiler", nullable = false)
    private LocalDate fechaAlquiler;

    @Column(name = "fecha_devolucion_prevista", nullable = false)
    private LocalDate fechaDevolucionPrevista;

    @Column(name = "dias_alquiler", nullable = false)
    private int diasAlquiler;

    @Column(name = "precio_dia", nullable = false, precision = 7, scale = 2)
    private BigDecimal precioDia;

    @Column(name = "monto_total", nullable = false, precision = 9, scale = 2)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_alquiler", nullable = false, length = 10)
    private EstadoAlquiler estadoAlquiler = EstadoAlquiler.ACTIVO;

    protected Alquiler() {
    }

    /** Crea un alquiler calculando la fecha de devolucion prevista y el monto total. */
    public Alquiler(Cliente cliente, Instrumento instrumento, LocalDate fechaAlquiler, int diasAlquiler) {
        this.cliente = cliente;
        this.instrumento = instrumento;
        this.fechaAlquiler = fechaAlquiler;
        this.diasAlquiler = diasAlquiler;
        this.fechaDevolucionPrevista = fechaAlquiler.plusDays(diasAlquiler);
        this.precioDia = instrumento.getPrecioAlquiler();
        this.montoTotal = precioDia.multiply(BigDecimal.valueOf(diasAlquiler));
    }

    public boolean estaPendiente() {
        return estadoAlquiler != EstadoAlquiler.DEVUELTO;
    }

    public void marcarAtrasado() {
        if (estadoAlquiler == EstadoAlquiler.ACTIVO) {
            this.estadoAlquiler = EstadoAlquiler.ATRASADO;
        }
    }

    public void marcarDevuelto() {
        this.estadoAlquiler = EstadoAlquiler.DEVUELTO;
    }

    public Integer getCodAlquiler() { return codAlquiler; }
    public Cliente getCliente() { return cliente; }
    public Instrumento getInstrumento() { return instrumento; }
    public LocalDate getFechaAlquiler() { return fechaAlquiler; }
    public LocalDate getFechaDevolucionPrevista() { return fechaDevolucionPrevista; }
    public int getDiasAlquiler() { return diasAlquiler; }
    public BigDecimal getPrecioDia() { return precioDia; }
    public BigDecimal getMontoTotal() { return montoTotal; }
    public EstadoAlquiler getEstadoAlquiler() { return estadoAlquiler; }
}
