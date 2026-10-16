package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Produto;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ProdutoDAOTest {

    private MockedStatic<BancoDeDadosConfiguracao> mockedConfig;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private ProdutoDAO produtoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockedConfig = mockStatic(BancoDeDadosConfiguracao.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedConfig.when(BancoDeDadosConfiguracao::obterConexao).thenReturn(mockConnection);
        produtoDAO = new ProdutoDAO();
    }

    @AfterEach
    void tearDown() {
        mockedConfig.close();
    }

    @Test
    void testInserirComDataCadastroSucesso() throws SQLException {
        Date data = new Date();
        Produto p = new Produto(null, "Mouse", "UN", 20.0, 45.0, 100.0, 1, data);
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(55);

        boolean ok = produtoDAO.inserir(p);

        assertTrue(ok);
        assertEquals(55, p.getProdutoId());
        verify(mockStatement).setString(1, "Mouse");
        verify(mockStatement).setString(2, "UN");
        verify(mockStatement).setDouble(3, 20.0);
        verify(mockStatement).setDouble(4, 45.0);
        verify(mockStatement).setDouble(5, 100.0);
        verify(mockStatement).setInt(6, 1);
        verify(mockStatement).setTimestamp(eq(7), eq(new Timestamp(data.getTime())));
    }

    @Test
    void testInserirSemDataCadastroSucesso() throws SQLException {
        Produto p = new Produto();
        p.setDescricao("Teclado");
        p.setUnidade("UN");
        p.setPrecoCusto(50.0);
        p.setPrecoVenda(99.0);
        p.setQuantidade(10.0);
        p.setCategoriaId(2);

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean ok = produtoDAO.inserir(p);

        assertTrue(ok);
        assertNull(p.getProdutoId());
        verify(mockStatement).setTimestamp(eq(7), any(Timestamp.class));
    }

    @Test
    void testInserirFalha() throws SQLException {
        Produto p = new Produto();
        p.setCategoriaId(1);
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        boolean ok = produtoDAO.inserir(p);

        assertFalse(ok);
    }

    @Test
    void testAtualizarSucessoEFalha() throws SQLException {
        Produto p = new Produto(8, "Cadeira", "UN", 150.0, 250.0, 5.0, 3, new Date());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(produtoDAO.atualizar(p));
        assertFalse(produtoDAO.atualizar(p));

        verify(mockStatement, times(2)).setString(1, "Cadeira");
        verify(mockStatement, times(2)).setString(2, "UN");
        verify(mockStatement, times(2)).setDouble(3, 150.0);
        verify(mockStatement, times(2)).setDouble(4, 250.0);
        verify(mockStatement, times(2)).setDouble(5, 5.0);
        verify(mockStatement, times(2)).setInt(6, 3);
        verify(mockStatement, times(2)).setInt(7, 8);
    }

    @Test
    void testAtualizarEstoqueSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(produtoDAO.atualizarEstoque(8, -2.0));
        assertFalse(produtoDAO.atualizarEstoque(8, 5.0));

        verify(mockStatement).setDouble(1, -2.0);
        verify(mockStatement, times(2)).setInt(2, 8);
        verify(mockStatement).setDouble(1, 5.0);
    }

    @Test
    void testExcluirSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(produtoDAO.excluir(8));
        assertFalse(produtoDAO.excluir(8));

        verify(mockStatement, times(2)).setInt(1, 8);
    }

    @Test
    void testBuscarPorIdEncontrado() throws SQLException {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("produtoId")).thenReturn(12);
        when(mockResultSet.getString("descricao")).thenReturn("Monitor");
        when(mockResultSet.getString("unidade")).thenReturn("UN");
        when(mockResultSet.getDouble("precoCusto")).thenReturn(300.0);
        when(mockResultSet.getDouble("precoVenda")).thenReturn(600.0);
        when(mockResultSet.getDouble("quantidade")).thenReturn(15.0);
        when(mockResultSet.getInt("categoriaId")).thenReturn(2);
        when(mockResultSet.getString("categoriaNome")).thenReturn("Eletrônicos");
        when(mockResultSet.getTimestamp("dataCadastro")).thenReturn(agora);

        Produto p = produtoDAO.buscarPorId(12);

        assertNotNull(p);
        assertEquals(12, p.getProdutoId());
        assertEquals("Monitor", p.getDescricao());
        assertEquals("UN", p.getUnidade());
        assertEquals(300.0, p.getPrecoCusto(), 0.001);
        assertEquals(600.0, p.getPrecoVenda(), 0.001);
        assertEquals(15.0, p.getQuantidade(), 0.001);
        assertEquals(2, p.getCategoriaId());
        assertEquals("Eletrônicos", p.getCategoriaNome());
        assertEquals(agora, p.getDataCadastro());
    }

    @Test
    void testBuscarPorIdNaoEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Produto p = produtoDAO.buscarPorId(999);

        assertNull(p);
    }

    @Test
    void testBuscarTodos() throws SQLException {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("produtoId")).thenReturn(1);
        when(mockResultSet.getString("descricao")).thenReturn("Cabo HDMI");
        when(mockResultSet.getString("unidade")).thenReturn("UN");
        when(mockResultSet.getDouble("precoCusto")).thenReturn(10.0);
        when(mockResultSet.getDouble("precoVenda")).thenReturn(25.0);
        when(mockResultSet.getDouble("quantidade")).thenReturn(30.0);
        when(mockResultSet.getInt("categoriaId")).thenReturn(1);
        when(mockResultSet.getString("categoriaNome")).thenReturn("Informática");
        when(mockResultSet.getTimestamp("dataCadastro")).thenReturn(agora);

        List<Produto> lista = produtoDAO.buscarTodos();

        assertEquals(1, lista.size());
        assertEquals("Cabo HDMI", lista.get(0).getDescricao());
    }

    @Test
    void testBuscarPorDescricaoComTermo() throws SQLException {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("produtoId")).thenReturn(1);
        when(mockResultSet.getString("descricao")).thenReturn("Mousepad Gamer");
        when(mockResultSet.getString("unidade")).thenReturn("UN");
        when(mockResultSet.getDouble("precoCusto")).thenReturn(15.0);
        when(mockResultSet.getDouble("precoVenda")).thenReturn(40.0);
        when(mockResultSet.getDouble("quantidade")).thenReturn(20.0);
        when(mockResultSet.getInt("categoriaId")).thenReturn(1);
        when(mockResultSet.getString("categoriaNome")).thenReturn("Informática");
        when(mockResultSet.getTimestamp("dataCadastro")).thenReturn(agora);

        List<Produto> lista = produtoDAO.buscarPorDescricao(" Mouse ");

        assertEquals(1, lista.size());
        assertEquals("Mousepad Gamer", lista.get(0).getDescricao());
        verify(mockStatement).setString(1, "%Mouse%");
    }

    @Test
    void testBuscarPorDescricaoNull() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        List<Produto> lista = produtoDAO.buscarPorDescricao(null);

        assertTrue(lista.isEmpty());
        verify(mockStatement).setString(1, "%%");
    }
}
