package com.estoque.model;

import java.io.Serializable;

public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer categoriaId;
    private String nome;

    public Categoria() {}

    public Categoria(Integer categoriaId, String nome) {
        this.categoriaId = categoriaId;
        this.nome = nome;
    }

    public Categoria(String nome) {
        this.nome = nome;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
