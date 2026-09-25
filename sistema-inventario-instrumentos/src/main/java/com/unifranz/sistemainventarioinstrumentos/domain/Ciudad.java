package com.unifranz.sistemainventarioinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ciudades")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_ciudad")
    private Integer codCiudad;

    @Column(name = "nombre_ciudad", nullable = false, length = 50)
    private String nombreCiudad;

    protected Ciudad() {
    }

    public Ciudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public Integer getCodCiudad() {
        return codCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }
}
