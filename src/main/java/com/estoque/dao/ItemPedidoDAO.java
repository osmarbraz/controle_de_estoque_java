package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.ItemPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar a lógica de ItemPedidoDAO.
 */
public class ItemPedidoDAO {

    /**
     * Executa a operação de inserir.
     * 
     * @param item Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean inserir(ItemPedido item) throws SQLException {
        String sql = "INSERT INTO ItemPedido (pedidoId, produtoId, quantidade, precoVenda) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE quantidade = quantidade + VALUES(quantidade), precoVenda = VALUES(precoVenda)";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, item.getPedidoId());
            declaracao.setInt(2, item.getProdutoId());
            declaracao.setDouble(3, item.getQuantidade());
            declaracao.setDouble(4, item.getPrecoVenda());
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de atualizar.
     * 
     * @param item Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean atualizar(ItemPedido item) throws SQLException {
        String sql = "UPDATE ItemPedido SET quantidade = ?, precoVenda = ? WHERE pedidoId = ? AND produtoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setDouble(1, item.getQuantidade());
            declaracao.setDouble(2, item.getPrecoVenda());
            declaracao.setInt(3, item.getPedidoId());
            declaracao.setInt(4, item.getProdutoId());
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de excluir.
     * 
     * @param pedidoId  Parâmetro necessário para a operação
     * @param produtoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean excluir(int pedidoId, int produtoId) throws SQLException {
        String sql = "DELETE FROM ItemPedido WHERE pedidoId = ? AND produtoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, pedidoId);
            declaracao.setInt(2, produtoId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de deleteAllByPedidoId.
     * 
     * @param pedidoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean deleteAllByPedidoId(int pedidoId) throws SQLException {
        String sql = "DELETE FROM ItemPedido WHERE pedidoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, pedidoId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de findByPedidoAndProduto.
     * 
     * @param pedidoId  Parâmetro necessário para a operação
     * @param produtoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (ItemPedido)
     */
    public ItemPedido findByPedidoAndProduto(int pedidoId, int produtoId) throws SQLException {
        String sql = "SELECT i.pedidoId, i.produtoId, i.quantidade, i.precoVenda, "
                + "p.descricao AS produtoDescricao, p.unidade "
                + "FROM ItemPedido i "
                + "INNER JOIN Produto p ON i.produtoId = p.produtoId "
                + "WHERE i.pedidoId = ? AND i.produtoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, pedidoId);
            declaracao.setInt(2, produtoId);
            try (ResultSet resultado = declaracao.executeQuery()) {
                if (resultado.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setPedidoId(resultado.getInt("pedidoId"));
                    item.setProdutoId(resultado.getInt("produtoId"));
                    item.setQuantidade(resultado.getDouble("quantidade"));
                    item.setPrecoVenda(resultado.getDouble("precoVenda"));
                    item.setProdutoDescricao(resultado.getString("produtoDescricao"));
                    item.setUnidade(resultado.getString("unidade"));
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Executa a operação de buscarPorPedidoId.
     * 
     * @param pedidoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (List<ItemPedido>)
     */
    public List<ItemPedido> buscarPorPedidoId(int pedidoId) throws SQLException {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT i.pedidoId, i.produtoId, i.quantidade, i.precoVenda, "
                + "p.descricao AS produtoDescricao, p.unidade "
                + "FROM ItemPedido i "
                + "INNER JOIN Produto p ON i.produtoId = p.produtoId "
                + "WHERE i.pedidoId = ? "
                + "ORDER BY p.descricao ASC";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, pedidoId);
            try (ResultSet resultado = declaracao.executeQuery()) {
                while (resultado.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setPedidoId(resultado.getInt("pedidoId"));
                    item.setProdutoId(resultado.getInt("produtoId"));
                    item.setQuantidade(resultado.getDouble("quantidade"));
                    item.setPrecoVenda(resultado.getDouble("precoVenda"));
                    item.setProdutoDescricao(resultado.getString("produtoDescricao"));
                    item.setUnidade(resultado.getString("unidade"));
                    lista.add(item);
                }
            }
        }
        return lista;
    }
}
