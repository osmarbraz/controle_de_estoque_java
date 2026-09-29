package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar a lógica de ClienteDAO.
 */
public class ClienteDAO {

    /**
     * Executa a operação de inserir.
     * 
     * @param cliente Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (Nome, CPF, rua, numero, bairro, cidadeId, dataCadastro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            declaracao.setString(1, cliente.getNome());
            declaracao.setString(2, cliente.getCpf());
            declaracao.setString(3, cliente.getRua());
            declaracao.setString(4, cliente.getNumero());
            declaracao.setString(5, cliente.getBairro());
            declaracao.setInt(6, cliente.getCidadeId());
            Timestamp dataCad = cliente.getDataCadastro() != null
                    ? new Timestamp(cliente.getDataCadastro().getTime())
                    : new Timestamp(System.currentTimeMillis());
            declaracao.setTimestamp(7, dataCad);

            int linhasAfetadas = declaracao.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet resultado = declaracao.getGeneratedKeys()) {
                    if (resultado.next()) {
                        cliente.setClienteId(resultado.getInt(1));
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
     * @param cliente Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET Nome = ?, CPF = ?, rua = ?, numero = ?, bairro = ?, cidadeId = ? WHERE clienteId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, cliente.getNome());
            declaracao.setString(2, cliente.getCpf());
            declaracao.setString(3, cliente.getRua());
            declaracao.setString(4, cliente.getNumero());
            declaracao.setString(5, cliente.getBairro());
            declaracao.setInt(6, cliente.getCidadeId());
            declaracao.setInt(7, cliente.getClienteId());
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de excluir.
     * 
     * @param clienteId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean excluir(int clienteId) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE clienteId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, clienteId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de buscarPorId.
     * 
     * @param clienteId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (Cliente)
     */
    public Cliente buscarPorId(int clienteId) throws SQLException {
        String sql = "SELECT cl.clienteId, cl.Nome, cl.CPF, cl.rua, cl.numero, cl.bairro, cl.cidadeId, cl.dataCadastro, "
                + "ci.nome AS cidadeNome, ci.uf AS cidadeUf "
                + "FROM Cliente cl "
                + "INNER JOIN Cidade ci ON cl.cidadeId = ci.cidadeId "
                + "WHERE cl.clienteId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, clienteId);
            try (ResultSet resultado = declaracao.executeQuery()) {
                if (resultado.next()) {
                    Cliente cli = new Cliente();
                    cli.setClienteId(resultado.getInt("clienteId"));
                    cli.setNome(resultado.getString("Nome"));
                    cli.setCpf(resultado.getString("CPF"));
                    cli.setRua(resultado.getString("rua"));
                    cli.setNumero(resultado.getString("numero"));
                    cli.setBairro(resultado.getString("bairro"));
                    cli.setCidadeId(resultado.getInt("cidadeId"));
                    cli.setCidadeNome(resultado.getString("cidadeNome"));
                    cli.setCidadeUf(resultado.getString("cidadeUf"));
                    cli.setDataCadastro(resultado.getTimestamp("dataCadastro"));
                    return cli;
                }
            }
        }
        return null;
    }

    /**
     * Executa a operação de buscarTodos.
     * 
     * @return Retorna o resultado da operação (List<Cliente>)
     */
    public List<Cliente> buscarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT cl.clienteId, cl.Nome, cl.CPF, cl.rua, cl.numero, cl.bairro, cl.cidadeId, cl.dataCadastro, "
                + "ci.nome AS cidadeNome, ci.uf AS cidadeUf "
                + "FROM Cliente cl "
                + "INNER JOIN Cidade ci ON cl.cidadeId = ci.cidadeId "
                + "ORDER BY cl.Nome ASC";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql);
                ResultSet resultado = declaracao.executeQuery()) {
            while (resultado.next()) {
                Cliente cli = new Cliente();
                cli.setClienteId(resultado.getInt("clienteId"));
                cli.setNome(resultado.getString("Nome"));
                cli.setCpf(resultado.getString("CPF"));
                cli.setRua(resultado.getString("rua"));
                cli.setNumero(resultado.getString("numero"));
                cli.setBairro(resultado.getString("bairro"));
                cli.setCidadeId(resultado.getInt("cidadeId"));
                cli.setCidadeNome(resultado.getString("cidadeNome"));
                cli.setCidadeUf(resultado.getString("cidadeUf"));
                cli.setDataCadastro(resultado.getTimestamp("dataCadastro"));
                lista.add(cli);
            }
        }
        return lista;
    }
}
