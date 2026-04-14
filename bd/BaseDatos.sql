-- ========================================
-- BaseDatos.sql
-- Sistema financiero - Devsu Challenge
-- ========================================
-- Nota:
-- Este script crea la base de datos desde cero.
-- ========================================

-- RESET BASE DE DATOS
DROP DATABASE IF EXISTS devsu;
CREATE DATABASE devsu;
USE devsu;

-- ========================================
-- TABLA: clientes
-- ========================================
CREATE TABLE clientes (
    cliente_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    genero VARCHAR(10),
    edad INT,
    identificacion VARCHAR(50) NOT NULL UNIQUE,
    direccion VARCHAR(255),
    telefono VARCHAR(50),
    contrasena VARCHAR(255),
    estado BIT(1) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_clientes_identificacion ON clientes(identificacion);

-- ========================================
-- TABLA: cliente_ref
-- Uso: desacoplamiento vía eventos (RabbitMQ)
-- ========================================
CREATE TABLE cliente_ref (
    cliente_id BIGINT PRIMARY KEY
);

-- ========================================
-- TABLA: cuenta
-- ========================================
CREATE TABLE cuenta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta ENUM('AHORRO','CORRIENTE') NOT NULL,
    saldo_inicial DECIMAL(15,2) NOT NULL DEFAULT 0,
    estado BIT(1) NOT NULL,
    cliente_id BIGINT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cuenta_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(cliente_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_cuenta_cliente ON cuenta(cliente_id);

-- ========================================
-- TABLA: movimientos
-- ========================================
CREATE TABLE movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    tipo_movimiento ENUM('CREDITO','DEBITO') NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    cuenta_id BIGINT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_movimientos_cuenta
        FOREIGN KEY (cuenta_id)
        REFERENCES cuenta(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_movimientos_cuenta ON movimientos(cuenta_id);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha);

-- ========================================
-- DATOS DE PRUEBA 
-- ========================================

-- CLIENTES
INSERT INTO clientes (nombre, genero, edad, identificacion, direccion, telefono, contrasena, estado)
VALUES 
('Jose Lema', 'M', 30, '1111', 'Otavalo sn y principal', '098254785', '1234', b'1'),
('Marianela Montalvo', 'F', 28, '2222', 'Amazonas y NNUU', '0976548965', '5678', b'1'),
('Juan Osorio', 'M', 35, '3333', '13 junio y equinoccial', '098874587', '1245', b'1');

-- CLIENTE_REF (simulación eventos RabbitMQ)
INSERT INTO cliente_ref (cliente_id)
VALUES (1),(2),(3);

-- CUENTAS
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
VALUES
('478758', 'AHORRO', 2000, b'1', 1),
('225487', 'CORRIENTE', 100, b'1', 2),
('495878', 'AHORRO', 0, b'1', 3),
('496825', 'AHORRO', 540, b'1', 2),
('585545', 'CORRIENTE', 1000, b'1', 1);

-- MOVIMIENTOS
INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo, cuenta_id)
VALUES
('2022-02-10 10:00:00', 'DEBITO', -575, 1425, 1),
('2022-02-10 10:00:00', 'CREDITO', 600, 700, 2),
('2022-02-08 10:00:00', 'CREDITO', 150, 150, 3),
('2022-02-08 10:00:00', 'DEBITO', -540, 0, 4);