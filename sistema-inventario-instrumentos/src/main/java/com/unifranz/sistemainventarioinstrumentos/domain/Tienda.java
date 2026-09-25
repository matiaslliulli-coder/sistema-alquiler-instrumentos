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

/** Sucursal de la cadena donde se alquilan los instrumentos. */
@Entity
@Table(name = "tiendas")
public class Tienda {

    public static final String ACTIVA = "A";
    public static final String INACTIVA = "I";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_tienda")
    private Integer codTienda;

    @Column(name = "nombre_tienda", nullable = false, length = 100)
    private String nombreTienda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_ciudad", nullable = false)
    private Ciudad ciudad;

    @Column(name = "direccion_tienda")
    private String direccionTienda;

    @Column(name = "telefono_tienda", length = 8)
    private String telefonoTienda;

    @Column(name = "latitud_tienda", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitudTienda;

    @Column(name = "longitud_tienda", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitudTienda;

    @Column(name = "responsable_tienda", length = 100)
    private String responsableTienda;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado = ACTIVA;

    protected Tienda() {
    }

    public Tienda(String nombreTienda, Ciudad ciudad, String direccionTienda, String telefonoTienda,
                  BigDecimal latitudTienda, BigDecimal longitudTienda, String responsableTienda) {
        this.nombreTienda = nombreTienda;
        this.ciudad = ciudad;
        this.direccionTienda = direccionTienda;
        this.telefonoTienda = telefonoTienda;
        this.latitudTienda = latitudTienda;
        this.longitudTienda = longitudTienda;
        this.responsableTienda = responsableTienda;
        this.estado = ACTIVA;
    }

    public boolean estaActiva() {
        return ACTIVA.equals(estado);
    }

    public Integer getCodTienda() {
        return codTienda;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public String getDireccionTienda() {
        return direccionTienda;
    }

    public String getTelefonoTienda() {
        return telefonoTienda;
    }

    public BigDecimal getLatitudTienda() {
        return latitudTienda;
    }

    public BigDecimal getLongitudTienda() {
        return longitudTienda;
    }

    public String getResponsableTienda() {
        return responsableTienda;
    }

    public String getEstado() {
        return estado;
    }
}
