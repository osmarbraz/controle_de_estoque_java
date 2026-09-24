package com.estoque.model;

import java.io.Serializable;
import java.util.Date;

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

    public Cliente() {}

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

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getCidadeId() {
        return cidadeId;
    }

    public void setCidadeId(Integer cidadeId) {
        this.cidadeId = cidadeId;
    }

    public String getCidadeNome() {
        return cidadeNome;
    }

    public void setCidadeNome(String cidadeNome) {
        this.cidadeNome = cidadeNome;
    }

    public String getCidadeUf() {
        return cidadeUf;
    }

    public void setCidadeUf(String cidadeUf) {
        this.cidadeUf = cidadeUf;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
