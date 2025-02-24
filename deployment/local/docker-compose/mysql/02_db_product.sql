CREATE DATABASE IF NOT EXISTS db_product;

USE db_product;

CREATE TABLE IF NOT EXISTS categoryEntity
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS aggProduct
(
    `id`          INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(255)  NOT NULL,
    `description` TEXT,
    `category_id` INT NOT NULL,
    `price`       DECIMAL(9, 2) NOT NULL,
    `quantity`    INT           NOT NULL,
    `created_at`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;

INSERT IGNORE INTO categoryEntity (`id`, `name`)
VALUES (1, 'ELECTRONICS'), (2, 'CLOTHING'), (3, 'FOOD'), (4, 'TOYS');

INSERT IGNORE INTO aggProduct (`id`, `name`, `description`, `category_id`, `price`, `quantity`, `created_at`)
VALUES
(1, 'Smartphone', 'A modern smartphone with high-speed performance.', 1, 599.99, 100, '2023-01-15 00:00:00'),
(2, 'Jeans', 'Comfortable blue jeans.', 2, 49.99, 100, '2023-02-20 01:00:00'),
(3, 'Dark Chocolate', 'Rich dark chocolate bar.', 3, 1.99, 100, '2023-03-01 02:05:00'),
(4, 'Action Figure', 'A collectible action figure.', 4, 15.99, 0, '2023-04-10 03:10:00'),
(5, 'Laptop', 'High-end laptop for work and gaming.', 1, 1200.00, 100, '2023-05-25 04:15:00'),
(6, 'T-Shirt', 'Cotton t-shirt in various sizes.', 2, 20.99, 100, '2023-06-15 05:20:00'),
(7, 'Milk Chocolate', 'Smooth milk chocolate bar.', 3, 3.49, 100, '2023-07-05 06:25:00'),
(8, 'Board Game', 'Family-friendly board game.', 4, 29.99, 100, '2023-08-20 07:30:00'),
(9, 'Electric Kettle', 'Fast boiling electric kettle.', 1, 35.99, 0, '2023-09-10 08:35:00'),
(10, 'Running Shoes', 'Lightweight running shoes.', 2, 89.99, 100, '2023-10-05 09:40:00');

CREATE TABLE IF NOT EXISTS outbox_message
(
    id           BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    event        TEXT          NOT NULL,
    destination  VARCHAR(255)  NOT NULL,
    status       VARCHAR(10)   NOT NULL,
    processed_at DATETIME,
    error        TEXT,
    retries      TINYINT                DEFAULT 0,
    created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = INNODB;
