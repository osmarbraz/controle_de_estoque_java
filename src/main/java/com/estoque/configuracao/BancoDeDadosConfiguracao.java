package com.estoque.configuracao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.function.Function;

/**
 * Classe responsável por gerenciar a lógica de BancoDeDadosConfiguracao.
 */
public class BancoDeDadosConfiguracao {

    private static final Logger LOGGER = Logger.getLogger(BancoDeDadosConfiguracao.class.getName());
    private static final Properties propriedades = new Properties();

    // Permite substituir a leitura das variáveis de ambiente nos testes.
    private static Function<String, String> fornecedorAmbiente = System::getenv;

    @FunctionalInterface
    interface CarregadorDriver {
        void carregar(String nomeDriver) throws ClassNotFoundException;
    }

    static void carregarDriver(CarregadorDriver carregador) {
        try {
            carregador.carregar("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                carregador.carregar("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Driver MySQL nao encontrado no classpath", ex);
            }
        }
    }

    static void carregarPropriedades(InputStream input) {
        if (input != null) {
            try {
                propriedades.load(input);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Nao foi possivel carregar propriedades de banco, usando padroes", e);
            } finally {
                try {
                    input.close();
                } catch (Exception ignored) {
                    // Ignora erros ao fechar o arquivo.
                }
            }
        }
    }

    //Bloco que inicializa carregando o driver jdbc e as propriedades
    static {
        carregarDriver(nomeDriver -> Class.forName(nomeDriver));
        InputStream input = BancoDeDadosConfiguracao.class.getClassLoader().getResourceAsStream("db.properties");
        carregarPropriedades(input);
    }

    /**
     * Obtém uma variável de ambiente.
     *
     * @param nome Nome da variável de ambiente
     * @return Retorna o resultado da operação (static String)
     */
    private static String obterVariavelAmbiente(String nome) {
        return fornecedorAmbiente.apply(nome);
    }

    /**
     * Define o fornecedor de variáveis de ambiente. Utilizado para facilitar os
     * testes unitários.
     */
    static void definirFornecedorAmbiente(Function<String, String> fornecedor) {
        fornecedorAmbiente = fornecedor;
    }

    /**
     * Obtém o valor de Host.
     *
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterHost() {
        //O programa procura a variável de ambiente DB_HOST. Ela é útil, por exemplo, quando a aplicação executa em um contêiner Docker.
        String env = obterVariavelAmbiente("DB_HOST");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        //O programa procura a propriedade que pode ser informada ao iniciar o Java.
        String prop = System.getProperty("db.host");
        if (prop != null && !prop.trim().isEmpty()) {
            return prop.trim();
        }
        //Essa alternativa utiliza o arquivo de propriedades.
        return propriedades.getProperty("db.host", "localhost");
    }

    /**
     * Obtém o valor de Port.
     *
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterPorta() {
        //O programa procura a variável de ambiente DB_HOST. Ela é útil, por exemplo, quando a aplicação executa em um contêiner Docker.
        String env = obterVariavelAmbiente("DB_PORT");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        //O programa procura a propriedade que pode ser informada ao iniciar o Java.
        String prop = System.getProperty("db.port");
        if (prop != null && !prop.trim().isEmpty()) {
            return prop.trim();
        }
        return propriedades.getProperty("db.port", "3306");
    }

    /**
     * Obtém o valor de DbName.
     *
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterNomeBanco() {
        //O programa procura a variável de ambiente DB_HOST. Ela é útil, por exemplo, quando a aplicação executa em um contêiner Docker.
        String env = obterVariavelAmbiente("DB_NAME");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        //O programa procura a propriedade que pode ser informada ao iniciar o Java.
        String prop = System.getProperty("db.name");
        if (prop != null && !prop.trim().isEmpty()) {
            return prop.trim();
        }
        return propriedades.getProperty("db.name", "EstoqueDB");
    }

    /**
     * Obtém o valor de User.
     *
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterUsuario() {
        //O programa procura a variável de ambiente DB_HOST. Ela é útil, por exemplo, quando a aplicação executa em um contêiner Docker.
        String env = obterVariavelAmbiente("DB_USER");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        //O programa procura a propriedade que pode ser informada ao iniciar o Java.
        String prop = System.getProperty("db.user");
        if (prop != null && !prop.trim().isEmpty()) {
            return prop.trim();
        }
        //Essa alternativa utiliza o arquivo de propriedades.
        return propriedades.getProperty("db.user", "root");
    }

    /**
     * Obtém o valor de Password.
     *
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterSenha() {
        //O programa procura a variável de ambiente DB_HOST. Ela é útil, por exemplo, quando a aplicação executa em um contêiner Docker.
        String env = obterVariavelAmbiente("DB_PASSWORD");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        //O programa procura a propriedade que pode ser informada ao iniciar o Java.
        String prop = System.getProperty("db.password");
        if (prop != null && !prop.trim().isEmpty()) {
            return prop.trim();
        }
        //Essa alternativa utiliza o arquivo de propriedades.
        return propriedades.getProperty("db.password", "root");
    }

    /**
     * Obtém o valor de Params.
     *
     * @return Retorna o resultado da operação (static String)
     */
    public static String obterParametros() {
        //O programa procura a variável de ambiente DB_HOST. Ela é útil, por exemplo, quando a aplicação executa em um contêiner Docker.
        String env = obterVariavelAmbiente("DB_PARAMS");
        if (env != null && !env.trim().isEmpty()) {
            return env.trim();
        }
        //O programa procura a propriedade que pode ser informada ao iniciar o Java.
        String prop = System.getProperty("db.params");
        if (prop != null && !prop.trim().isEmpty()) {
            return prop.trim();
        }
        //Essa alternativa utiliza o arquivo de propriedades.
        return propriedades.getProperty("db.params", "?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");
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
        String urlBanco = "jdbc:mysql://" + obterHost() + ":" + obterPorta() + "/" + obterNomeBanco() + obterParametros();
        return obterConexaoInterna(urlBanco);
    }

    /**
     * Tenta conectar utilizando a senha configurada em db.propriedades e
     * fallback para senhas comuns se necessario.
     */
    private static synchronized Connection obterConexaoInterna(String url) throws SQLException {
        //Obtem o usuário e senha
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
