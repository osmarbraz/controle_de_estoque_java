package com.estoque.modelo;

import java.io.Serializable;

/**
 * Representa a entidade Categoria no sistema.
 */
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer categoriaId;
    private String nome;

    /**
     * Construtor da classe Categoria.
     */
    public Categoria() {}

    /**
     * Construtor da classe Categoria.
     * @param categoriaId Valor inicial para categoriaId
     * @param nome Valor inicial para nome
     */
    public Categoria(Integer categoriaId, String nome) {
        this.categoriaId = categoriaId;
        this.nome = nome;
    }

    /**
     * Construtor da classe Categoria.
     * @param nome Valor inicial para nome
     */
    public Categoria(String nome) {
        this.nome = nome;
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
     * Obtém o valor de Nome.
     * @return Retorna o valor correspondente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor de Nome.
     * @param nome Parâmetro para a operação
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
}
