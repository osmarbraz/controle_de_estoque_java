package com.estoque.dao;

import com.estoque.config.DatabaseConfig;
import com.estoque.model.ItemPedido;
import com.estoque.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();

    public boolean insert(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO Pedido (clienteId, data, total, situacao) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pedido.getClienteId());
            Timestamp dataPed = pedido.getData() != null
                    ? new Timestamp(pedido.getData().getTime())
                    : new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(2, dataPed);
            stmt.setDouble(3, pedido.getTotal());
            stmt.setString(4, pedido.getSituacao() != null ? pedido.getSituacao() : "aberto");

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setPedidoId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean updateHeader(Pedido pedido) throws SQLException {
        String sql = "UPDATE Pedido SET clienteId = ?, situacao = ? WHERE pedidoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getClienteId());
            stmt.setString(2, pedido.getSituacao());
            stmt.setInt(3, pedido.getPedidoId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateSituacao(int pedidoId, String situacao) throws SQLException {
        String sql = "UPDATE Pedido SET situacao = ? WHERE pedidoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, situacao);
            stmt.setInt(2, pedidoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean recalculateTotal(int pedidoId) throws SQLException {
        String sql = "UPDATE Pedido SET total = COALESCE((SELECT SUM(quantidade * precoVenda) FROM ItemPedido WHERE pedidoId = ?), 0.0) WHERE pedidoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            stmt.setInt(2, pedidoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int pedidoId) throws SQLException {
        String sql = "DELETE FROM Pedido WHERE pedidoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Pedido findById(int pedidoId) throws SQLException {
        String sql = "SELECT p.pedidoId, p.clienteId, p.data, p.total, p.situacao, "
                   + "cli.Nome AS clienteNome "
                   + "FROM Pedido p "
                   + "INNER JOIN Cliente cli ON p.clienteId = cli.clienteId "
                   + "WHERE p.pedidoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pedido ped = new Pedido();
                    ped.setPedidoId(rs.getInt("pedidoId"));
                    ped.setClienteId(rs.getInt("clienteId"));
                    ped.setClienteNome(rs.getString("clienteNome"));
                    ped.setData(rs.getTimestamp("data"));
                    ped.setTotal(rs.getDouble("total"));
                    ped.setSituacao(rs.getString("situacao"));
                    ped.setItens(itemPedidoDAO.findByPedidoId(pedidoId));
                    return ped;
                }
            }
        }
        return null;
    }

    public List<Pedido> findAll() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.pedidoId, p.clienteId, p.data, p.total, p.situacao, "
                   + "cli.Nome AS clienteNome "
                   + "FROM Pedido p "
                   + "INNER JOIN Cliente cli ON p.clienteId = cli.clienteId "
                   + "ORDER BY p.pedidoId DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pedido ped = new Pedido();
                ped.setPedidoId(rs.getInt("pedidoId"));
                ped.setClienteId(rs.getInt("clienteId"));
                ped.setClienteNome(rs.getString("clienteNome"));
                ped.setData(rs.getTimestamp("data"));
                ped.setTotal(rs.getDouble("total"));
                ped.setSituacao(rs.getString("situacao"));
                lista.add(ped);
            }
        }
        return lista;
    }

    public List<Pedido> findAllWithItems() throws SQLException {
        List<Pedido> pedidos = findAll();
        for (Pedido p : pedidos) {
            p.setItens(itemPedidoDAO.findByPedidoId(p.getPedidoId()));
        }
        return pedidos;
    }
}
