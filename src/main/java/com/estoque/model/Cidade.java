package com.estoque.model;

import java.io.Serializable;

public class Cidade implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer cidadeId;
    private String nome;
    private String uf;

    public Cidade() {}

    public Cidade(Integer cidadeId, String nome, String uf) {
        this.cidadeId = cidadeId;
        this.nome = nome;
        this.uf = uf;
    }

    public Cidade(String nome, String uf) {
        this.nome = nome;
        this.uf = uf;
    }

    public Integer getCidadeId() {
        return cidadeId;
    }

    public void setCidadeId(Integer cidadeId) {
        this.cidadeId = cidadeId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
