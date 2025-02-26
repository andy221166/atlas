CREATE DATABASE IF NOT EXISTS db_catalog;

USE db_catalog;

CREATE TABLE IF NOT EXISTS brand
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS category
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS product
(
    `id`             INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`           CHAR(10)      NOT NULL,
    `name`           VARCHAR(255)  NOT NULL,
    `price`          DECIMAL(9, 2) NOT NULL,
    `status`         VARCHAR(20)   NOT NULL,
    `available_from` DATETIME      NOT NULL,
    `is_active`      TINYINT(1)    NOT NULL,
    `brand_id`       INT           NOT NULL,
    `quantity`       INT           NOT NULL,
    `created_at`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS product_detail
(
    `product_id`  INT      NOT NULL PRIMARY KEY,
    `description` TEXT,
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS product_image
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT          NOT NULL,
    `image_url`  VARCHAR(255) NOT NULL,
    `is_cover`   TINYINT(1)   NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_id (product_id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS product_category
(
    `product_id`  INT      NOT NULL,
    `category_id` INT      NOT NULL,
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`product_id`, `category_id`)
) ENGINE = INNODB;

CREATE TABLE sequence_generator
(
    seq_name  VARCHAR(50) PRIMARY KEY,
    seq_value INT NOT NULL
);

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
