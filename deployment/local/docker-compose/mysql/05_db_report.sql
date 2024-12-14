CREATE DATABASE IF NOT EXISTS db_report;

USE db_report;

CREATE TABLE IF NOT EXISTS orders
(
    id           INT            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id      INT            NOT NULL,
    first_name   VARCHAR(255)   NOT NULL,
    last_name    VARCHAR(255)   NOT NULL,
    amount       DECIMAL(11, 2) NOT NULL,
    created_at   DATETIME       NOT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS order_item
(
    id            INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id      INT           NOT NULL,
    product_id    INT           NOT NULL,
    product_name  VARCHAR(255)  NOT NULL,
    product_price DECIMAL(9, 2) NOT NULL,
    quantity      INT           NOT NULL
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS outbox_message
(
    id           BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    payload      TEXT          NOT NULL,
    status       VARCHAR(10)   NOT NULL,
    processed_at DATETIME,
    error        TEXT,
    retries      TINYINT                DEFAULT 0,
    created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;
