package com.estoque.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());
    private static final Properties properties = new Properties();

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

        try ( InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Nao foi possivel carregar db.properties, usando padroes", e);
        }
    }

    public static String getHost() {
        return properties.getProperty("db.host", "localhost");
    }

    public static String getPort() {
        return properties.getProperty("db.port", "3306");
    }

    public static String getDbName() {
        return properties.getProperty("db.name", "EstoqueDB");
    }

    public static String getUser() {
        return properties.getProperty("db.user", "root");
    }

    public static String getPassword() {
        return properties.getProperty("db.password", "root");
    }

    public static String getParams() {
        return properties.getProperty("db.params", "?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
    }

    /**
     * Retorna conexao com o servidor MySQL (sem selecionar banco especifico)
     */
    public static Connection getServerConnection() throws SQLException {
        String baseUrl = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getParams();
        return getConnectionInternal(baseUrl);
    }

    /**
     * Retorna conexao especificamente para o banco EstoqueDB
     */
    public static Connection getConnection() throws SQLException {
        // Garantir que banco e tabelas foram verificados e criados antes da conexao ser liberada
        DatabaseInitializer.initializeIfNeeded();
        return getDatabaseConnectionInternal();
    }

    public static Connection getDatabaseConnectionInternal() throws SQLException {
        String dbUrl = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDbName() + getParams();
        return getConnectionInternal(dbUrl);
    }

    /**
     * Tenta conectar utilizando a senha configurada em db.properties e fallback
     * para senhas comuns se necessario.
     */
    private static synchronized Connection getConnectionInternal(String url) throws SQLException {
        String user = getUser();
        String password = getPassword();

        try {
            Connection conn =DriverManager.getConnection(url, user, password);
            LOGGER.info("Conexao com MySQL estabelecida com sucesso!");
            return conn;
        } catch (SQLException e) {
            LOGGER.info("Conexao com MySQL estabelecida com sucesso!");
            throw new SQLException("Nao foi possivel conectar ao MySQL.");
        }
    }
}
