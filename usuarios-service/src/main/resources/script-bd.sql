CREATE DATABASE IF NOT EXISTS carambolos_usuarios
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE carambolos_usuarios;

-- -----------------------------------------------------
-- Table usuario
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
  id INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(60) NOT NULL,
  senha VARCHAR(60) NOT NULL,
  contato VARCHAR(14) NOT NULL,
  data_nascimento DATE NULL,
  genero VARCHAR(20) NULL,
  imagem_url VARCHAR(500) NULL,
  sys_admin TINYINT NULL,
  is_ativo TINYINT NULL,
  PRIMARY KEY (id),
  INDEX nome_idx (nome ASC),
  INDEX contato_idx (contato ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table endereco
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS endereco (
  id INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(20) NULL,
  cep VARCHAR(128) NOT NULL,
  estado VARCHAR(256) NOT NULL,
  cidade VARCHAR(256) NOT NULL,
  bairro VARCHAR(256) NOT NULL,
  logradouro VARCHAR(256) NOT NULL,
  numero VARCHAR(128) NOT NULL,
  complemento VARCHAR(256) NULL,
  referencia VARCHAR(256) NULL,
  usuario_id INT NULL,
  is_ativo TINYINT NULL,
  dedup_hash VARCHAR(64) NULL,
  PRIMARY KEY (id),
  INDEX fk_endereco_usuario1_idx (usuario_id ASC),
  INDEX cep_idx (cep ASC),
  INDEX dedup_hash_idx (dedup_hash ASC),
  CONSTRAINT fk_endereco_usuario1
    FOREIGN KEY (usuario_id)
    REFERENCES usuario (id)
    ON DELETE SET NULL
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--CREATE TABLE IF NOT EXISTS jwt_token_blacklist (
--  id INT NOT NULL AUTO_INCREMENT,
--  token VARCHAR(500) NOT NULL,
--  blacklisted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--  expiration_date DATETIME NOT NULL,
--  usuario_id INT NULL,
--  PRIMARY KEY (id),
--  INDEX token_idx (token(255)),
--  INDEX blacklisted_at_idx (blacklisted_at),
--  INDEX usuario_id_idx (usuario_id)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

