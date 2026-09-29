package com.estoque.modelo;

import java.io.Serializable;

/**
 * Representa a entidade ItemPedido no sistema.
 */
public class ItemPedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pedidoId;
    private Integer produtoId;
    private String produtoDescricao;
    private String unidade;
    private double quantidade;
    private double precoVenda;

    /**
     * Construtor da classe ItemPedido.
     */
    public ItemPedido() {}

    /**
     * Construtor da classe ItemPedido.
     * @param pedidoId Valor inicial para pedidoId
     * @param produtoId Valor inicial para produtoId
     * @param quantidade Valor inicial para quantidade
     * @param precoVenda Valor inicial para precoVenda
     */
    public ItemPedido(Integer pedidoId, Integer produtoId, double quantidade, double precoVenda) {
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }

    /**
     * Obtém o valor de PedidoId.
     * @return Retorna o valor correspondente
     */
    public Integer getPedidoId() {
        return pedidoId;
    }

    /**
     * Define o valor de PedidoId.
     * @param pedidoId Parâmetro para a operação
     */
    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    /**
     * Obtém o valor de ProdutoId.
     * @return Retorna o valor correspondente
     */
    public Integer getProdutoId() {
        return produtoId;
    }

    /**
     * Define o valor de ProdutoId.
     * @param produtoId Parâmetro para a operação
     */
    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }

    /**
     * Obtém o valor de ProdutoDescricao.
     * @return Retorna o valor correspondente
     */
    public String getProdutoDescricao() {
        return produtoDescricao;
    }

    /**
     * Define o valor de ProdutoDescricao.
     * @param produtoDescricao Parâmetro para a operação
     */
    public void setProdutoDescricao(String produtoDescricao) {
        this.produtoDescricao = produtoDescricao;
    }

    /**
     * Obtém o valor de Unidade.
     * @return Retorna o valor correspondente
     */
    public String getUnidade() {
        return unidade;
    }

    /**
     * Define o valor de Unidade.
     * @param unidade Parâmetro para a operação
     */
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    /**
     * Obtém o valor de Quantidade.
     * @return Retorna o valor correspondente
     */
    public double getQuantidade() {
        return quantidade;
    }

    /**
     * Define o valor de Quantidade.
     * @param quantidade Parâmetro para a operação
     */
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Obtém o valor de PrecoVenda.
     * @return Retorna o valor correspondente
     */
    public double getPrecoVenda() {
        return precoVenda;
    }

    /**
     * Define o valor de PrecoVenda.
     * @param precoVenda Parâmetro para a operação
     */
    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    /**
     * Obtém o valor de Subtotal.
     * @return Retorna o valor correspondente
     */
    public double getSubtotal() {
        return quantidade * precoVenda;
    }
}
