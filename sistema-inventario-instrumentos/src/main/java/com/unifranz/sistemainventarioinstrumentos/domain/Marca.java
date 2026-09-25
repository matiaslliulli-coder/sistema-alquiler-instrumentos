package com.unifranz.sistemainventarioinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_marca")
    private Integer codMarca;

    @Column(name = "nombre_marca", nullable = false, length = 50)
    private String nombreMarca;

    protected Marca() {
    }

    public Marca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public Integer getCodMarca() {
        return codMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }
}
