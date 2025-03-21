CREATE DATABASE IF NOT EXISTS db_notification;

USE db_notification;

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
