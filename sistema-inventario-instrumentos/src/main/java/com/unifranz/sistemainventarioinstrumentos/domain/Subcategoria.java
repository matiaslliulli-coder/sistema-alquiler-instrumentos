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

/** Tipo de instrumento (por ejemplo Guitarra), enlazado a una categoria (por ejemplo Cuerdas). */
@Entity
@Table(name = "subcategorias")
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_subcategoria")
    private Integer codSubcategoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cod_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "nombre_subcategoria", nullable = false, length = 50)
    private String nombreSubcategoria;

    protected Subcategoria() {
    }

    public Subcategoria(Categoria categoria, String nombreSubcategoria) {
        this.categoria = categoria;
        this.nombreSubcategoria = nombreSubcategoria;
    }

    public Integer getCodSubcategoria() {
        return codSubcategoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }
}
