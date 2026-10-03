package com.unifranz.sistemaalquilerinstrumentos.domain;

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
import java.time.LocalDateTime;

/** Cobro generado por una devolucion (atraso o dano). */
@Entity
@Table(name = "penalidades")
public class Penalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_penalidad")
    private Integer codPenalidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_devolucion", nullable = false)
    private Devolucion devolucion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_tarifa", nullable = false)
    private TarifaPenalidad tarifa;

    @Column(name = "concepto", nullable = false, length = 150)
    private String concepto;

    @Column(name = "monto", nullable = false, precision = 9, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion = LocalDateTime.now();

    protected Penalidad() {
    }

    public Penalidad(Devolucion devolucion, TarifaPenalidad tarifa, String concepto, BigDecimal monto) {
        this.devolucion = devolucion;
        this.tarifa = tarifa;
        this.concepto = concepto;
        this.monto = monto;
    }

    public Integer getCodPenalidad() { return codPenalidad; }
    public Devolucion getDevolucion() { return devolucion; }
    public TarifaPenalidad getTarifa() { return tarifa; }
    public String getConcepto() { return concepto; }
    public BigDecimal getMonto() { return monto; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
}
