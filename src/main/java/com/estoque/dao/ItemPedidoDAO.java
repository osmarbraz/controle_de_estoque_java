package com.estoque.dao;

import com.estoque.config.DatabaseConfig;
import com.estoque.model.ItemPedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    public boolean insert(ItemPedido item) throws SQLException {
        String sql = "INSERT INTO ItemPedido (pedidoId, produtoId, quantidade, precoVenda) VALUES (?, ?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE quantidade = quantidade + VALUES(quantidade), precoVenda = VALUES(precoVenda)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getPedidoId());
            stmt.setInt(2, item.getProdutoId());
            stmt.setDouble(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoVenda());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean update(ItemPedido item) throws SQLException {
        String sql = "UPDATE ItemPedido SET quantidade = ?, precoVenda = ? WHERE pedidoId = ? AND produtoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, item.getQuantidade());
            stmt.setDouble(2, item.getPrecoVenda());
            stmt.setInt(3, item.getPedidoId());
            stmt.setInt(4, item.getProdutoId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int pedidoId, int produtoId) throws SQLException {
        String sql = "DELETE FROM ItemPedido WHERE pedidoId = ? AND produtoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.setInt(2, produtoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteAllByPedidoId(int pedidoId) throws SQLException {
        String sql = "DELETE FROM ItemPedido WHERE pedidoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public ItemPedido findByPedidoAndProduto(int pedidoId, int produtoId) throws SQLException {
        String sql = "SELECT i.pedidoId, i.produtoId, i.quantidade, i.precoVenda, "
                   + "p.descricao AS produtoDescricao, p.unidade "
                   + "FROM ItemPedido i "
                   + "INNER JOIN Produto p ON i.produtoId = p.produtoId "
                   + "WHERE i.pedidoId = ? AND i.produtoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.setInt(2, produtoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setPedidoId(rs.getInt("pedidoId"));
                    item.setProdutoId(rs.getInt("produtoId"));
                    item.setQuantidade(rs.getDouble("quantidade"));
                    item.setPrecoVenda(rs.getDouble("precoVenda"));
                    item.setProdutoDescricao(rs.getString("produtoDescricao"));
                    item.setUnidade(rs.getString("unidade"));
                    return item;
                }
            }
        }
        return null;
    }

    public List<ItemPedido> findByPedidoId(int pedidoId) throws SQLException {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT i.pedidoId, i.produtoId, i.quantidade, i.precoVenda, "
                   + "p.descricao AS produtoDescricao, p.unidade "
                   + "FROM ItemPedido i "
                   + "INNER JOIN Produto p ON i.produtoId = p.produtoId "
                   + "WHERE i.pedidoId = ? "
                   + "ORDER BY p.descricao ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setPedidoId(rs.getInt("pedidoId"));
                    item.setProdutoId(rs.getInt("produtoId"));
                    item.setQuantidade(rs.getDouble("quantidade"));
                    item.setPrecoVenda(rs.getDouble("precoVenda"));
                    item.setProdutoDescricao(rs.getString("produtoDescricao"));
                    item.setUnidade(rs.getString("unidade"));
                    lista.add(item);
                }
            }
        }
        return lista;
    }
}
