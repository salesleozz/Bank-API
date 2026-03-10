-- Script de criação do banco de dados e dados de teste para Bank API

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS bank_api_db;
USE bank_api_db;

-- As tabelas serão criadas automaticamente pelo JPA/Hibernate
-- mas aqui estão as estruturas principais que serão geradas:

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuarios (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  senha VARCHAR(255) NOT NULL,
  cpf VARCHAR(11) NOT NULL UNIQUE,
  telefone VARCHAR(20) NOT NULL,
  data_nascimento VARCHAR(255) NOT NULL,
  endereco VARCHAR(500) NOT NULL,
  data_criacao DATETIME NOT NULL,
  data_atualizacao DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Contas Bancárias
CREATE TABLE IF NOT EXISTS contas_bancarias (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL UNIQUE,
  numero_conta VARCHAR(50) NOT NULL UNIQUE,
  agencia VARCHAR(10) NOT NULL UNIQUE,
  saldo DECIMAL(15, 2) NOT NULL DEFAULT 0,
  limite_cheque DECIMAL(15, 2) NOT NULL DEFAULT 1000,
  ativa BOOLEAN NOT NULL DEFAULT true,
  data_criacao DATETIME NOT NULL,
  data_atualizacao DATETIME NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Cartões
CREATE TABLE IF NOT EXISTS cartoes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  numero_cartao VARCHAR(16) NOT NULL UNIQUE,
  titular VARCHAR(255) NOT NULL,
  data_validade VARCHAR(5) NOT NULL,
  cvv VARCHAR(3) NOT NULL,
  limite_credito DECIMAL(15, 2) NOT NULL,
  saldo_disponivel DECIMAL(15, 2) NOT NULL,
  fatura_atual DECIMAL(15, 2) NOT NULL DEFAULT 0,
  status_cartao VARCHAR(50) NOT NULL,
  tipo_cartao VARCHAR(50) NOT NULL,
  data_criacao DATETIME NOT NULL,
  data_atualizacao DATETIME NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  INDEX idx_usuario_id (usuario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Transferências
CREATE TABLE IF NOT EXISTS transferencias (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_origem_id BIGINT NOT NULL,
  usuario_destino_id BIGINT NOT NULL,
  valor DECIMAL(15, 2) NOT NULL,
  descricao VARCHAR(500),
  tipo_transferencia VARCHAR(50) NOT NULL,
  status_transferencia VARCHAR(50) NOT NULL,
  data_transferencia DATETIME NOT NULL,
  data_criacao DATETIME NOT NULL,
  FOREIGN KEY (usuario_origem_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  FOREIGN KEY (usuario_destino_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  INDEX idx_usuario_origem (usuario_origem_id),
  INDEX idx_usuario_destino (usuario_destino_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de Empréstimos
CREATE TABLE IF NOT EXISTS emprestimos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  usuario_id BIGINT NOT NULL,
  valor_total DECIMAL(15, 2) NOT NULL,
  saldo_devedor DECIMAL(15, 2) NOT NULL,
  taxa_juros DECIMAL(5, 2) NOT NULL,
  numero_parcelas INT NOT NULL,
  parcelas_quitadas INT NOT NULL DEFAULT 0,
  status_emprestimo VARCHAR(50) NOT NULL,
  data_contratacao DATETIME NOT NULL,
  data_primeiro_vencimento DATETIME NOT NULL,
  data_proximo_vencimento DATETIME NOT NULL,
  data_criacao DATETIME NOT NULL,
  data_atualizacao DATETIME NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  INDEX idx_usuario_id (usuario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dados de teste
INSERT INTO usuarios (nome, email, senha, cpf, telefone, data_nascimento, endereco, data_criacao, data_atualizacao) VALUES
('João Silva', 'joao.silva@example.com', 'senha123', '12345678901', '(11) 98765-4321', '1990-01-15', 'Rua A, 123, São Paulo', NOW(), NOW()),
('Maria Santos', 'maria.santos@example.com', 'senha456', '98765432101', '(11) 99876-5432', '1992-05-20', 'Av. B, 456, São Paulo', NOW(), NOW()),
('Pedro Oliveira', 'pedro.oliveira@example.com', 'senha789', '55555555555', '(11) 91234-5678', '1988-03-10', 'Rua C, 789, Rio de Janeiro', NOW(), NOW());

-- Contas bancárias são criadas automaticamente na aplicação

