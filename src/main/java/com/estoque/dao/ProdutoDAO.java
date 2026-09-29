package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar a lógica de ProdutoDAO.
 */
public class ProdutoDAO {

    /**
     * Executa a operação de inserir.
     * 
     * @param produto Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produto (descricao, unidade, precoCusto, precoVenda, quantidade, categoriaId, dataCadastro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            declaracao.setString(1, produto.getDescricao());
            declaracao.setString(2, produto.getUnidade());
            declaracao.setDouble(3, produto.getPrecoCusto());
            declaracao.setDouble(4, produto.getPrecoVenda());
            declaracao.setDouble(5, produto.getQuantidade());
            declaracao.setInt(6, produto.getCategoriaId());
            Timestamp dataCad = produto.getDataCadastro() != null
                    ? new Timestamp(produto.getDataCadastro().getTime())
                    : new Timestamp(System.currentTimeMillis());
            declaracao.setTimestamp(7, dataCad);

            int linhasAfetadas = declaracao.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet resultado = declaracao.getGeneratedKeys()) {
                    if (resultado.next()) {
                        produto.setProdutoId(resultado.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Executa a operação de atualizar.
     * 
     * @param produto Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE Produto SET descricao = ?, unidade = ?, precoCusto = ?, precoVenda = ?, quantidade = ?, categoriaId = ? "
                + "WHERE produtoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, produto.getDescricao());
            declaracao.setString(2, produto.getUnidade());
            declaracao.setDouble(3, produto.getPrecoCusto());
            declaracao.setDouble(4, produto.getPrecoVenda());
            declaracao.setDouble(5, produto.getQuantidade());
            declaracao.setInt(6, produto.getCategoriaId());
            declaracao.setInt(7, produto.getProdutoId());
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de atualizarEstoque.
     * 
     * @param produtoId       Parâmetro necessário para a operação
     * @param deltaQuantidade Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean atualizarEstoque(int produtoId, double deltaQuantidade) throws SQLException {
        String sql = "UPDATE Produto SET quantidade = quantidade + ? WHERE produtoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setDouble(1, deltaQuantidade);
            declaracao.setInt(2, produtoId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de excluir.
     * 
     * @param produtoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean excluir(int produtoId) throws SQLException {
        String sql = "DELETE FROM Produto WHERE produtoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, produtoId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de buscarPorId.
     * 
     * @param produtoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (Produto)
     */
    public Produto buscarPorId(int produtoId) throws SQLException {
        String sql = "SELECT p.produtoId, p.descricao, p.unidade, p.precoCusto, p.precoVenda, p.quantidade, p.categoriaId, p.dataCadastro, "
                + "cat.nome AS categoriaNome "
                + "FROM Produto p "
                + "INNER JOIN Categoria cat ON p.categoriaId = cat.categoriaId "
                + "WHERE p.produtoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, produtoId);
            try (ResultSet resultado = declaracao.executeQuery()) {
                if (resultado.next()) {
                    Produto p = new Produto();
                    p.setProdutoId(resultado.getInt("produtoId"));
                    p.setDescricao(resultado.getString("descricao"));
                    p.setUnidade(resultado.getString("unidade"));
                    p.setPrecoCusto(resultado.getDouble("precoCusto"));
                    p.setPrecoVenda(resultado.getDouble("precoVenda"));
                    p.setQuantidade(resultado.getDouble("quantidade"));
                    p.setCategoriaId(resultado.getInt("categoriaId"));
                    p.setCategoriaNome(resultado.getString("categoriaNome"));
                    p.setDataCadastro(resultado.getTimestamp("dataCadastro"));
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Executa a operação de buscarTodos.
     * 
     * @return Retorna o resultado da operação (List<Produto>)
     */
    public List<Produto> buscarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT p.produtoId, p.descricao, p.unidade, p.precoCusto, p.precoVenda, p.quantidade, p.categoriaId, p.dataCadastro, "
                + "cat.nome AS categoriaNome "
                + "FROM Produto p "
                + "INNER JOIN Categoria cat ON p.categoriaId = cat.categoriaId "
                + "ORDER BY p.descricao ASC";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql);
                ResultSet resultado = declaracao.executeQuery()) {
            while (resultado.next()) {
                Produto p = new Produto();
                p.setProdutoId(resultado.getInt("produtoId"));
                p.setDescricao(resultado.getString("descricao"));
                p.setUnidade(resultado.getString("unidade"));
                p.setPrecoCusto(resultado.getDouble("precoCusto"));
                p.setPrecoVenda(resultado.getDouble("precoVenda"));
                p.setQuantidade(resultado.getDouble("quantidade"));
                p.setCategoriaId(resultado.getInt("categoriaId"));
                p.setCategoriaNome(resultado.getString("categoriaNome"));
                p.setDataCadastro(resultado.getTimestamp("dataCadastro"));
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Executa a operação de buscarPorDescricao.
     * 
     * @param consulta Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (List<Produto>)
     */
    public List<Produto> buscarPorDescricao(String consulta) throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT p.produtoId, p.descricao, p.unidade, p.precoCusto, p.precoVenda, p.quantidade, p.categoriaId, p.dataCadastro, "
                + "cat.nome AS categoriaNome "
                + "FROM Produto p "
                + "INNER JOIN Categoria cat ON p.categoriaId = cat.categoriaId "
                + "WHERE p.descricao LIKE ? "
                + "ORDER BY p.descricao ASC LIMIT 20";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, "%" + (consulta != null ? consulta.trim() : "") + "%");
            try (ResultSet resultado = declaracao.executeQuery()) {
                while (resultado.next()) {
                    Produto p = new Produto();
                    p.setProdutoId(resultado.getInt("produtoId"));
                    p.setDescricao(resultado.getString("descricao"));
                    p.setUnidade(resultado.getString("unidade"));
                    p.setPrecoCusto(resultado.getDouble("precoCusto"));
                    p.setPrecoVenda(resultado.getDouble("precoVenda"));
                    p.setQuantidade(resultado.getDouble("quantidade"));
                    p.setCategoriaId(resultado.getInt("categoriaId"));
                    p.setCategoriaNome(resultado.getString("categoriaNome"));
                    p.setDataCadastro(resultado.getTimestamp("dataCadastro"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
