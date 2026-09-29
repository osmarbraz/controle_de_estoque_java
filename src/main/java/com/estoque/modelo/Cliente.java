package com.estoque.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa a entidade Cliente no sistema.
 */
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clienteId;
    private String nome;
    private String cpf;
    private String rua;
    private String numero;
    private String bairro;
    private Integer cidadeId;
    private String cidadeNome;
    private String cidadeUf;
    private Date dataCadastro;

    /**
     * Construtor da classe Cliente.
     */
    public Cliente() {}

    /**
     * Construtor da classe Cliente.
     * @param clienteId Valor inicial para clienteId
     * @param nome Valor inicial para nome
     * @param cpf Valor inicial para cpf
     * @param rua Valor inicial para rua
     * @param numero Valor inicial para numero
     * @param bairro Valor inicial para bairro
     * @param cidadeId Valor inicial para cidadeId
     * @param dataCadastro Valor inicial para dataCadastro
     */
    public Cliente(Integer clienteId, String nome, String cpf, String rua, String numero, String bairro, Integer cidadeId, Date dataCadastro) {
        this.clienteId = clienteId;
        this.nome = nome;
        this.cpf = cpf;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidadeId = cidadeId;
        this.dataCadastro = dataCadastro;
    }

    /**
     * Obtém o valor de ClienteId.
     * @return Retorna o valor correspondente
     */
    public Integer getClienteId() {
        return clienteId;
    }

    /**
     * Define o valor de ClienteId.
     * @param clienteId Parâmetro para a operação
     */
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
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
     * Obtém o valor de Cpf.
     * @return Retorna o valor correspondente
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o valor de Cpf.
     * @param cpf Parâmetro para a operação
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Obtém o valor de Rua.
     * @return Retorna o valor correspondente
     */
    public String getRua() {
        return rua;
    }

    /**
     * Define o valor de Rua.
     * @param rua Parâmetro para a operação
     */
    public void setRua(String rua) {
        this.rua = rua;
    }

    /**
     * Obtém o valor de Numero.
     * @return Retorna o valor correspondente
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Define o valor de Numero.
     * @param numero Parâmetro para a operação
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Obtém o valor de Bairro.
     * @return Retorna o valor correspondente
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * Define o valor de Bairro.
     * @param bairro Parâmetro para a operação
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
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
     * Obtém o valor de CidadeNome.
     * @return Retorna o valor correspondente
     */
    public String getCidadeNome() {
        return cidadeNome;
    }

    /**
     * Define o valor de CidadeNome.
     * @param cidadeNome Parâmetro para a operação
     */
    public void setCidadeNome(String cidadeNome) {
        this.cidadeNome = cidadeNome;
    }

    /**
     * Obtém o valor de CidadeUf.
     * @return Retorna o valor correspondente
     */
    public String getCidadeUf() {
        return cidadeUf;
    }

    /**
     * Define o valor de CidadeUf.
     * @param cidadeUf Parâmetro para a operação
     */
    public void setCidadeUf(String cidadeUf) {
        this.cidadeUf = cidadeUf;
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
