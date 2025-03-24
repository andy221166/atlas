CREATE DATABASE IF NOT EXISTS db_product;

USE db_product;

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

-- Insert Brands
INSERT INTO brand (name)
VALUES ('Apple'),
       ('Samsung'),
       ('Nike'),
       ('Adidas'),
       ('Sony'),
       ('Dell'),
       ('Logitech'),
       ('Bose'),
       ('LG'),
       ('Philips');

-- Insert Categories
INSERT INTO category (name)
VALUES ('Electronics'),
       ('Clothing'),
       ('Home Appliances'),
       ('Sports Equipment'),
       ('Audio'),
       ('Computing'),
       ('Mobile Phones'),
       ('Wearables'),
       ('Gaming'),
       ('Kitchen Appliances');

-- Insert Products
INSERT INTO product (code, name, price, status, available_from, is_active, brand_id, quantity)
VALUES ('APPH14PRO', 'iPhone 14 Pro', 1299.99, 'IN_STOCK', '2023-09-15 00:00:00', 1, 1, 150),
       ('SAMS22ULT', 'Samsung S22 Ultra', 1199.99, 'IN_STOCK', '2023-08-10 00:00:00', 1, 2, 120),
       ('NKAIRMAX', 'Nike Air Max 270', 149.99, 'IN_STOCK', '2023-07-20 00:00:00', 1, 3, 200),
       ('ADIULTRA', 'Adidas Ultraboost', 179.99, 'DISCONTINUED', '2023-06-15 00:00:00', 1, 4, 0),
       ('SONYWH1000', 'Sony WH-1000XM4', 349.99, 'IN_STOCK', '2023-05-10 00:00:00', 1, 5, 75),
       ('DELLXPS15', 'Dell XPS 15', 1799.99, 'IN_STOCK', '2023-04-22 00:00:00', 1, 6, 50),
       ('LGCX65OLED', 'LG CX 65" OLED TV', 2499.99, 'IN_STOCK', '2023-03-15 00:00:00', 1, 9, 30),
       ('BOSESPORT', 'Bose Sport Earbuds', 179.99, 'IN_STOCK', '2023-02-28 00:00:00', 1, 8, 100),
       ('LOGMX3', 'Logitech MX Master 3', 99.99, 'IN_STOCK', '2023-01-20 00:00:00', 1, 7, 80),
       ('PHILAIR5', 'Philips Airfryer XXL', 249.99, 'IN_STOCK', '2023-06-05 00:00:00', 1, 10, 60),
       ('APPWATCH8', 'Apple Watch Series 8', 399.99, 'IN_STOCK', '2023-09-20 00:00:00', 1, 1, 90),
       ('SAMQLEDA', 'Samsung Q80A QLED TV', 1499.99, 'OUT_OF_STOCK', '2023-07-10 00:00:00', 0, 2,
        0),
       ('NKDRIFIT', 'Nike Dri-FIT Running Shirt', 34.99, 'IN_STOCK', '2023-08-15 00:00:00', 1, 3,
        300),
       ('ADISTANS', 'Adidas Stan Smith', 89.99, 'IN_STOCK', '2023-05-25 00:00:00', 1, 4, 150),
       ('SONYPSX5', 'Sony PlayStation 5', 499.99, 'DISCONTINUED', '2023-04-10 00:00:00', 1, 5, 0),
       ('DELLG15', 'Dell G15 Gaming Laptop', 1299.99, 'IN_STOCK', '2023-03-05 00:00:00', 1, 6, 40),
       ('LOGPRO', 'Logitech Pro Wireless Mouse', 129.99, 'IN_STOCK', '2023-02-15 00:00:00', 1, 7,
        65),
       ('BOSENC700', 'Bose NC 700 Headphones', 379.99, 'IN_STOCK', '2023-01-10 00:00:00', 1, 8, 55),
       ('LGNANO90', 'LG NanoCell 90 TV', 1199.99, 'IN_STOCK', '2023-06-20 00:00:00', 1, 9, 25),
       ('PHILHUE', 'Philips Hue Starter Kit', 199.99, 'IN_STOCK', '2023-07-25 00:00:00', 1, 10, 70);

