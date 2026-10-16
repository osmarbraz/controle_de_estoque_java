package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Categoria;
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
import static org.mockito.Mockito.*;

public class CategoriaDAOTest {

    private MockedStatic<BancoDeDadosConfiguracao> mockedConfig;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private CategoriaDAO categoriaDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockedConfig = mockStatic(BancoDeDadosConfiguracao.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedConfig.when(BancoDeDadosConfiguracao::obterConexao).thenReturn(mockConnection);
        categoriaDAO = new CategoriaDAO();
    }

    @AfterEach
    void tearDown() {
        mockedConfig.close();
    }

    @Test
    void testInserirSucesso() throws SQLException {
        Categoria cat = new Categoria("Informática");
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(42);

        boolean resultado = categoriaDAO.inserir(cat);

        assertTrue(resultado);
        assertEquals(42, cat.getCategoriaId());
        verify(mockStatement).setString(1, "Informática");
    }

    @Test
    void testInserirFalhaLinhasZero() throws SQLException {
        Categoria cat = new Categoria("Informática");
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        boolean resultado = categoriaDAO.inserir(cat);

        assertFalse(resultado);
        assertNull(cat.getCategoriaId());
    }

    @Test
    void testInserirSemGeneratedKey() throws SQLException {
        Categoria cat = new Categoria("Informática");
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean resultado = categoriaDAO.inserir(cat);

        assertTrue(resultado);
        assertNull(cat.getCategoriaId());
    }

    @Test
    void testAtualizarSucessoEFalha() throws SQLException {
        Categoria cat = new Categoria(5, "Alimentos");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        boolean res1 = categoriaDAO.atualizar(cat);
        assertTrue(res1);

        boolean res2 = categoriaDAO.atualizar(cat);
        assertFalse(res2);

        verify(mockStatement, times(2)).setString(1, "Alimentos");
        verify(mockStatement, times(2)).setInt(2, 5);
    }

    @Test
    void testExcluirSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        boolean res1 = categoriaDAO.excluir(5);
        assertTrue(res1);

        boolean res2 = categoriaDAO.excluir(5);
        assertFalse(res2);

        verify(mockStatement, times(2)).setInt(1, 5);
    }

    @Test
    void testBuscarPorIdEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("categoriaId")).thenReturn(7);
        when(mockResultSet.getString("nome")).thenReturn("Bebidas");

        Categoria cat = categoriaDAO.buscarPorId(7);

        assertNotNull(cat);
        assertEquals(7, cat.getCategoriaId());
        assertEquals("Bebidas", cat.getNome());
    }

    @Test
    void testBuscarPorIdNaoEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Categoria cat = categoriaDAO.buscarPorId(99);

        assertNull(cat);
    }

    @Test
    void testBuscarTodos() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("categoriaId")).thenReturn(1, 2);
        when(mockResultSet.getString("nome")).thenReturn("Cat 1", "Cat 2");

        List<Categoria> lista = categoriaDAO.buscarTodos();

        assertEquals(2, lista.size());
        assertEquals("Cat 1", lista.get(0).getNome());
        assertEquals("Cat 2", lista.get(1).getNome());
    }
}
