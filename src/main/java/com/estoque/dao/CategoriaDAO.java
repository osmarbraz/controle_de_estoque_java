package com.estoque.dao;

import com.estoque.config.DatabaseConfig;
import com.estoque.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean insert(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO Categoria (nome) VALUES (?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoria.setCategoriaId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Categoria categoria) throws SQLException {
        String sql = "UPDATE Categoria SET nome = ? WHERE categoriaId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getCategoriaId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int categoriaId) throws SQLException {
        String sql = "DELETE FROM Categoria WHERE categoriaId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoriaId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Categoria findById(int categoriaId) throws SQLException {
        String sql = "SELECT categoriaId, nome FROM Categoria WHERE categoriaId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoriaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(
                        rs.getInt("categoriaId"),
                        rs.getString("nome")
                    );
                }
            }
        }
        return null;
    }

    public List<Categoria> findAll() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT categoriaId, nome FROM Categoria ORDER BY nome ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(
                    rs.getInt("categoriaId"),
                    rs.getString("nome")
                ));
            }
        }
        return lista;
    }
}
