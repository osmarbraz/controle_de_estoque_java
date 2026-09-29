package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Cidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar a lógica de CidadeDAO.
 */
public class CidadeDAO {

    /**
     * Executa a operação de inserir.
     * 
     * @param cidade Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean inserir(Cidade cidade) throws SQLException {
        String sql = "INSERT INTO Cidade (nome, uf) VALUES (?, ?)";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            declaracao.setString(1, cidade.getNome());
            declaracao.setString(2, cidade.getUf());
            int linhasAfetadas = declaracao.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet resultado = declaracao.getGeneratedKeys()) {
                    if (resultado.next()) {
                        cidade.setCidadeId(resultado.getInt(1));
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
     * @param cidade Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean atualizar(Cidade cidade) throws SQLException {
        String sql = "UPDATE Cidade SET nome = ?, uf = ? WHERE cidadeId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, cidade.getNome());
            declaracao.setString(2, cidade.getUf());
            declaracao.setInt(3, cidade.getCidadeId());
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de excluir.
     * 
     * @param cidadeId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean excluir(int cidadeId) throws SQLException {
        String sql = "DELETE FROM Cidade WHERE cidadeId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, cidadeId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de buscarPorId.
     * 
     * @param cidadeId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (Cidade)
     */
    public Cidade buscarPorId(int cidadeId) throws SQLException {
        String sql = "SELECT cidadeId, nome, uf FROM Cidade WHERE cidadeId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, cidadeId);
            try (ResultSet resultado = declaracao.executeQuery()) {
                if (resultado.next()) {
                    return new Cidade(
                            resultado.getInt("cidadeId"),
                            resultado.getString("nome"),
                            resultado.getString("uf"));
                }
            }
        }
        return null;
    }

    /**
     * Executa a operação de buscarTodos.
     * 
     * @return Retorna o resultado da operação (List<Cidade>)
     */
    public List<Cidade> buscarTodos() throws SQLException {
        List<Cidade> lista = new ArrayList<>();
        String sql = "SELECT cidadeId, nome, uf FROM Cidade ORDER BY nome ASC";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql);
                ResultSet resultado = declaracao.executeQuery()) {
            while (resultado.next()) {
                lista.add(new Cidade(
                        resultado.getInt("cidadeId"),
                        resultado.getString("nome"),
                        resultado.getString("uf")));
            }
        }
        return lista;
    }
}
