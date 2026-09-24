package com.estoque.model;

import java.io.Serializable;

public class ItemPedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pedidoId;
    private Integer produtoId;
    private String produtoDescricao;
    private String unidade;
    private double quantidade;
    private double precoVenda;

    public ItemPedido() {}

    public ItemPedido(Integer pedidoId, Integer produtoId, double quantidade, double precoVenda) {
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoDescricao() {
        return produtoDescricao;
    }

    public void setProdutoDescricao(String produtoDescricao) {
        this.produtoDescricao = produtoDescricao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public double getSubtotal() {
        return quantidade * precoVenda;
    }
}
