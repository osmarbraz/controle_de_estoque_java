-- ============================================================
-- 0. Criar o Banco de Dados
-- ============================================================

CREATE DATABASE EstoqueDB;
USE EstoqueDB;

-- ============================================================
-- 1. Tabela Cidade
-- ============================================================

CREATE TABLE IF NOT EXISTS Cidade (
    cidadeId INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    uf VARCHAR(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- 2. Tabela Categoria
-- ============================================================

CREATE TABLE IF NOT EXISTS Categoria (
    categoriaId INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- 3. Tabela Cliente
-- ============================================================

CREATE TABLE IF NOT EXISTS Cliente (
    clienteId INT AUTO_INCREMENT PRIMARY KEY,
    Nome VARCHAR(150) NOT NULL,
    CPF VARCHAR(20) NOT NULL,
    rua VARCHAR(150),
    numero VARCHAR(20),
    bairro VARCHAR(100),
    cidadeId INT NOT NULL,
    dataCadastro DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cliente_cidade
        FOREIGN KEY (cidadeId)
        REFERENCES Cidade(cidadeId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- 4. Tabela Produto
-- ============================================================

CREATE TABLE IF NOT EXISTS Produto (
    produtoId INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(200) NOT NULL,
    unidade VARCHAR(10) NOT NULL,
    precoCusto DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    precoVenda DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    quantidade DOUBLE NOT NULL DEFAULT 0,
    categoriaId INT NOT NULL,
    dataCadastro DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (categoriaId)
        REFERENCES Categoria(categoriaId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- 5. Tabela Pedido
-- ============================================================

CREATE TABLE IF NOT EXISTS Pedido (
    pedidoId INT AUTO_INCREMENT PRIMARY KEY,
    clienteId INT NOT NULL,
    data DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    situacao VARCHAR(20) NOT NULL DEFAULT 'aberto',

    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (clienteId)
        REFERENCES Cliente(clienteId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ============================================================
-- 6. Tabela ItemPedido
-- ============================================================

CREATE TABLE IF NOT EXISTS ItemPedido (
    pedidoId INT NOT NULL,
    produtoId INT NOT NULL,
    quantidade DOUBLE NOT NULL DEFAULT 1,
    precoVenda DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    PRIMARY KEY (pedidoId, produtoId),

    CONSTRAINT fk_item_pedido
        FOREIGN KEY (pedidoId)
        REFERENCES Pedido(pedidoId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_item_produto
        FOREIGN KEY (produtoId)
        REFERENCES Produto(produtoId)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;