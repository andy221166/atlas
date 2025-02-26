CREATE DATABASE IF NOT EXISTS db_user;

USE db_user;

CREATE TABLE IF NOT EXISTS userEntities
(
    id           INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username     VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20)  NOT NULL,
    role         VARCHAR(50)  NOT NULL,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_username (username),
    UNIQUE INDEX idx_email (email)
) ENGINE = INNODB;

-- Password: Aa@123456
INSERT INTO userEntities (id, username, password, first_name, last_name, email, phone_number, role)
VALUES (1, 'admin', '$2a$12$JBXIjeVKldJZ0824t5ULHOLeoq330xmpx0Ua/5Ipz4hlGxlSm9nE2', 'John', 'Doe',
        'admin@atlas.org', '0987654321', 'ADMIN'),
       (2, 'user', '$2a$12$JBXIjeVKldJZ0824t5ULHOLeoq330xmpx0Ua/5Ipz4hlGxlSm9nE2', 'John', 'Smith',
        'user@atlas.org', '0987321654', 'USER');

CREATE TABLE IF NOT EXISTS outbox_message
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    event        TEXT         NOT NULL,
    destination  VARCHAR(255) NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    processed_at DATETIME,
    error        TEXT,
    retries      TINYINT               DEFAULT 0,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;
