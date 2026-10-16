package com.estoque.configuracao;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BancoDeDadosConfiguracaoTest {

    @Test
    void deveTentarDriverLegadoQuandoDriverAtualNaoForEncontrado() {
        // Variáveis
        List<String> driversConsultados = new ArrayList<>();

        // Entrada e Processamento
        BancoDeDadosConfiguracao.carregarDriver(nomeDriver -> {
            driversConsultados.add(nomeDriver);

            if (nomeDriver.equals("com.mysql.cj.jdbc.Driver")) {
                throw new ClassNotFoundException("Driver atual nao encontrado");
            }
        });

        // Saída
        assertEquals(2, driversConsultados.size());
        assertEquals("com.mysql.cj.jdbc.Driver",driversConsultados.get(0));
        assertEquals("com.mysql.jdbc.Driver",driversConsultados.get(1));
    }

    @Test
    void deveRegistrarErroQuandoNenhumDriverMySQLForEncontrado() {
        // Variáveis
        List<String> driversConsultados = new ArrayList<>();
        // Entrada e Processamento
        BancoDeDadosConfiguracao.carregarDriver(nomeDriver -> {
            driversConsultados.add(nomeDriver);
            throw new ClassNotFoundException(
                    "Driver nao encontrado: " + nomeDriver
            );
        });
        // Saída
        assertEquals(2, driversConsultados.size());
        assertEquals("com.mysql.cj.jdbc.Driver", driversConsultados.get(0));
        assertEquals("com.mysql.jdbc.Driver", driversConsultados.get(1));
    }

    @Test
    void deveTratarErrosAoCarregarEFecharArquivoDePropriedades() {
        // Entrada: cria um fluxo que falha ao ler e ao fechar.
        InputStream inputComErro = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Erro simulado durante a leitura");
            }

            @Override
            public void close() throws IOException {
                throw new IOException("Erro simulado durante o fechamento");
            }
        };
        // Processamento e saída: os erros devem ser tratados internamente.
        assertDoesNotThrow(() -> BancoDeDadosConfiguracao.carregarPropriedades(inputComErro));
    }

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
    void testObterHostComVariavelDeAmbiente() {
        BancoDeDadosConfiguracao.definirFornecedorAmbiente(nome -> "DB_HOST".equals(nome) ? "  localhost  " : null);
        try {
            assertEquals("localhost", BancoDeDadosConfiguracao.obterHost());
        } finally {
            BancoDeDadosConfiguracao.definirFornecedorAmbiente(System::getenv);
        }
    }

    @Test
    void testObterPortaComVariavelDeAmbiente() {
        BancoDeDadosConfiguracao.definirFornecedorAmbiente(nome -> "DB_PORT".equals(nome) ? "  3306  " : null);
        try {
            assertEquals("3306", BancoDeDadosConfiguracao.obterPorta());
        } finally {
            BancoDeDadosConfiguracao.definirFornecedorAmbiente(System::getenv);
        }
    }

    @Test
    void testObterNomeBancoComVariavelDeAmbiente() {
        BancoDeDadosConfiguracao.definirFornecedorAmbiente(nome -> "DB_NAME".equals(nome) ? "  EstoqueDB  " : null);
        try {
            assertEquals("EstoqueDB", BancoDeDadosConfiguracao.obterNomeBanco());
        } finally {
            BancoDeDadosConfiguracao.definirFornecedorAmbiente(System::getenv);
        }
    }

    @Test
    void testObterUsuarioComVariavelDeAmbiente() {
        BancoDeDadosConfiguracao.definirFornecedorAmbiente(nome -> "DB_USER".equals(nome) ? "  root  " : null);
        try {
            assertEquals("root", BancoDeDadosConfiguracao.obterUsuario());
        } finally {
            BancoDeDadosConfiguracao.definirFornecedorAmbiente(System::getenv);
        }
    }

    @Test
    void testObterSenhaComVariavelDeAmbiente() {
        BancoDeDadosConfiguracao.definirFornecedorAmbiente(nome -> "DB_PASSWORD".equals(nome) ? "  root  " : null);
        try {
            assertEquals("root", BancoDeDadosConfiguracao.obterSenha());
        } finally {
            BancoDeDadosConfiguracao.definirFornecedorAmbiente(System::getenv);
        }
    }

    @Test
    void testObterParametrosComVariavelDeAmbiente() {
        BancoDeDadosConfiguracao.definirFornecedorAmbiente(nome -> "DB_PARAMS".equals(nome) ? "  ?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC  " : null);
        try {
            assertEquals("?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", BancoDeDadosConfiguracao.obterParametros());
        } finally {
            BancoDeDadosConfiguracao.definirFornecedorAmbiente(System::getenv);
        }
    }

    @Test
    void testObterConexoesSucesso() throws SQLException {
        Connection mockConn = mock(Connection.class);

        try ( MockedStatic<DriverManager> mockedDriver = mockStatic(DriverManager.class);  MockedStatic<BancoDeDadosInicializador> mockedInit = mockStatic(BancoDeDadosInicializador.class)) {

            mockedDriver.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mockConn);
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
        try ( MockedStatic<DriverManager> mockedDriver = mockStatic(DriverManager.class)) {
            mockedDriver.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenAnswer(inv -> {
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
