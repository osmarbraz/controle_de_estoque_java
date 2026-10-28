package com.estoque.configuracao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BancoDeDadosInicializadorTest {

    private MockedStatic<BancoDeDadosConfiguracao> mockedConfig;
    private Connection mockConnServidor;
    private Connection mockConnBanco;
    private Statement mockStmtServidor;
    private Statement mockStmtBanco;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws Exception {
        resetState(false);
        mockedConfig = mockStatic(BancoDeDadosConfiguracao.class);
        mockConnServidor = mock(Connection.class);
        mockConnBanco = mock(Connection.class);
        mockStmtServidor = mock(Statement.class);
        mockStmtBanco = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnServidor.createStatement()).thenReturn(mockStmtServidor);
        when(mockConnBanco.createStatement()).thenReturn(mockStmtBanco);
        mockedConfig.when(BancoDeDadosConfiguracao::obterHost).thenReturn("localhost");
        mockedConfig.when(BancoDeDadosConfiguracao::obterPorta).thenReturn("3306");
        mockedConfig.when(BancoDeDadosConfiguracao::obterNomeBanco).thenReturn("EstoqueDB");
        mockedConfig.when(BancoDeDadosConfiguracao::obterConexaoServidor).thenReturn(mockConnServidor);
        mockedConfig.when(BancoDeDadosConfiguracao::obterConexaoBancoInterna).thenReturn(mockConnBanco);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockedConfig.close();
        resetState(false);
    }

    private void resetState(boolean state) throws Exception {
        Field f = BancoDeDadosInicializador.class.getDeclaredField("isInitialized");
        f.setAccessible(true);
        f.set(null, state);
    }

    @Test
    void testConstrutor() {
        BancoDeDadosInicializador init = new BancoDeDadosInicializador();
        assertNotNull(init);
    }

    @Test
    void testJaInicializadoNaoFazNada() throws Exception {
        resetState(true);

        BancoDeDadosInicializador.inicializarSeNecessario();

        mockedConfig.verify(BancoDeDadosConfiguracao::obterConexaoServidor, never());
        mockedConfig.verify(BancoDeDadosConfiguracao::obterConexaoBancoInterna, never());
    }

    @Test
    void testInicializacaoCompletaComTabelaVazia() throws Exception {
        when(mockStmtBanco.executeQuery("SELECT COUNT(*) FROM Cidade")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(0);

        BancoDeDadosInicializador.inicializarSeNecessario();

        verify(mockStmtServidor).executeUpdate(contains("CREATE DATABASE IF NOT EXISTS EstoqueDB"));
        verify(mockStmtBanco).executeUpdate(contains("CREATE TABLE IF NOT EXISTS Cidade"));
        verify(mockStmtBanco).executeUpdate(contains("CREATE TABLE IF NOT EXISTS Categoria"));
        verify(mockStmtBanco).executeUpdate(contains("CREATE TABLE IF NOT EXISTS Cliente"));
        verify(mockStmtBanco).executeUpdate(contains("CREATE TABLE IF NOT EXISTS Produto"));
        verify(mockStmtBanco).executeUpdate(contains("CREATE TABLE IF NOT EXISTS Pedido"));
        verify(mockStmtBanco).executeUpdate(contains("CREATE TABLE IF NOT EXISTS ItemPedido"));
        verify(mockStmtBanco).executeUpdate(contains("INSERT INTO Cidade"));
        verify(mockStmtBanco).executeUpdate(contains("INSERT INTO Categoria"));
        verify(mockStmtBanco).executeUpdate(contains("INSERT INTO Cliente"));
        verify(mockStmtBanco).executeUpdate(contains("INSERT INTO Produto"));
        // Deve inserir 2 pedidos (aberto e fechado)
        verify(mockStmtBanco, times(2)).executeUpdate(contains("INSERT INTO Pedido"));
        // Deve inserir itens de ambos os pedidos
        verify(mockStmtBanco, times(2)).executeUpdate(contains("INSERT INTO ItemPedido"));
        // Deve ajustar o estoque dos 3 produtos do pedido fechado
        verify(mockStmtBanco, times(3)).executeUpdate(contains("UPDATE Produto SET quantidade"));
        // O pedido 2 deve estar fechado
        verify(mockStmtBanco).executeUpdate(contains("fechado"));
    }

    @Test
    void testInicializacaoComTabelaJaPopulada() throws Exception {
        when(mockStmtBanco.executeQuery("SELECT COUNT(*) FROM Cidade")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(4);

        BancoDeDadosInicializador.inicializarSeNecessario();

        verify(mockStmtBanco, never()).executeUpdate(contains("INSERT INTO Cidade"));
    }

    @Test
    void testInicializacaoComFalhaAoPopularDadosTratada() throws Exception {
        when(mockStmtBanco.executeQuery("SELECT COUNT(*) FROM Cidade")).thenThrow(new SQLException("Erro ao contar"));

        assertDoesNotThrow(BancoDeDadosInicializador::inicializarSeNecessario);
    }

    @Test
    void testInicializacaoFalhaNoBancoLancaRuntimeException() throws Exception {
        when(mockStmtServidor.executeUpdate(anyString())).thenThrow(new SQLException("Erro de conexao"));

        RuntimeException ex = assertThrows(RuntimeException.class, BancoDeDadosInicializador::inicializarSeNecessario);
        assertTrue(ex.getMessage().contains("Erro ao inicializar banco de dados"));
    }
}
