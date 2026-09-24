package com.estoque.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer pedidoId;
    private Integer clienteId;
    private String clienteNome;
    private Date data;
    private double total;
    private String situacao; // "aberto", "fechado"
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {}

    public Pedido(Integer pedidoId, Integer clienteId, Date data, double total, String situacao) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.data = data;
        this.total = total;
        this.situacao = situacao;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}
