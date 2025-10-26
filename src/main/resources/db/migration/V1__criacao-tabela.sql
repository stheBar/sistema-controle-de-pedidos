-- ===============================
-- V1__criar_tabelas_iniciais.sql
-- Sistema de Controle de Pedidos
-- ===============================

-- Usuário
CREATE TABLE IF NOT EXISTS usuario (
                                       id_usuario BIGSERIAL PRIMARY KEY,
                                       nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(72) NOT NULL,
    usuario_tipo VARCHAR(20) NOT NULL
    );

-- Mesa
CREATE TABLE IF NOT EXISTS mesa (
                                    id_mesa BIGSERIAL PRIMARY KEY,
                                    numero INT NOT NULL UNIQUE,
                                    disponivel BOOLEAN NOT NULL DEFAULT TRUE
);

-- Produto
CREATE TABLE IF NOT EXISTS produto (
                                       id_produto BIGSERIAL PRIMARY KEY,
                                       nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco NUMERIC(10,2) NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    categoria VARCHAR(20) NOT NULL
    );

-- Conta
CREATE TABLE IF NOT EXISTS conta (
                                     id_conta BIGSERIAL PRIMARY KEY,
                                     id_mesa BIGINT NOT NULL,
                                     cpf_titular VARCHAR(14),
    nome_titular VARCHAR(100),
    conta_status VARCHAR(20) NOT NULL DEFAULT 'ABERTA',
    data_abertura TIMESTAMP NOT NULL DEFAULT NOW(),
    data_fechamento TIMESTAMP,
    valor_total NUMERIC(14,2) NOT NULL DEFAULT 0,
    forma_pagamento VARCHAR(20),
    CONSTRAINT fk_conta_mesa FOREIGN KEY (id_mesa) REFERENCES mesa(id_mesa),
    CONSTRAINT ck_conta_forma_pagamento CHECK (
                                                  forma_pagamento IN ('DINHEIRO','CARTAO','PIX') OR forma_pagamento IS NULL
    )
    );

-- Índice: garante apenas uma conta ABERTA por mesa
CREATE UNIQUE INDEX IF NOT EXISTS uq_conta_aberta_por_mesa
    ON conta (id_mesa)
    WHERE conta_status = 'ABERTA';

-- Pedido
CREATE TABLE IF NOT EXISTS pedido (
                                      id_pedido BIGSERIAL PRIMARY KEY,
                                      id_usuario BIGINT NOT NULL,
                                      id_conta BIGINT NOT NULL,
                                      data_hora_pedido TIMESTAMP NOT NULL DEFAULT NOW(),
    pedido_status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_pedido_conta FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
    );

-- Itens do pedido
CREATE TABLE IF NOT EXISTS item_pedido (
                                           id_item_pedido BIGSERIAL PRIMARY KEY,
                                           id_pedido BIGINT NOT NULL,
                                           id_produto BIGINT NOT NULL,
                                           quantidade INT NOT NULL,
                                           preco_unitario NUMERIC(10,2) NOT NULL,
    observacao TEXT,
    CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    CONSTRAINT fk_item_pedido_produto FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
    );
