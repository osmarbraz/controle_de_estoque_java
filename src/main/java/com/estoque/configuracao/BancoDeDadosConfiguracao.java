package com.estoque.configuracao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável por gerenciar a lógica de BancoDeDadosConfiguracao.
 */
public class BancoDeDadosConfiguracao {

    private static final Logger LOGGER = Logger.getLogger(BancoDeDadosConfiguracao.class.getName());
    private static final Properties propriedades = new Properties();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Driver MySQL nao encontrado no classpath", ex);
            }
        }

        try (InputStream input = BancoDeDadosConfiguracao.class.getClassLoader()
                .getResourceAsStream("db.propriedades")) {
            if (input != null) {
                propriedades.load(input);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Nao foi possivel carregar db.propriedades, usando padroes", e);
        }
    }

    /**
     * Obtém o valor de Host.
     * 
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterHost() {
        return propriedades.getProperty("db.host", "localhost");
    }

    /**
     * Obtém o valor de Port.
     * 
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterPorta() {
        return propriedades.getProperty("db.port", "3306");
    }

    /**
     * Obtém o valor de DbName.
     * 
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterNomeBanco() {
        return propriedades.getProperty("db.name", "EstoqueDB");
    }

    /**
     * Obtém o valor de User.
     * 
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterUsuario() {
        return propriedades.getProperty("db.user", "root");
    }

    /**
     * Obtém o valor de Password.
     * 
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterSenha() {
        return propriedades.getProperty("db.password", "root");
    }

    /**
     * Obtém o valor de Params.
     * 
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterParametros() {
        return propriedades.getProperty("db.params",
                "?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
    }

    /**
     * Retorna conexao com o servidor MySQL (sem selecionar banco especifico)
     */
    /**
     * Obtém o valor de ServerConnection.
     * 
     * @return Retorna o resultado da operação (static Connection)
     */
    public static Connection obterConexaoServidor() throws SQLException {
        String urlBase = "jdbc:mysql://" + obterHost() + ":" + obterPorta() + "/" + obterParametros();
        return obterConexaoInterna(urlBase);
    }

    /**
     * Retorna conexao especificamente para o banco EstoqueDB
     */
    /**
     * Obtém uma conexão válida com o banco de dados.
     * 
     * @return Retorna o resultado da operação (static Connection)
     */
    public static Connection obterConexao() throws SQLException {
        // Garantir que banco e tabelas foram verificados e criados antes da conexao ser
        // liberada
        BancoDeDadosInicializador.inicializarSeNecessario();
        return obterConexaoBancoInterna();
    }

    /**
     * Obtém o valor de DatabaseConnectionInternal.
     * 
     * @return Retorna o resultado da operação (static Connection)
     */
    public static Connection obterConexaoBancoInterna() throws SQLException {
        String urlBanco = "jdbc:mysql://" + obterHost() + ":" + obterPorta() + "/" + obterNomeBanco()
                + obterParametros();
        return obterConexaoInterna(urlBanco);
    }

    /**
     * Tenta conectar utilizando a senha configurada em db.propriedades e fallback
     * para senhas comuns se necessario.
     */
    private static synchronized Connection obterConexaoInterna(String url) throws SQLException {
        String user = obterUsuario();
        String password = obterSenha();

        try {
            Connection conexao = DriverManager.getConnection(url, user, password);
            // LOGGER.info("Conexao com MySQL estabelecida com sucesso!");
            return conexao;
        } catch (SQLException e) {
            LOGGER.info("Problema na conexao com MySQL!");
            throw new SQLException("Nao foi possivel conectar ao MySQL.");
        }
    }
}
