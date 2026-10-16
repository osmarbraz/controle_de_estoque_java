package com.estoque.dao;

import com.estoque.TestUtils;
import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.ItemPedido;
import com.estoque.modelo.Pedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PedidoDAOTest {

    private MockedStatic<BancoDeDadosConfiguracao> mockedConfig;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private ItemPedidoDAO mockItemPedidoDAO;
    private PedidoDAO pedidoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockedConfig = mockStatic(BancoDeDadosConfiguracao.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockItemPedidoDAO = mock(ItemPedidoDAO.class);

        mockedConfig.when(BancoDeDadosConfiguracao::obterConexao).thenReturn(mockConnection);
        pedidoDAO = new PedidoDAO();
        TestUtils.setField(pedidoDAO, "itemPedidoDAO", mockItemPedidoDAO);
    }

    @AfterEach
    void tearDown() {
        mockedConfig.close();
    }

    @Test
    void testInserirComDataESituacaoSucesso() throws SQLException {
        Date data = new Date();
        Pedido p = new Pedido(null, 1, data, 150.0, "fechado");
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(101);

        boolean ok = pedidoDAO.inserir(p);

        assertTrue(ok);
        assertEquals(101, p.getPedidoId());
        verify(mockStatement).setInt(1, 1);
        verify(mockStatement).setTimestamp(eq(2), eq(new Timestamp(data.getTime())));
        verify(mockStatement).setDouble(3, 150.0);
        verify(mockStatement).setString(4, "fechado");
    }

    @Test
    void testInserirSemDataESemSituacaoSucesso() throws SQLException {
        Pedido p = new Pedido();
        p.setClienteId(2);
        p.setTotal(0.0);
        // data and situacao null to test defaults

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean ok = pedidoDAO.inserir(p);

        assertTrue(ok);
        assertNull(p.getPedidoId());
        verify(mockStatement).setTimestamp(eq(2), any(Timestamp.class));
        verify(mockStatement).setString(4, "aberto");
    }

    @Test
    void testInserirFalha() throws SQLException {
        Pedido p = new Pedido();
        p.setClienteId(1);
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        boolean ok = pedidoDAO.inserir(p);

        assertFalse(ok);
    }

    @Test
    void testAtualizarCabecalhoSucessoEFalha() throws SQLException {
        Pedido p = new Pedido(10, 3, new Date(), 0.0, "fechado");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(pedidoDAO.atualizarCabecalho(p));
        assertFalse(pedidoDAO.atualizarCabecalho(p));

        verify(mockStatement, times(2)).setInt(1, 3);
        verify(mockStatement, times(2)).setString(2, "fechado");
        verify(mockStatement, times(2)).setInt(3, 10);
    }

    @Test
    void testAtualizarSituacaoSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(pedidoDAO.atualizarSituacao(10, "fechado"));
        assertFalse(pedidoDAO.atualizarSituacao(10, "fechado"));

        verify(mockStatement, times(2)).setString(1, "fechado");
        verify(mockStatement, times(2)).setInt(2, 10);
    }

    @Test
    void testRecalcularTotalSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(pedidoDAO.recalcularTotal(10));
        assertFalse(pedidoDAO.recalcularTotal(10));

        verify(mockStatement, times(2)).setInt(1, 10);
        verify(mockStatement, times(2)).setInt(2, 10);
    }

    @Test
    void testExcluirSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(pedidoDAO.excluir(10));
        assertFalse(pedidoDAO.excluir(10));

        verify(mockStatement, times(2)).setInt(1, 10);
    }

    @Test
    void testBuscarPorIdEncontrado() throws SQLException {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("pedidoId")).thenReturn(10);
        when(mockResultSet.getInt("clienteId")).thenReturn(2);
        when(mockResultSet.getString("clienteNome")).thenReturn("Maria");
        when(mockResultSet.getTimestamp("data")).thenReturn(agora);
        when(mockResultSet.getDouble("total")).thenReturn(200.0);
        when(mockResultSet.getString("situacao")).thenReturn("aberto");

        ItemPedido item = new ItemPedido(10, 1, 2.0, 100.0);
        when(mockItemPedidoDAO.buscarPorPedidoId(10)).thenReturn(Collections.singletonList(item));

        Pedido p = pedidoDAO.buscarPorId(10);

        assertNotNull(p);
        assertEquals(10, p.getPedidoId());
        assertEquals(2, p.getClienteId());
        assertEquals("Maria", p.getClienteNome());
        assertEquals(agora, p.getData());
        assertEquals(200.0, p.getTotal(), 0.001);
        assertEquals("aberto", p.getSituacao());
        assertEquals(1, p.getItens().size());
        assertEquals(item, p.getItens().get(0));
    }

    @Test
    void testBuscarPorIdNaoEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Pedido p = pedidoDAO.buscarPorId(999);

        assertNull(p);
    }

    @Test
    void testBuscarTodos() throws SQLException {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("pedidoId")).thenReturn(1);
        when(mockResultSet.getInt("clienteId")).thenReturn(2);
        when(mockResultSet.getString("clienteNome")).thenReturn("Carlos");
        when(mockResultSet.getTimestamp("data")).thenReturn(agora);
        when(mockResultSet.getDouble("total")).thenReturn(99.0);
        when(mockResultSet.getString("situacao")).thenReturn("aberto");

        List<Pedido> lista = pedidoDAO.buscarTodos();

        assertEquals(1, lista.size());
        assertEquals(1, lista.get(0).getPedidoId());
        assertEquals("Carlos", lista.get(0).getClienteNome());
    }

    @Test
    void testBuscarTodosComItens() throws SQLException {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("pedidoId")).thenReturn(5);
        when(mockResultSet.getInt("clienteId")).thenReturn(3);
        when(mockResultSet.getString("clienteNome")).thenReturn("Carlos");
        when(mockResultSet.getTimestamp("data")).thenReturn(agora);
        when(mockResultSet.getDouble("total")).thenReturn(99.0);
        when(mockResultSet.getString("situacao")).thenReturn("aberto");

        ItemPedido item = new ItemPedido(5, 1, 1.0, 99.0);
        when(mockItemPedidoDAO.buscarPorPedidoId(5)).thenReturn(Collections.singletonList(item));

        List<Pedido> lista = pedidoDAO.buscarTodosComItens();

        assertEquals(1, lista.size());
        assertEquals(1, lista.get(0).getItens().size());
        assertEquals(item, lista.get(0).getItens().get(0));
    }
}
