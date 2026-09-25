package com.estoque.dao;

import com.estoque.config.DatabaseConfig;
import com.estoque.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public boolean insert(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produto (descricao, unidade, precoCusto, precoVenda, quantidade, categoriaId, dataCadastro) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getDescricao());
            stmt.setString(2, produto.getUnidade());
            stmt.setDouble(3, produto.getPrecoCusto());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setDouble(5, produto.getQuantidade());
            stmt.setInt(6, produto.getCategoriaId());
            Timestamp dataCad = produto.getDataCadastro() != null
                    ? new Timestamp(produto.getDataCadastro().getTime())
                    : new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(7, dataCad);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setProdutoId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Produto produto) throws SQLException {
        String sql = "UPDATE Produto SET descricao = ?, unidade = ?, precoCusto = ?, precoVenda = ?, quantidade = ?, categoriaId = ? "
                   + "WHERE produtoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getDescricao());
            stmt.setString(2, produto.getUnidade());
            stmt.setDouble(3, produto.getPrecoCusto());
            stmt.setDouble(4, produto.getPrecoVenda());
            stmt.setDouble(5, produto.getQuantidade());
            stmt.setInt(6, produto.getCategoriaId());
            stmt.setInt(7, produto.getProdutoId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateEstoque(int produtoId, double deltaQuantidade) throws SQLException {
        String sql = "UPDATE Produto SET quantidade = quantidade + ? WHERE produtoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, deltaQuantidade);
            stmt.setInt(2, produtoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int produtoId) throws SQLException {
        String sql = "DELETE FROM Produto WHERE produtoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Produto findById(int produtoId) throws SQLException {
        String sql = "SELECT p.produtoId, p.descricao, p.unidade, p.precoCusto, p.precoVenda, p.quantidade, p.categoriaId, p.dataCadastro, "
                   + "cat.nome AS categoriaNome "
                   + "FROM Produto p "
                   + "INNER JOIN Categoria cat ON p.categoriaId = cat.categoriaId "
                   + "WHERE p.produtoId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto p = new Produto();
                    p.setProdutoId(rs.getInt("produtoId"));
                    p.setDescricao(rs.getString("descricao"));
                    p.setUnidade(rs.getString("unidade"));
                    p.setPrecoCusto(rs.getDouble("precoCusto"));
                    p.setPrecoVenda(rs.getDouble("precoVenda"));
                    p.setQuantidade(rs.getDouble("quantidade"));
                    p.setCategoriaId(rs.getInt("categoriaId"));
                    p.setCategoriaNome(rs.getString("categoriaNome"));
                    p.setDataCadastro(rs.getTimestamp("dataCadastro"));
                    return p;
                }
            }
        }
        return null;
    }

    public List<Produto> findAll() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT p.produtoId, p.descricao, p.unidade, p.precoCusto, p.precoVenda, p.quantidade, p.categoriaId, p.dataCadastro, "
                   + "cat.nome AS categoriaNome "
                   + "FROM Produto p "
                   + "INNER JOIN Categoria cat ON p.categoriaId = cat.categoriaId "
                   + "ORDER BY p.descricao ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setProdutoId(rs.getInt("produtoId"));
                p.setDescricao(rs.getString("descricao"));
                p.setUnidade(rs.getString("unidade"));
                p.setPrecoCusto(rs.getDouble("precoCusto"));
                p.setPrecoVenda(rs.getDouble("precoVenda"));
                p.setQuantidade(rs.getDouble("quantidade"));
                p.setCategoriaId(rs.getInt("categoriaId"));
                p.setCategoriaNome(rs.getString("categoriaNome"));
                p.setDataCadastro(rs.getTimestamp("dataCadastro"));
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Produto> searchByDescricao(String query) throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT p.produtoId, p.descricao, p.unidade, p.precoCusto, p.precoVenda, p.quantidade, p.categoriaId, p.dataCadastro, "
                   + "cat.nome AS categoriaNome "
                   + "FROM Produto p "
                   + "INNER JOIN Categoria cat ON p.categoriaId = cat.categoriaId "
                   + "WHERE p.descricao LIKE ? "
                   + "ORDER BY p.descricao ASC LIMIT 20";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + (query != null ? query.trim() : "") + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto();
                    p.setProdutoId(rs.getInt("produtoId"));
                    p.setDescricao(rs.getString("descricao"));
                    p.setUnidade(rs.getString("unidade"));
                    p.setPrecoCusto(rs.getDouble("precoCusto"));
                    p.setPrecoVenda(rs.getDouble("precoVenda"));
                    p.setQuantidade(rs.getDouble("quantidade"));
                    p.setCategoriaId(rs.getInt("categoriaId"));
                    p.setCategoriaNome(rs.getString("categoriaNome"));
                    p.setDataCadastro(rs.getTimestamp("dataCadastro"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
