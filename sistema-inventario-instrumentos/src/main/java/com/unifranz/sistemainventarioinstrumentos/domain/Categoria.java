package com.unifranz.sistemainventarioinstrumentos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_categoria")
    private Integer codCategoria;

    @Column(name = "nombre_categoria", nullable = false, length = 50)
    private String nombreCategoria;

    protected Categoria() {
    }

    public Categoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getCodCategoria() {
        return codCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }
}
