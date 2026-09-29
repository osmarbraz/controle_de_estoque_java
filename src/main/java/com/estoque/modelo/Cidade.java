package com.estoque.modelo;

import java.io.Serializable;

/**
 * Representa a entidade Cidade no sistema.
 */
public class Cidade implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer cidadeId;
    private String nome;
    private String uf;

    /**
     * Construtor da classe Cidade.
     */
    public Cidade() {}

    /**
     * Construtor da classe Cidade.
     * @param cidadeId Valor inicial para cidadeId
     * @param nome Valor inicial para nome
     * @param uf Valor inicial para uf
     */
    public Cidade(Integer cidadeId, String nome, String uf) {
        this.cidadeId = cidadeId;
        this.nome = nome;
        this.uf = uf;
    }

    /**
     * Construtor da classe Cidade.
     * @param nome Valor inicial para nome
     * @param uf Valor inicial para uf
     */
    public Cidade(String nome, String uf) {
        this.nome = nome;
        this.uf = uf;
    }

    /**
     * Obtém o valor de CidadeId.
     * @return Retorna o valor correspondente
     */
    public Integer getCidadeId() {
        return cidadeId;
    }

    /**
     * Define o valor de CidadeId.
     * @param cidadeId Parâmetro para a operação
     */
    public void setCidadeId(Integer cidadeId) {
        this.cidadeId = cidadeId;
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

    /**
     * Obtém o valor de Uf.
     * @return Retorna o valor correspondente
     */
    public String getUf() {
        return uf;
    }

    /**
     * Define o valor de Uf.
     * @param uf Parâmetro para a operação
     */
    public void setUf(String uf) {
        this.uf = uf;
    }
}
