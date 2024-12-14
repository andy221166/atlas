CREATE DATABASE IF NOT EXISTS db_user;

USE db_user;

CREATE TABLE IF NOT EXISTS users
(
    id           INT            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(255)   NOT NULL,
    password     VARCHAR(255)   NOT NULL,
    role         VARCHAR(50)    NOT NULL,
    first_name   VARCHAR(255)   NOT NULL,
    last_name    VARCHAR(255)   NOT NULL,
    email        VARCHAR(255)   NOT NULL,
    phone_number VARCHAR(20)    NOT NULL,
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_username (username),
    UNIQUE INDEX idx_email (email)
) ENGINE = INNODB;

-- Password: Aa@123456
INSERT INTO users (id, username, password, role, first_name, last_name, email, phone_number)
VALUES
    (1, 'admin', '$2a$12$JBXIjeVKldJZ0824t5ULHOLeoq330xmpx0Ua/5Ipz4hlGxlSm9nE2', 'ADMIN', 'John', 'Doe', 'admin@atlas.org', '0987654321'),
    (2, 'customer', '$2a$12$JBXIjeVKldJZ0824t5ULHOLeoq330xmpx0Ua/5Ipz4hlGxlSm9nE2', 'CUSTOMER', 'John', 'Smith', 'user@atlas.org', '0987321654');

CREATE TABLE IF NOT EXISTS customer
(
    user_id      INT            NOT NULL AUTO_INCREMENT PRIMARY KEY,
    credit       DECIMAL(11, 2) NOT NULL DEFAULT 0,
    created_at   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;

INSERT INTO customer (user_id, credit)
VALUES (2, 1000000);

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
