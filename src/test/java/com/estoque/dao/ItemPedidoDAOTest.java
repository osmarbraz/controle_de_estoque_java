package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.ItemPedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ItemPedidoDAOTest {

    private MockedStatic<BancoDeDadosConfiguracao> mockedConfig;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private ItemPedidoDAO itemPedidoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockedConfig = mockStatic(BancoDeDadosConfiguracao.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedConfig.when(BancoDeDadosConfiguracao::obterConexao).thenReturn(mockConnection);
        itemPedidoDAO = new ItemPedidoDAO();
    }

    @AfterEach
    void tearDown() {
        mockedConfig.close();
    }

    @Test
    void testInserirSucessoEFalha() throws SQLException {
        ItemPedido item = new ItemPedido(1, 2, 3.0, 50.0);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(itemPedidoDAO.inserir(item));
        assertFalse(itemPedidoDAO.inserir(item));

        verify(mockStatement, times(2)).setInt(1, 1);
        verify(mockStatement, times(2)).setInt(2, 2);
        verify(mockStatement, times(2)).setDouble(3, 3.0);
        verify(mockStatement, times(2)).setDouble(4, 50.0);
    }

    @Test
    void testAtualizarSucessoEFalha() throws SQLException {
        ItemPedido item = new ItemPedido(1, 2, 4.0, 55.0);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(itemPedidoDAO.atualizar(item));
        assertFalse(itemPedidoDAO.atualizar(item));

        verify(mockStatement, times(2)).setDouble(1, 4.0);
        verify(mockStatement, times(2)).setDouble(2, 55.0);
        verify(mockStatement, times(2)).setInt(3, 1);
        verify(mockStatement, times(2)).setInt(4, 2);
    }

    @Test
    void testExcluirSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(itemPedidoDAO.excluir(1, 2));
        assertFalse(itemPedidoDAO.excluir(1, 2));

        verify(mockStatement, times(2)).setInt(1, 1);
        verify(mockStatement, times(2)).setInt(2, 2);
    }

    @Test
    void testDeleteAllByPedidoIdSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(2).thenReturn(0);

        assertTrue(itemPedidoDAO.deleteAllByPedidoId(10));
        assertFalse(itemPedidoDAO.deleteAllByPedidoId(10));

        verify(mockStatement, times(2)).setInt(1, 10);
    }

    @Test
    void testFindByPedidoAndProdutoEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("pedidoId")).thenReturn(1);
        when(mockResultSet.getInt("produtoId")).thenReturn(2);
        when(mockResultSet.getDouble("quantidade")).thenReturn(5.0);
        when(mockResultSet.getDouble("precoVenda")).thenReturn(19.90);
        when(mockResultSet.getString("produtoDescricao")).thenReturn("Pendrive");
        when(mockResultSet.getString("unidade")).thenReturn("UN");

        ItemPedido item = itemPedidoDAO.findByPedidoAndProduto(1, 2);

        assertNotNull(item);
        assertEquals(1, item.getPedidoId());
        assertEquals(2, item.getProdutoId());
        assertEquals(5.0, item.getQuantidade(), 0.001);
        assertEquals(19.90, item.getPrecoVenda(), 0.001);
        assertEquals("Pendrive", item.getProdutoDescricao());
        assertEquals("UN", item.getUnidade());
    }

    @Test
    void testFindByPedidoAndProdutoNaoEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        ItemPedido item = itemPedidoDAO.findByPedidoAndProduto(99, 88);

        assertNull(item);
    }

    @Test
    void testBuscarPorPedidoId() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("pedidoId")).thenReturn(1);
        when(mockResultSet.getInt("produtoId")).thenReturn(2);
        when(mockResultSet.getDouble("quantidade")).thenReturn(1.0);
        when(mockResultSet.getDouble("precoVenda")).thenReturn(10.0);
        when(mockResultSet.getString("produtoDescricao")).thenReturn("Item 1");
        when(mockResultSet.getString("unidade")).thenReturn("UN");

        List<ItemPedido> lista = itemPedidoDAO.buscarPorPedidoId(1);

        assertEquals(1, lista.size());
        assertEquals("Item 1", lista.get(0).getProdutoDescricao());
    }
}
