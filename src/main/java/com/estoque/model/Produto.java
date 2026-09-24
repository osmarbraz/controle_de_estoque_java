package com.estoque.model;

import java.io.Serializable;
import java.util.Date;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer produtoId;
    private String descricao;
    private String unidade;
    private double precoCusto;
    private double precoVenda;
    private double quantidade;
    private Integer categoriaId;
    private String categoriaNome;
    private Date dataCadastro;

    public Produto() {}

    public Produto(Integer produtoId, String descricao, String unidade, double precoCusto, double precoVenda, double quantidade, Integer categoriaId, Date dataCadastro) {
        this.produtoId = produtoId;
        this.descricao = descricao;
        this.unidade = unidade;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
        this.dataCadastro = dataCadastro;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