-- Insert Product Details
INSERT INTO product_detail (product_id, description)
VALUES (1,
        'The iPhone 14 Pro features a 6.1-inch Super Retina XDR display with ProMotion technology, A16 Bionic chip, and a 48MP main camera with 4x optical zoom.'),
       (2,
        'The Samsung S22 Ultra comes with a 6.8-inch Dynamic AMOLED display, Snapdragon 8 Gen 1 processor, and a 108MP camera system with 100x Space Zoom.'),
       (3,
        'The Nike Air Max 270 features Nike\'s largest Air unit yet in the heel for unmatched comfort and a modern look that\'s perfect for everyday wear.'),
       (4,
        'The Adidas Ultraboost features responsive Boost cushioning, a Primeknit upper for adaptive support, and a Continental™ Rubber outsole for extraordinary grip.'),
       (5,
        'The Sony WH-1000XM4 wireless headphones offer industry-leading noise cancellation, exceptional sound quality, and up to 30 hours of battery life.'),
       (6,
        'The Dell XPS 15 features a 15.6-inch 4K OLED display, Intel Core i9 processor, NVIDIA GeForce RTX 3050 Ti graphics, and up to 32GB of RAM.'),
       (7,
        'The LG CX 65" OLED TV delivers perfect black, infinite contrast, and over a billion rich colors powered by the α9 Gen 3 AI Processor 4K.'),
       (8,
        'The Bose Sport Earbuds are designed for your best workout yet with a comfortable secure fit, weather-resistant design, and high-quality sound.'),
       (9,
        'The Logitech MX Master 3 is an advanced wireless mouse with ultra-fast scrolling, app-specific customizations, and works on any surface.'),
       (10,
        'The Philips Airfryer XXL uses hot air to fry your favorite foods with little or no added oil, featuring Twin TurboStar technology for fat removal.'),
       (11,
        'The Apple Watch Series 8 features advanced health sensors, a crack-resistant front crystal, and new safety features like Crash Detection.'),
       (12,
        'The Samsung Q80A QLED TV features Quantum Dot technology, Direct Full Array backlighting, and Object Tracking Sound for an immersive experience.'),
       (13,
        'The Nike Dri-FIT Running Shirt is made with sweat-wicking fabric to help keep you dry and comfortable during your run.'),
       (14,
        'The Adidas Stan Smith is a classic tennis shoe with a clean, minimalist design featuring a leather upper and perforated 3-Stripes.'),
       (15,
        'The Sony PlayStation 5 features lightning-fast loading, an ultra-high speed SSD, deeper immersion with haptic feedback, and stunning 4K graphics.'),
       (16,
        'The Dell G15 Gaming Laptop features an 11th Gen Intel Core i7 processor, NVIDIA GeForce RTX 3060 graphics, and advanced thermal management.'),
       (17,
        'The Logitech Pro Wireless Mouse is designed with esports professionals for a lightweight, ultra-responsive gaming experience with LIGHTSPEED wireless technology.'),
       (18,
        'The Bose NC 700 Headphones feature 11 levels of noise cancellation, unrivaled voice pickup for clear calls, and up to 20 hours of battery life.'),
       (19,
        'The LG NanoCell 90 TV uses NanoCell technology to deliver pure colors, wide viewing angles, and features AI-powered 4K upscaling.'),
       (20,
        'The Philips Hue Starter Kit includes a bridge and four smart bulbs that can be controlled via app or voice for millions of colors and scenes.');

