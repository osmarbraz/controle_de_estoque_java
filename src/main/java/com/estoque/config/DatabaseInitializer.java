package com.estoque.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());
    private static volatile boolean isInitialized = false;

    public static synchronized void initializeIfNeeded() {
        if (isInitialized) {
            return;
        }

        try {
            LOGGER.info("Verificando existencia do Banco de Dados e Tabelas...");
            createDatabaseIfNotExists();
            createTablesIfNotExists();
            seedInitialDataIfEmpty();
            isInitialized = true;
            LOGGER.info("Banco de Dados e Tabelas validados com sucesso!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao inicializar Banco de Dados e Tabelas: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao inicializar banco de dados", e);
        }
    }

    private static void createDatabaseIfNotExists() throws Exception {
        try (Connection conn = DatabaseConfig.getServerConnection();
             Statement stmt = conn.createStatement()) {
            String dbName = DatabaseConfig.getDbName();
            String sql = "CREATE DATABASE IF NOT EXISTS " + dbName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
            stmt.executeUpdate(sql);
            LOGGER.info("Banco de dados '" + dbName + "' verificado/criado.");
        }
    }

    private static void createTablesIfNotExists() throws Exception {
        try (Connection conn = DatabaseConfig.getDatabaseConnectionInternal();
             Statement stmt = conn.createStatement()) {

            // 1. Tabela Cidade
            String createCidade = "CREATE TABLE IF NOT EXISTS Cidade ("
                    + "cidadeId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "nome VARCHAR(100) NOT NULL, "
                    + "uf VARCHAR(2) NOT NULL"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            stmt.executeUpdate(createCidade);

            // 2. Tabela Categoria
            String createCategoria = "CREATE TABLE IF NOT EXISTS Categoria ("
                    + "categoriaId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "nome VARCHAR(100) NOT NULL"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            stmt.executeUpdate(createCategoria);

            // 3. Tabela Cliente
            String createCliente = "CREATE TABLE IF NOT EXISTS Cliente ("
                    + "clienteId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "Nome VARCHAR(150) NOT NULL, "
                    + "CPF VARCHAR(20) NOT NULL, "
                    + "rua VARCHAR(150), "
                    + "numero VARCHAR(20), "
                    + "bairro VARCHAR(100), "
                    + "cidadeId INT NOT NULL, "
                    + "dataCadastro DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "CONSTRAINT fk_cliente_cidade FOREIGN KEY (cidadeId) REFERENCES Cidade(cidadeId) ON DELETE RESTRICT ON UPDATE CASCADE"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            stmt.executeUpdate(createCliente);

            // 4. Tabela Produto
            String createProduto = "CREATE TABLE IF NOT EXISTS Produto ("
                    + "produtoId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "descricao VARCHAR(200) NOT NULL, "
                    + "unidade VARCHAR(10) NOT NULL, "
                    + "precoCusto DECIMAL(10,2) NOT NULL DEFAULT 0.00, "
                    + "precoVenda DECIMAL(10,2) NOT NULL DEFAULT 0.00, "
                    + "quantidade DOUBLE NOT NULL DEFAULT 0, "
                    + "categoriaId INT NOT NULL, "
                    + "dataCadastro DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "CONSTRAINT fk_produto_categoria FOREIGN KEY (categoriaId) REFERENCES Categoria(categoriaId) ON DELETE RESTRICT ON UPDATE CASCADE"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            stmt.executeUpdate(createProduto);

            // 5. Tabela Pedido
            String createPedido = "CREATE TABLE IF NOT EXISTS Pedido ("
                    + "pedidoId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "clienteId INT NOT NULL, "
                    + "data DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    + "total DECIMAL(10,2) NOT NULL DEFAULT 0.00, "
                    + "situacao VARCHAR(20) NOT NULL DEFAULT 'aberto', "
                    + "CONSTRAINT fk_pedido_cliente FOREIGN KEY (clienteId) REFERENCES Cliente(clienteId) ON DELETE RESTRICT ON UPDATE CASCADE"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            stmt.executeUpdate(createPedido);

            // 6. Tabela ItemPedido
            String createItemPedido = "CREATE TABLE IF NOT EXISTS ItemPedido ("
                    + "pedidoId INT NOT NULL, "
                    + "produtoId INT NOT NULL, "
                    + "quantidade DOUBLE NOT NULL DEFAULT 1, "
                    + "precoVenda DECIMAL(10,2) NOT NULL DEFAULT 0.00, "
                    + "PRIMARY KEY (pedidoId, produtoId), "
                    + "CONSTRAINT fk_item_pedido FOREIGN KEY (pedidoId) REFERENCES Pedido(pedidoId) ON DELETE CASCADE ON UPDATE CASCADE, "
                    + "CONSTRAINT fk_item_produto FOREIGN KEY (produtoId) REFERENCES Produto(produtoId) ON DELETE RESTRICT ON UPDATE CASCADE"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
            stmt.executeUpdate(createItemPedido);

            LOGGER.info("Tabelas Cidade, Categoria, Cliente, Produto, Pedido e ItemPedido verificadas/criadas.");
        }
    }

    private static void seedInitialDataIfEmpty() {
        try (Connection conn = DatabaseConfig.getDatabaseConnectionInternal();
             Statement stmt = conn.createStatement()) {

            // Checar se a tabela Cidade possui registros
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Cidade");
            if (rs.next() && rs.getInt(1) == 0) {
                LOGGER.info("Populando dados iniciais de demonstracao no banco EstoqueDB...");

                // Cidades
                stmt.executeUpdate("INSERT INTO Cidade (nome, uf) VALUES "
                        + "('São Paulo', 'SP'), "
                        + "('Rio de Janeiro', 'RJ'), "
                        + "('Belo Horizonte', 'MG'), "
                        + "('Curitiba', 'PR');");

                // Categorias
                stmt.executeUpdate("INSERT INTO Categoria (nome) VALUES "
                        + "('Informática'), "
                        + "('Eletrônicos'), "
                        + "('Alimentos'), "
                        + "('Bebidas'), "
                        + "('Escritório');");

                // Clientes
                stmt.executeUpdate("INSERT INTO Cliente (Nome, CPF, rua, numero, bairro, cidadeId, dataCadastro) VALUES "
                        + "('João da Silva', '123.456.789-00', 'Av. Paulista', '1000', 'Bela Vista', 1, NOW()), "
                        + "('Maria Fernandes', '987.654.321-11', 'Rua Copacabana', '500', 'Copacabana', 2, NOW()), "
                        + "('Carlos Souza', '456.789.123-22', 'Rua dos Inconfidentes', '250', 'Savassi', 3, NOW());");

                // Produtos
                stmt.executeUpdate("INSERT INTO Produto (descricao, unidade, precoCusto, precoVenda, quantidade, categoriaId, dataCadastro) VALUES "
                        + "('Teclado Mecânico RGB', 'UN', 120.00, 199.90, 45, 1, NOW()), "
                        + "('Mouse Sem Fio Ergoclean', 'UN', 45.00, 89.90, 80, 1, NOW()), "
                        + "('Monitor UltraWide 29 IPS', 'UN', 750.00, 1199.00, 15, 2, NOW()), "
                        + "('Café Gourmet Torrado 500g', 'UN', 18.50, 29.90, 120, 3, NOW()), "
                        + "('Caixa de Água Mineral 500ml', 'CX', 15.00, 28.00, 60, 4, NOW()), "
                        + "('Cadeira Ergonômica de Escritório', 'UN', 320.00, 549.00, 10, 5, NOW());");

                // Pedido Inicial
                stmt.executeUpdate("INSERT INTO Pedido (clienteId, data, total, situacao) VALUES "
                        + "(1, NOW(), 289.80, 'aberto');");

                // Itens do Pedido Inicial
                stmt.executeUpdate("INSERT INTO ItemPedido (pedidoId, produtoId, quantidade, precoVenda) VALUES "
                        + "(1, 1, 1, 199.90), "
                        + "(1, 2, 1, 89.90);");

                LOGGER.info("Dados de demonstracao inseridos com sucesso.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao inserir dados iniciais: " + e.getMessage(), e);
        }
    }
}
