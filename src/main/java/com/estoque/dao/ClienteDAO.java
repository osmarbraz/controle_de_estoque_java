package com.estoque.dao;

import com.estoque.config.DatabaseConfig;
import com.estoque.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean insert(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (Nome, CPF, rua, numero, bairro, cidadeId, dataCadastro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRua());
            stmt.setString(4, cliente.getNumero());
            stmt.setString(5, cliente.getBairro());
            stmt.setInt(6, cliente.getCidadeId());
            Timestamp dataCad = cliente.getDataCadastro() != null 
                    ? new Timestamp(cliente.getDataCadastro().getTime()) 
                    : new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(7, dataCad);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setClienteId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean update(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET Nome = ?, CPF = ?, rua = ?, numero = ?, bairro = ?, cidadeId = ? WHERE clienteId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRua());
            stmt.setString(4, cliente.getNumero());
            stmt.setString(5, cliente.getBairro());
            stmt.setInt(6, cliente.getCidadeId());
            stmt.setInt(7, cliente.getClienteId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int clienteId) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE clienteId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Cliente findById(int clienteId) throws SQLException {
        String sql = "SELECT cl.clienteId, cl.Nome, cl.CPF, cl.rua, cl.numero, cl.bairro, cl.cidadeId, cl.dataCadastro, "
                   + "ci.nome AS cidadeNome, ci.uf AS cidadeUf "
                   + "FROM Cliente cl "
                   + "INNER JOIN Cidade ci ON cl.cidadeId = ci.cidadeId "
                   + "WHERE cl.clienteId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cli = new Cliente();
                    cli.setClienteId(rs.getInt("clienteId"));
                    cli.setNome(rs.getString("Nome"));
                    cli.setCpf(rs.getString("CPF"));
                    cli.setRua(rs.getString("rua"));
                    cli.setNumero(rs.getString("numero"));
                    cli.setBairro(rs.getString("bairro"));
                    cli.setCidadeId(rs.getInt("cidadeId"));
                    cli.setCidadeNome(rs.getString("cidadeNome"));
                    cli.setCidadeUf(rs.getString("cidadeUf"));
                    cli.setDataCadastro(rs.getTimestamp("dataCadastro"));
                    return cli;
                }
            }
        }
        return null;
    }

    public List<Cliente> findAll() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT cl.clienteId, cl.Nome, cl.CPF, cl.rua, cl.numero, cl.bairro, cl.cidadeId, cl.dataCadastro, "
                   + "ci.nome AS cidadeNome, ci.uf AS cidadeUf "
                   + "FROM Cliente cl "
                   + "INNER JOIN Cidade ci ON cl.cidadeId = ci.cidadeId "
                   + "ORDER BY cl.Nome ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setClienteId(rs.getInt("clienteId"));
                cli.setNome(rs.getString("Nome"));
                cli.setCpf(rs.getString("CPF"));
                cli.setRua(rs.getString("rua"));
                cli.setNumero(rs.getString("numero"));
                cli.setBairro(rs.getString("bairro"));
                cli.setCidadeId(rs.getInt("cidadeId"));
                cli.setCidadeNome(rs.getString("cidadeNome"));
                cli.setCidadeUf(rs.getString("cidadeUf"));
                cli.setDataCadastro(rs.getTimestamp("dataCadastro"));
                lista.add(cli);
            }
        }
        return lista;
    }
}
