package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Cidade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CidadeDAOTest {

    private MockedStatic<BancoDeDadosConfiguracao> mockedConfig;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private CidadeDAO cidadeDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockedConfig = mockStatic(BancoDeDadosConfiguracao.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedConfig.when(BancoDeDadosConfiguracao::obterConexao).thenReturn(mockConnection);
        cidadeDAO = new CidadeDAO();
    }

    @AfterEach
    void tearDown() {
        mockedConfig.close();
    }

    @Test
    void testInserirSucesso() throws SQLException {
        Cidade c = new Cidade("Campinas", "SP");
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(10);

        boolean ok = cidadeDAO.inserir(c);

        assertTrue(ok);
        assertEquals(10, c.getCidadeId());
        verify(mockStatement).setString(1, "Campinas");
        verify(mockStatement).setString(2, "SP");
    }

    @Test
    void testInserirSemChaveGerada() throws SQLException {
        Cidade c = new Cidade("Santos", "SP");
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean ok = cidadeDAO.inserir(c);

        assertTrue(ok);
        assertNull(c.getCidadeId());
    }

    @Test
    void testInserirFalha() throws SQLException {
        Cidade c = new Cidade("Santos", "SP");
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        boolean ok = cidadeDAO.inserir(c);

        assertFalse(ok);
    }

    @Test
    void testAtualizarSucessoEFalha() throws SQLException {
        Cidade c = new Cidade(2, "Niterói", "RJ");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(cidadeDAO.atualizar(c));
        assertFalse(cidadeDAO.atualizar(c));

        verify(mockStatement, times(2)).setString(1, "Niterói");
        verify(mockStatement, times(2)).setString(2, "RJ");
        verify(mockStatement, times(2)).setInt(3, 2);
    }

    @Test
    void testExcluirSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(cidadeDAO.excluir(2));
        assertFalse(cidadeDAO.excluir(2));

        verify(mockStatement, times(2)).setInt(1, 2);
    }

    @Test
    void testBuscarPorIdEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("cidadeId")).thenReturn(5);
        when(mockResultSet.getString("nome")).thenReturn("Belo Horizonte");
        when(mockResultSet.getString("uf")).thenReturn("MG");

        Cidade c = cidadeDAO.buscarPorId(5);

        assertNotNull(c);
        assertEquals(5, c.getCidadeId());
        assertEquals("Belo Horizonte", c.getNome());
        assertEquals("MG", c.getUf());
    }

    @Test
    void testBuscarPorIdNaoEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Cidade c = cidadeDAO.buscarPorId(999);

        assertNull(c);
    }

    @Test
    void testBuscarTodos() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("cidadeId")).thenReturn(1, 2);
        when(mockResultSet.getString("nome")).thenReturn("Curitiba", "Florianópolis");
        when(mockResultSet.getString("uf")).thenReturn("PR", "SC");

        List<Cidade> lista = cidadeDAO.buscarTodos();

        assertEquals(2, lista.size());
        assertEquals("Curitiba", lista.get(0).getNome());
        assertEquals("Florianópolis", lista.get(1).getNome());
    }
}
