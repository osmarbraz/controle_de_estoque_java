package com.estoque.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa a entidade Produto no sistema.
 */
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

    /**
     * Construtor da classe Produto.
     */
    public Produto() {}

    /**
     * Construtor da classe Produto.
     * @param produtoId Valor inicial para produtoId
     * @param descricao Valor inicial para descricao
     * @param unidade Valor inicial para unidade
     * @param precoCusto Valor inicial para precoCusto
     * @param precoVenda Valor inicial para precoVenda
     * @param quantidade Valor inicial para quantidade
     * @param categoriaId Valor inicial para categoriaId
     * @param dataCadastro Valor inicial para dataCadastro
     */
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
     * Obtém o valor de Descricao.
     * @return Retorna o valor correspondente
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define o valor de Descricao.
     * @param descricao Parâmetro para a operação
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
     * Obtém o valor de PrecoCusto.
     * @return Retorna o valor correspondente
     */
    public double getPrecoCusto() {
        return precoCusto;
    }

    /**
     * Define o valor de PrecoCusto.
     * @param precoCusto Parâmetro para a operação
     */
    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
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
     * Obtém o valor de CategoriaId.
     * @return Retorna o valor correspondente
     */
    public Integer getCategoriaId() {
        return categoriaId;
    }

    /**
     * Define o valor de CategoriaId.
     * @param categoriaId Parâmetro para a operação
     */
    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    /**
     * Obtém o valor de CategoriaNome.
     * @return Retorna o valor correspondente
     */
    public String getCategoriaNome() {
        return categoriaNome;
    }

    /**
     * Define o valor de CategoriaNome.
     * @param categoriaNome Parâmetro para a operação
     */
    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    /**
     * Obtém o valor de DataCadastro.
     * @return Retorna o valor correspondente
     */
    public Date getDataCadastro() {
        return dataCadastro;
    }

    /**
     * Define o valor de DataCadastro.
     * @param dataCadastro Parâmetro para a operação
     */
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
