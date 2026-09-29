package com.estoque.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representa a entidade Pedido no sistema.
 */
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pedidoId;
    private Integer clienteId;
    private String clienteNome;
    private Date data;
    private double total;
    private String situacao; // "aberto", "fechado"
    private List<ItemPedido> itens = new ArrayList<>();

    /**
     * Construtor da classe Pedido.
     */
    public Pedido() {}

    /**
     * Construtor da classe Pedido.
     * @param pedidoId Valor inicial para pedidoId
     * @param clienteId Valor inicial para clienteId
     * @param data Valor inicial para data
     * @param total Valor inicial para total
     * @param situacao Valor inicial para situacao
     */
    public Pedido(Integer pedidoId, Integer clienteId, Date data, double total, String situacao) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.data = data;
        this.total = total;
        this.situacao = situacao;
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
     * Obtém o valor de ClienteNome.
     * @return Retorna o valor correspondente
     */
    public String getClienteNome() {
        return clienteNome;
    }

    /**
     * Define o valor de ClienteNome.
     * @param clienteNome Parâmetro para a operação
     */
    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    /**
     * Obtém o valor de Data.
     * @return Retorna o valor correspondente
     */
    public Date getData() {
        return data;
    }

    /**
     * Define o valor de Data.
     * @param data Parâmetro para a operação
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Obtém o valor de Total.
     * @return Retorna o valor correspondente
     */
    public double getTotal() {
        return total;
    }

    /**
     * Define o valor de Total.
     * @param total Parâmetro para a operação
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Obtém o valor de Situacao.
     * @return Retorna o valor correspondente
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * Define o valor de Situacao.
     * @param situacao Parâmetro para a operação
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * Obtém o valor de Itens.
     * @return Retorna o valor correspondente
     */
    public List<ItemPedido> getItens() {
        return itens;
    }

    /**
     * Define o valor de Itens.
     * @param itens Parâmetro para a operação
     */
    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}
