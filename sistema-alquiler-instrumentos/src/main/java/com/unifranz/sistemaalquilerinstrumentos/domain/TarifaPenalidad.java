package com.unifranz.sistemaalquilerinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/** Tarifa configurable que usa el sistema para calcular las penalidades. */
@Entity
@Table(name = "tarifas_penalidad")
public class TarifaPenalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_tarifa")
    private Integer codTarifa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_penalidad", nullable = false, length = 15, unique = true)
    private TipoPenalidad tipoPenalidad;

    @Column(name = "descripcion", nullable = false, length = 150)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_calculo", nullable = false, length = 15)
    private TipoCalculo tipoCalculo;

    @Column(name = "valor", nullable = false, precision = 7, scale = 2)
    private BigDecimal valor;

    @Column(name = "activa", nullable = false)
    private boolean activa = true;

    protected TarifaPenalidad() {
    }

    public TarifaPenalidad(TipoPenalidad tipoPenalidad, String descripcion, TipoCalculo tipoCalculo, BigDecimal valor) {
        this.tipoPenalidad = tipoPenalidad;
        this.descripcion = descripcion;
        this.tipoCalculo = tipoCalculo;
        this.valor = valor;
    }

    public Integer getCodTarifa() { return codTarifa; }
    public TipoPenalidad getTipoPenalidad() { return tipoPenalidad; }
    public String getDescripcion() { return descripcion; }
    public TipoCalculo getTipoCalculo() { return tipoCalculo; }
    public BigDecimal getValor() { return valor; }
    public boolean isActiva() { return activa; }
}
