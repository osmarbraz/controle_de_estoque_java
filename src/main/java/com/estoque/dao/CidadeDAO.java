package com.estoque.dao;

import com.estoque.config.DatabaseConfig;
import com.estoque.model.Cidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class CidadeDAO {

    public boolean insert(Cidade cidade) throws SQLException {
        String sql = "INSERT INTO Cidade (nome, uf) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cidade.getNome());
            stmt.setString(2, cidade.getUf());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cidade.setCidadeId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Cidade cidade) throws SQLException {
        String sql = "UPDATE Cidade SET nome = ?, uf = ? WHERE cidadeId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cidade.getNome());
            stmt.setString(2, cidade.getUf());
            stmt.setInt(3, cidade.getCidadeId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int cidadeId) throws SQLException {
        String sql = "DELETE FROM Cidade WHERE cidadeId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cidadeId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Cidade findById(int cidadeId) throws SQLException {
        String sql = "SELECT cidadeId, nome, uf FROM Cidade WHERE cidadeId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cidadeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cidade(
                        rs.getInt("cidadeId"),
                        rs.getString("nome"),
                        rs.getString("uf")
                    );
                }
            }
        }
        return null;
    }

    public List<Cidade> findAll() throws SQLException {
        List<Cidade> lista = new ArrayList<>();
        String sql = "SELECT cidadeId, nome, uf FROM Cidade ORDER BY nome ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cidade(
                    rs.getInt("cidadeId"),
                    rs.getString("nome"),
                    rs.getString("uf")
                ));
            }
        }
        return lista;
    }
}