-- Insert Product Images
INSERT INTO product_image (product_id, image_url, is_cover)
VALUES (1, 'https://example.com/images/iphone14pro_1.jpg', 1),
       (1, 'https://example.com/images/iphone14pro_2.jpg', 0),
       (1, 'https://example.com/images/iphone14pro_3.jpg', 0),
       (2, 'https://example.com/images/samsungs22_1.jpg', 1),
       (2, 'https://example.com/images/samsungs22_2.jpg', 0),
       (3, 'https://example.com/images/nikeairmax_1.jpg', 1),
       (3, 'https://example.com/images/nikeairmax_2.jpg', 0),
       (4, 'https://example.com/images/adidasultra_1.jpg', 1),
       (5, 'https://example.com/images/sonywh1000_1.jpg', 1),
       (5, 'https://example.com/images/sonywh1000_2.jpg', 0),
       (6, 'https://example.com/images/dellxps_1.jpg', 1),
       (7, 'https://example.com/images/lgoled_1.jpg', 1),
       (8, 'https://example.com/images/bosesport_1.jpg', 1),
       (9, 'https://example.com/images/logimx3_1.jpg', 1),
       (10, 'https://example.com/images/philipsair_1.jpg', 1),
       (11, 'https://example.com/images/applewatch_1.jpg', 1),
       (12, 'https://example.com/images/samsungqled_1.jpg', 1),
       (13, 'https://example.com/images/nikedrifit_1.jpg', 1),
       (14, 'https://example.com/images/adidasssmith_1.jpg', 1),
       (15, 'https://example.com/images/ps5_1.jpg', 1),
       (16, 'https://example.com/images/dellg15_1.jpg', 1),
       (17, 'https://example.com/images/logipro_1.jpg', 1),
       (18, 'https://example.com/images/bosenc700_1.jpg', 1),
       (19, 'https://example.com/images/lgnano_1.jpg', 1),
       (20, 'https://example.com/images/philipshue_1.jpg', 1);

-- Insert Product Categories
INSERT INTO product_category (product_id, category_id)
VALUES (1, 1),
       (1, 7),   -- iPhone: Electronics, Mobile Phones
       (2, 1),
       (2, 7),   -- Samsung S22: Electronics, Mobile Phones
       (3, 2),
       (3, 4),   -- Nike Air Max: Clothing, Sports Equipment
       (4, 2),
       (4, 4),   -- Adidas Ultraboost: Clothing, Sports Equipment
       (5, 1),
       (5, 5),   -- Sony Headphones: Electronics, Audio
       (6, 1),
       (6, 6),   -- Dell XPS: Electronics, Computing
       (7, 1),
       (7, 3),   -- LG OLED TV: Electronics, Home Appliances
       (8, 5),
       (8, 4),   -- Bose Earbuds: Audio, Sports Equipment
       (9, 1),
       (9, 6),   -- Logitech Mouse: Electronics, Computing
       (10, 3),
       (10, 10), -- Philips Airfryer: Home Appliances, Kitchen Appliances
       (11, 1),
       (11, 8),  -- Apple Watch: Electronics, Wearables
       (12, 1),
       (12, 3),  -- Samsung QLED: Electronics, Home Appliances
       (13, 2),
       (13, 4),  -- Nike Shirt: Clothing, Sports Equipment
       (14, 2),
       (14, 4),  -- Adidas Stan Smith: Clothing, Sports Equipment
       (15, 1),
       (15, 9),  -- PlayStation 5: Electronics, Gaming
       (16, 1),
       (16, 6),
       (16, 9),  -- Dell G15: Electronics, Computing, Gaming
       (17, 1),
       (17, 6),
       (17, 9),  -- Logitech Pro Mouse: Electronics, Computing, Gaming
       (18, 1),
       (18, 5),  -- Bose NC 700: Electronics, Audio
       (19, 1),
       (19, 3),  -- LG NanoCell: Electronics, Home Appliances
       (20, 1),
       (20, 3); -- Philips Hue: Electronics, Home Appliances
