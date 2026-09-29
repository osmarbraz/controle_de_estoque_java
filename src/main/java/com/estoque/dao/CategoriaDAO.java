package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar a lógica de CategoriaDAO.
 */
public class CategoriaDAO {

    /**
     * Executa a operação de inserir.
     * 
     * @param categoria Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean inserir(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO Categoria (nome) VALUES (?)";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            declaracao.setString(1, categoria.getNome());
            int linhasAfetadas = declaracao.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet resultado = declaracao.getGeneratedKeys()) {
                    if (resultado.next()) {
                        categoria.setCategoriaId(resultado.getInt(1));
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
     * @param categoria Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE Categoria SET nome = ? WHERE categoriaId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, categoria.getNome());
            declaracao.setInt(2, categoria.getCategoriaId());
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de excluir.
     * 
     * @param categoriaId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean excluir(int categoriaId) throws SQLException {
        String sql = "DELETE FROM Categoria WHERE categoriaId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, categoriaId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de buscarPorId.
     * 
     * @param categoriaId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (Categoria)
     */
    public Categoria buscarPorId(int categoriaId) throws SQLException {
        String sql = "SELECT categoriaId, nome FROM Categoria WHERE categoriaId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, categoriaId);
            try (ResultSet resultado = declaracao.executeQuery()) {
                if (resultado.next()) {
                    return new Categoria(
                            resultado.getInt("categoriaId"),
                            resultado.getString("nome"));
                }
            }
        }
        return null;
    }

    /**
     * Executa a operação de buscarTodos.
     * 
     * @return Retorna o resultado da operação (List<Categoria>)
     */
    public List<Categoria> buscarTodos() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT categoriaId, nome FROM Categoria ORDER BY nome ASC";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql);
                ResultSet resultado = declaracao.executeQuery()) {
            while (resultado.next()) {
                lista.add(new Categoria(
                        resultado.getInt("categoriaId"),
                        resultado.getString("nome")));
            }
        }
        return lista;
    }
}
