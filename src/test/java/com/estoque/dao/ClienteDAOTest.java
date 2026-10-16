package com.estoque.dao;

import com.estoque.configuracao.BancoDeDadosConfiguracao;
import com.estoque.modelo.Cliente;
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

public class ClienteDAOTest {

    private MockedStatic<BancoDeDadosConfiguracao> mockedConfig;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private ClienteDAO clienteDAO;

    @BeforeEach
    void setUp() throws SQLException {
        mockedConfig = mockStatic(BancoDeDadosConfiguracao.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedConfig.when(BancoDeDadosConfiguracao::obterConexao).thenReturn(mockConnection);
        clienteDAO = new ClienteDAO();
    }

    @AfterEach
    void tearDown() {
        mockedConfig.close();
    }

    @Test
    void testInserirComDataCadastroSucesso() throws SQLException {
        Date data = new Date();
        Cliente c = new Cliente(null, "João", "123", "Rua A", "10", "Bairro B", 1, data);
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(101);

        boolean ok = clienteDAO.inserir(c);

        assertTrue(ok);
        assertEquals(101, c.getClienteId());
        verify(mockStatement).setString(1, "João");
        verify(mockStatement).setString(2, "123");
        verify(mockStatement).setString(3, "Rua A");
        verify(mockStatement).setString(4, "10");
        verify(mockStatement).setString(5, "Bairro B");
        verify(mockStatement).setInt(6, 1);
        verify(mockStatement).setTimestamp(eq(7), eq(new Timestamp(data.getTime())));
    }

    @Test
    void testInserirSemDataCadastroSucesso() throws SQLException {
        Cliente c = new Cliente();
        c.setNome("Maria");
        c.setCpf("456");
        c.setRua("Rua B");
        c.setNumero("20");
        c.setBairro("Centro");
        c.setCidadeId(2);
        // dataCadastro is null to test branch System.currentTimeMillis()

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);
        when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean ok = clienteDAO.inserir(c);

        assertTrue(ok);
        assertNull(c.getClienteId());
        verify(mockStatement).setTimestamp(eq(7), any(Timestamp.class));
    }

    @Test
    void testInserirFalha() throws SQLException {
        Cliente c = new Cliente();
        c.setCidadeId(1);
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(0);

        boolean ok = clienteDAO.inserir(c);

        assertFalse(ok);
    }

    @Test
    void testAtualizarSucessoEFalha() throws SQLException {
        Cliente c = new Cliente(5, "Carlos", "789", "Rua C", "30", "Bairro C", 3, new Date());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(clienteDAO.atualizar(c));
        assertFalse(clienteDAO.atualizar(c));

        verify(mockStatement, times(2)).setString(1, "Carlos");
        verify(mockStatement, times(2)).setString(2, "789");
        verify(mockStatement, times(2)).setString(3, "Rua C");
        verify(mockStatement, times(2)).setString(4, "30");
        verify(mockStatement, times(2)).setString(5, "Bairro C");
        verify(mockStatement, times(2)).setInt(6, 3);
        verify(mockStatement, times(2)).setInt(7, 5);
    }

    @Test
    void testExcluirSucessoEFalha() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1).thenReturn(0);

        assertTrue(clienteDAO.excluir(5));
        assertFalse(clienteDAO.excluir(5));

        verify(mockStatement, times(2)).setInt(1, 5);
    }

    @Test
    void testBuscarPorIdEncontrado() throws SQLException {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("clienteId")).thenReturn(7);
        when(mockResultSet.getString("Nome")).thenReturn("Ana");
        when(mockResultSet.getString("CPF")).thenReturn("111");
        when(mockResultSet.getString("rua")).thenReturn("Rua D");
        when(mockResultSet.getString("numero")).thenReturn("40");
        when(mockResultSet.getString("bairro")).thenReturn("Bairro D");
        when(mockResultSet.getInt("cidadeId")).thenReturn(2);
        when(mockResultSet.getString("cidadeNome")).thenReturn("Curitiba");
        when(mockResultSet.getString("cidadeUf")).thenReturn("PR");
        when(mockResultSet.getTimestamp("dataCadastro")).thenReturn(agora);

        Cliente c = clienteDAO.buscarPorId(7);

        assertNotNull(c);
        assertEquals(7, c.getClienteId());
        assertEquals("Ana", c.getNome());
        assertEquals("111", c.getCpf());
        assertEquals("Rua D", c.getRua());
        assertEquals("40", c.getNumero());
        assertEquals("Bairro D", c.getBairro());
        assertEquals(2, c.getCidadeId());
        assertEquals("Curitiba", c.getCidadeNome());
        assertEquals("PR", c.getCidadeUf());
        assertEquals(agora, c.getDataCadastro());
    }

    @Test
    void testBuscarPorIdNaoEncontrado() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        Cliente c = clienteDAO.buscarPorId(999);

        assertNull(c);
    }

    @Test
    void testBuscarTodos() throws SQLException {
        Timestamp agora = new Timestamp(System.currentTimeMillis());
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("clienteId")).thenReturn(1);
        when(mockResultSet.getString("Nome")).thenReturn("Pedro");
        when(mockResultSet.getString("CPF")).thenReturn("222");
        when(mockResultSet.getString("rua")).thenReturn("Rua E");
        when(mockResultSet.getString("numero")).thenReturn("50");
        when(mockResultSet.getString("bairro")).thenReturn("Bairro E");
        when(mockResultSet.getInt("cidadeId")).thenReturn(1);
        when(mockResultSet.getString("cidadeNome")).thenReturn("São Paulo");
        when(mockResultSet.getString("cidadeUf")).thenReturn("SP");
        when(mockResultSet.getTimestamp("dataCadastro")).thenReturn(agora);

        List<Cliente> lista = clienteDAO.buscarTodos();

        assertEquals(1, lista.size());
        assertEquals("Pedro", lista.get(0).getNome());
        assertEquals("São Paulo", lista.get(0).getCidadeNome());
    }
}
