package com.estoque.configuracao;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BancoDeDadosConfiguracaoTest {

    @Test
    void testPropriedadesPadraoEConstrutor() {
        BancoDeDadosConfiguracao config = new BancoDeDadosConfiguracao();
        assertNotNull(config);

        assertNotNull(BancoDeDadosConfiguracao.obterHost());
        assertNotNull(BancoDeDadosConfiguracao.obterPorta());
        assertNotNull(BancoDeDadosConfiguracao.obterNomeBanco());
        assertNotNull(BancoDeDadosConfiguracao.obterUsuario());
        assertNotNull(BancoDeDadosConfiguracao.obterSenha());
        assertNotNull(BancoDeDadosConfiguracao.obterParametros());
    }

    @Test
    void testObterConexoesSucesso() throws SQLException {
        Connection mockConn = mock(Connection.class);

        try (MockedStatic<DriverManager> mockedDriver = mockStatic(DriverManager.class);
             MockedStatic<BancoDeDadosInicializador> mockedInit = mockStatic(BancoDeDadosInicializador.class)) {

            mockedDriver.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);
            mockedInit.when(BancoDeDadosInicializador::inicializarSeNecessario).thenAnswer(inv -> null);

            Connection connServidor = BancoDeDadosConfiguracao.obterConexaoServidor();
            assertNotNull(connServidor);
            assertSame(mockConn, connServidor);

            Connection connBancoInterna = BancoDeDadosConfiguracao.obterConexaoBancoInterna();
            assertNotNull(connBancoInterna);
            assertSame(mockConn, connBancoInterna);

            Connection connPrincipal = BancoDeDadosConfiguracao.obterConexao();
            assertNotNull(connPrincipal);
            assertSame(mockConn, connPrincipal);
            mockedInit.verify(BancoDeDadosInicializador::inicializarSeNecessario);
        }
    }

    @Test
    void testObterConexaoFalhaLancaSQLException() {
        try (MockedStatic<DriverManager> mockedDriver = mockStatic(DriverManager.class)) {
            mockedDriver.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenAnswer(inv -> {
                        throw new SQLException("Driver failure");
                    });

            SQLException ex = assertThrows(SQLException.class, BancoDeDadosConfiguracao::obterConexaoServidor);
            assertEquals("Nao foi possivel conectar ao MySQL.", ex.getMessage());
        }
    }

    @Test
    void testSystemProperties() {
        System.setProperty("db.host", "meu-host");
        System.setProperty("db.port", "3307");
        System.setProperty("db.name", "BancoTeste");
        System.setProperty("db.user", "usuario_teste");
        System.setProperty("db.password", "senha_teste");
        System.setProperty("db.params", "?param=1");

        try {
            assertEquals("meu-host", BancoDeDadosConfiguracao.obterHost());
            assertEquals("3307", BancoDeDadosConfiguracao.obterPorta());
            assertEquals("BancoTeste", BancoDeDadosConfiguracao.obterNomeBanco());
            assertEquals("usuario_teste", BancoDeDadosConfiguracao.obterUsuario());
            assertEquals("senha_teste", BancoDeDadosConfiguracao.obterSenha());
            assertEquals("?param=1", BancoDeDadosConfiguracao.obterParametros());
        } finally {
            System.clearProperty("db.host");
            System.clearProperty("db.port");
            System.clearProperty("db.name");
            System.clearProperty("db.user");
            System.clearProperty("db.password");
            System.clearProperty("db.params");
        }
    }
}
