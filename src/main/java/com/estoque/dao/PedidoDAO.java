package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar a lógica de PedidoDAO.
 */
public class PedidoDAO {

    private final ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();

    /**
     * Executa a operação de inserir.
     * 
     * @param pedido Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean inserir(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO Pedido (clienteId, data, total, situacao) VALUES (?, ?, ?, ?)";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            declaracao.setInt(1, pedido.getClienteId());
            Timestamp dataPed = pedido.getData() != null
                    ? new Timestamp(pedido.getData().getTime())
                    : new Timestamp(System.currentTimeMillis());
            declaracao.setTimestamp(2, dataPed);
            declaracao.setDouble(3, pedido.getTotal());
            declaracao.setString(4, pedido.getSituacao() != null ? pedido.getSituacao() : "aberto");

            int linhasAfetadas = declaracao.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet resultado = declaracao.getGeneratedKeys()) {
                    if (resultado.next()) {
                        pedido.setPedidoId(resultado.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Executa a operação de atualizarCabecalho.
     * 
     * @param pedido Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean atualizarCabecalho(Pedido pedido) throws SQLException {
        String sql = "UPDATE Pedido SET clienteId = ?, situacao = ? WHERE pedidoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, pedido.getClienteId());
            declaracao.setString(2, pedido.getSituacao());
            declaracao.setInt(3, pedido.getPedidoId());
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de atualizarSituacao.
     * 
     * @param pedidoId Parâmetro necessário para a operação
     * @param situacao Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean atualizarSituacao(int pedidoId, String situacao) throws SQLException {
        String sql = "UPDATE Pedido SET situacao = ? WHERE pedidoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, situacao);
            declaracao.setInt(2, pedidoId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de recalcularTotal.
     * 
     * @param pedidoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean recalcularTotal(int pedidoId) throws SQLException {
        String sql = "UPDATE Pedido SET total = COALESCE((SELECT SUM(quantidade * precoVenda) FROM ItemPedido WHERE pedidoId = ?), 0.0) WHERE pedidoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, pedidoId);
            declaracao.setInt(2, pedidoId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de excluir.
     * 
     * @param pedidoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (boolean)
     */
    public boolean excluir(int pedidoId) throws SQLException {
        String sql = "DELETE FROM Pedido WHERE pedidoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, pedidoId);
            return declaracao.executeUpdate() > 0;
        }
    }

    /**
     * Executa a operação de buscarPorId.
     * 
     * @param pedidoId Parâmetro necessário para a operação
     * @return Retorna o resultado da operação (Pedido)
     */
    public Pedido buscarPorId(int pedidoId) throws SQLException {
        String sql = "SELECT p.pedidoId, p.clienteId, p.data, p.total, p.situacao, "
                + "cli.Nome AS clienteNome "
                + "FROM Pedido p "
                + "INNER JOIN Cliente cli ON p.clienteId = cli.clienteId "
                + "WHERE p.pedidoId = ?";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, pedidoId);
            try (ResultSet resultado = declaracao.executeQuery()) {
                if (resultado.next()) {
                    Pedido ped = new Pedido();
                    ped.setPedidoId(resultado.getInt("pedidoId"));
                    ped.setClienteId(resultado.getInt("clienteId"));
                    ped.setClienteNome(resultado.getString("clienteNome"));
                    ped.setData(resultado.getTimestamp("data"));
                    ped.setTotal(resultado.getDouble("total"));
                    ped.setSituacao(resultado.getString("situacao"));
                    ped.setItens(itemPedidoDAO.buscarPorPedidoId(pedidoId));
                    return ped;
                }
            }
        }
        return null;
    }

    /**
     * Executa a operação de buscarTodos.
     * 
     * @return Retorna o resultado da operação (List<Pedido>)
     */
    public List<Pedido> buscarTodos() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.pedidoId, p.clienteId, p.data, p.total, p.situacao, "
                + "cli.Nome AS clienteNome "
                + "FROM Pedido p "
                + "INNER JOIN Cliente cli ON p.clienteId = cli.clienteId "
                + "ORDER BY p.pedidoId DESC";
        try (Connection conexao = BancoDeDadosConfiguracao.obterConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql);
                ResultSet resultado = declaracao.executeQuery()) {
            while (resultado.next()) {
                Pedido ped = new Pedido();
                ped.setPedidoId(resultado.getInt("pedidoId"));
                ped.setClienteId(resultado.getInt("clienteId"));
                ped.setClienteNome(resultado.getString("clienteNome"));
                ped.setData(resultado.getTimestamp("data"));
                ped.setTotal(resultado.getDouble("total"));
                ped.setSituacao(resultado.getString("situacao"));
                lista.add(ped);
            }
        }
        return lista;
    }

    /**
     * Executa a operação de buscarTodosComItens.
     * 
     * @return Retorna o resultado da operação (List<Pedido>)
     */
    public List<Pedido> buscarTodosComItens() throws SQLException {
        List<Pedido> pedidos = buscarTodos();
        for (Pedido p : pedidos) {
            p.setItens(itemPedidoDAO.buscarPorPedidoId(p.getPedidoId()));
        }
        return pedidos;
    }
}
