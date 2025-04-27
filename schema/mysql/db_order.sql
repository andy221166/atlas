CREATE DATABASE IF NOT EXISTS db_order;

USE db_order;

CREATE TABLE IF NOT EXISTS orders
(
    id              INT            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code            CHAR(7)        NOT NULL,
    user_id         INT            NOT NULL,
    amount          DECIMAL(11, 2) NOT NULL,
    status          VARCHAR(20)    NOT NULL,
    canceled_reason VARCHAR(255),
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_code (code),
    INDEX idx_user_id (user_id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS order_item
(
    id            INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id      INT           NOT NULL,
    product_id    INT           NOT NULL,
    product_price DECIMAL(9, 2) NOT NULL,
    quantity      INT           NOT NULL,
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
) ENGINE = InnoDB;

CREATE TABLE sequence_generator
(
    seq_name  VARCHAR(50) PRIMARY KEY,
    seq_value INT NOT NULL
);
