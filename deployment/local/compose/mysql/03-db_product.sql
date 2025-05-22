CREATE DATABASE IF NOT EXISTS db_product;

USE db_product;

CREATE TABLE IF NOT EXISTS brand
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS category
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS product
(
    `id`             INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`           VARCHAR(255)  NOT NULL,
    `price`          DECIMAL(9, 2) NOT NULL,
    `quantity`       INT           NOT NULL,
    `status`         VARCHAR(20)   NOT NULL,
    `available_from` DATETIME      NOT NULL,
    `is_active`      TINYINT(1)    NOT NULL,
    `brand_id`       INT           NOT NULL,
    `version`        BIGINT        DEFAULT 0,
    `created_at`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     DATETIME               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS product_details
(
    `product_id`  INT      NOT NULL PRIMARY KEY,
    `description` TEXT,
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS product_attribute
(
    `id`         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `product_id` INT          NOT NULL,
    `name`       VARCHAR(255) NOT NULL,
    `value`      VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_product_id (product_id),
    UNIQUE INDEX idx_product_id_name (product_id, name)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS product_category
(
    `product_id`  INT      NOT NULL,
    `category_id` INT      NOT NULL,
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`product_id`, `category_id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS outbox_message
(
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    message_payload TEXT         NOT NULL,
    message_class   VARCHAR(255) NOT NULL,
    message_key     VARCHAR(255) NOT NULL,
    destination     VARCHAR(255) NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    processed_at    DATETIME,
    error           TEXT,
    retries         TINYINT               DEFAULT 0,
    created_at      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB;

-- https://github.com/quartznet/quartznet/blob/main/database/tables/tables_mysql_innodb.sql

CREATE TABLE QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    JOB_NAME          VARCHAR(200) NOT NULL,
    JOB_GROUP         VARCHAR(200) NOT NULL,
    DESCRIPTION       VARCHAR(250) NULL,
    JOB_CLASS_NAME    VARCHAR(250) NOT NULL,
    IS_DURABLE        BOOLEAN      NOT NULL,
    IS_NONCONCURRENT  BOOLEAN      NOT NULL,
    IS_UPDATE_DATA    BOOLEAN      NOT NULL,
    REQUESTS_RECOVERY BOOLEAN      NOT NULL,
    JOB_DATA          BLOB         NULL,
    PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR(120) NOT NULL,
    TRIGGER_NAME   VARCHAR(200) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    JOB_NAME       VARCHAR(200) NOT NULL,
    JOB_GROUP      VARCHAR(200) NOT NULL,
    DESCRIPTION    VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(19)   NULL,
    PREV_FIRE_TIME BIGINT(19)   NULL,
    PRIORITY       INTEGER      NULL,
    TRIGGER_STATE  VARCHAR(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
    START_TIME     BIGINT(19)   NOT NULL,
    END_TIME       BIGINT(19)   NULL,
    CALENDAR_NAME  VARCHAR(200) NULL,
    MISFIRE_INSTR  SMALLINT(2)  NULL,
    JOB_DATA       BLOB         NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    REPEAT_COUNT    BIGINT(7)    NOT NULL,
    REPEAT_INTERVAL BIGINT(12)   NOT NULL,
    TIMES_TRIGGERED BIGINT(10)   NOT NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(120) NOT NULL,
    TIME_ZONE_ID    VARCHAR(80),
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR(120)   NOT NULL,
    TRIGGER_NAME  VARCHAR(200)   NOT NULL,
    TRIGGER_GROUP VARCHAR(200)   NOT NULL,
    STR_PROP_1    VARCHAR(512)   NULL,
    STR_PROP_2    VARCHAR(512)   NULL,
    STR_PROP_3    VARCHAR(512)   NULL,
    INT_PROP_1    INT            NULL,
    INT_PROP_2    INT            NULL,
    LONG_PROP_1   BIGINT         NULL,
    LONG_PROP_2   BIGINT         NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   BOOLEAN        NULL,
    BOOL_PROP_2   BOOLEAN        NULL,
    TIME_ZONE_ID  VARCHAR(80)    NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA     BLOB         NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    INDEX (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(200) NOT NULL,
    CALENDAR      BLOB         NOT NULL,
    PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    ENTRY_ID          VARCHAR(140) NOT NULL,
    TRIGGER_NAME      VARCHAR(200) NOT NULL,
    TRIGGER_GROUP     VARCHAR(200) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    FIRED_TIME        BIGINT(19)   NOT NULL,
    SCHED_TIME        BIGINT(19)   NOT NULL,
    PRIORITY          INTEGER      NOT NULL,
    STATE             VARCHAR(16)  NOT NULL,
    JOB_NAME          VARCHAR(200) NULL,
    JOB_GROUP         VARCHAR(200) NULL,
    IS_NONCONCURRENT  BOOLEAN      NULL,
    REQUESTS_RECOVERY BOOLEAN      NULL,
    PRIMARY KEY (SCHED_NAME, ENTRY_ID)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(19)   NOT NULL,
    CHECKIN_INTERVAL  BIGINT(19)   NOT NULL,
    PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
) ENGINE = InnoDB;

CREATE TABLE QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40)  NOT NULL,
    PRIMARY KEY (SCHED_NAME, LOCK_NAME)
) ENGINE = InnoDB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS (SCHED_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS (SCHED_NAME, JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS (SCHED_NAME, CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS (SCHED_NAME, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR,
                                                             NEXT_FIRE_TIME, TRIGGER_GROUP,
                                                             TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);

INSERT INTO brand (id, name)
VALUES (1, 'Apple'),
       (2, 'Samsung'),
       (3, 'Sony'),
       (4, 'LG'),
       (5, 'Dell');

INSERT INTO category (id, name)
VALUES (1, 'Laptops'),
       (2, 'Smartphones'),
       (3, 'Tablets'),
       (4, 'Televisions'),
       (5, 'Accessories');

INSERT INTO product (id, name, price, quantity, status, available_from, is_active, brand_id)
VALUES (1, 'MacBook Pro 14"', 1999.99, 50, 'IN_STOCK', NOW(), 1, 1),
       (2, 'Samsung Galaxy S23', 899.99, 100, 'IN_STOCK', NOW(), 1, 2),
       (3, 'Sony WH-1000XM5', 349.99, 200, 'IN_STOCK', NOW(), 1, 3),
       (4, 'LG OLED 55"', 1499.99, 0, 'DISCONTINUED', NOW(), 1, 4),
       (5, 'Dell XPS 15', 1899.99, 60, 'IN_STOCK', NOW(), 1, 5),
       (6, 'iPad Pro 12.9"', 1099.99, 70, 'IN_STOCK', NOW(), 1, 1),
       (7, 'Samsung Galaxy Tab S8', 799.99, 80, 'IN_STOCK', NOW(), 1, 2),
       (8, 'Sony Bravia 65"', 1799.99, 40, 'IN_STOCK', NOW(), 1, 3),
       (9, 'LG NanoCell 75"', 1999.99, 0, 'OUT_STOCK', NOW(), 1, 4),
       (10, 'Dell Inspiron 15', 849.99, 90, 'IN_STOCK', NOW(), 1, 5),
       (11, 'MacBook Air 13"', 1299.99, 55, 'IN_STOCK', NOW(), 1, 1),
       (12, 'Samsung Galaxy A54', 499.99, 110, 'IN_STOCK', NOW(), 1, 2),
       (13, 'Sony WF-1000XM4', 279.99, 150, 'IN_STOCK', NOW(), 1, 3),
       (14, 'LG UltraGear Monitor', 399.99, 100, 'IN_STOCK', NOW(), 1, 4),
       (15, 'Dell Latitude 7420', 1599.99, 75, 'IN_STOCK', NOW(), 1, 5),
       (16, 'Mac Studio', 3999.99, 30, 'IN_STOCK', NOW(), 1, 1),
       (17, 'Samsung QLED TV', 2199.99, 0, 'OUT_STOCK', NOW(), 1, 2),
       (18, 'Sony PlayStation 5', 499.99, 200, 'IN_STOCK', NOW(), 1, 3),
       (19, 'LG Soundbar', 199.99, 180, 'IN_STOCK', NOW(), 1, 4),
       (20, 'Alienware m15', 2099.99, 40, 'IN_STOCK', NOW(), 1, 5);

INSERT INTO product_details (product_id, description)
VALUES (1, 'A powerful MacBook with M2 chip and Retina display.'),
       (2, 'Latest Samsung flagship phone with high-end specs.'),
       (3, 'Noise-canceling headphones with amazing sound quality.'),
       (4, 'OLED Smart TV - great for gaming and streaming!'),
       (5, 'High-performance ultrabook with a sleek design.'),
       (6, 'iPad Pro 12.9" - The best tablet for professionals.'),
       (7, 'Premium Android tablet with a powerful chip.'),
       (8, 'Sony’s best Bravia model with Dolby Vision support.'),
       (9, 'NanoCell technology for amazing color accuracy.'),
       (10, 'A budget-friendly yet capable laptop.'),
       (11, 'Lightweight MacBook ideal for students.'),
       (12, 'A mid-range phone with great battery life.'),
       (13, 'Sony’s best-in-class wireless earbuds.'),
       (14, 'UltraGear gaming monitor with 240Hz refresh rate.'),
       (15, 'Corporate laptop optimized for security.'),
       (16, 'Superpower Mac Studio, perfect for professionals.'),
       (17, 'Samsung’s premium QLED TV with 4K HDR.'),
       (18, 'Next-gen gaming console with ultra-fast SSD.'),
       (19, 'Immersive soundbar for home theaters.'),
       (20, 'Powerful gaming laptop with RTX graphics.');

INSERT INTO product_attribute (id, product_id, name, value)
VALUES
    -- MacBook Pro 14"
    (1, 1, 'Processor', 'Apple M2'),
    (2, 1, 'RAM', '16GB'),
    (3, 1, 'Storage', '512GB SSD'),

    -- Samsung Galaxy S23
    (4, 2, 'Camera', '50MP Triple Lens'),
    (5, 2, 'Battery', '4500mAh'),
    (6, 2, 'Display', '6.1-inch Dynamic AMOLED'),

    -- Sony WH-1000XM5
    (7, 3, 'Type', 'Over-Ear'),
    (8, 3, 'Noise Cancelling', 'Yes'),
    (9, 3, 'Battery Life', '30 Hours'),

    -- LG OLED 55"
    (10, 4, 'Screen Size', '55 inches'),
    (11, 4, 'Resolution', '4K OLED'),
    (12, 4, 'Refresh Rate', '120Hz'),

    -- Dell XPS 15
    (13, 5, 'Processor', 'Intel i7-12700H'),
    (14, 5, 'RAM', '16GB DDR5'),
    (15, 5, 'Storage', '1TB SSD'),

    -- iPad Pro 12.9"
    (16, 6, 'Display', '12.9-inch Liquid Retina XDR'),
    (17, 6, 'Chip', 'Apple M1'),
    (18, 6, 'Storage', '512GB'),

    -- Samsung Galaxy Tab S8
    (19, 7, 'Battery Life', '12 Hours'),
    (20, 7, 'Storage', '256GB'),
    (21, 7, 'Display', '11-inch LCD'),

    -- Sony Bravia 65"
    (22, 8, 'Screen Size', '65 inches'),
    (23, 8, 'HDR', 'Dolby Vision'),
    (24, 8, 'Refresh Rate', '120Hz'),

    -- LG NanoCell 75"
    (25, 9, 'Screen Size', '75 inches'),
    (26, 9, 'Technology', 'NanoCell'),
    (27, 9, 'Resolution', '4K UHD'),

    -- Dell Inspiron 15
    (28, 10, 'Processor', 'Intel i5-1135G7'),
    (29, 10, 'RAM', '8GB DDR4'),
    (30, 10, 'Storage', '512GB SSD'),

    -- MacBook Air 13"
    (31, 11, 'Processor', 'Apple M2'),
    (32, 11, 'RAM', '8GB'),
    (33, 11, 'Storage', '256GB SSD'),

    -- Samsung Galaxy A54
    (34, 12, 'Camera', '48MP Quad'),
    (35, 12, 'Battery', '5000mAh'),
    (36, 12, 'Display', '6.4-inch AMOLED'),

    -- Sony WF-1000XM4
    (37, 13, 'Type', 'In-Ear'),
    (38, 13, 'Noise Cancelling', 'Yes'),
    (39, 13, 'Battery Life', '24 Hours'),

    -- LG UltraGear Monitor
    (40, 14, 'Screen Size', '27 inches'),
    (41, 14, 'Refresh Rate', '165Hz'),
    (42, 14, 'Resolution', '2560 x 1440'),

    -- Dell Latitude 7420
    (43, 15, 'Processor', 'Intel i7-1185G7'),
    (44, 15, 'RAM', '16GB LPDDR4X'),
    (45, 15, 'Storage', '512GB SSD'),

    -- Mac Studio
    (46, 16, 'Processor', 'Apple M1 Ultra'),
    (47, 16, 'RAM', '64GB Unified Memory'),
    (48, 16, 'Storage', '1TB SSD'),

    -- Samsung QLED TV
    (49, 17, 'Screen Size', '65 inches'),
    (50, 17, 'Technology', 'QLED'),
    (51, 17, 'Resolution', '4K UHD'),

    -- Sony PlayStation 5
    (52, 18, 'Processor', 'Custom AMD Ryzen Zen 2'),
    (53, 18, 'GPU', 'RDNA 2-based GPU'),
    (54, 18, 'Storage', '825GB SSD'),

    -- LG Soundbar
    (55, 19, 'Channels', '5.1.2'),
    (56, 19, 'Power Output', '500W'),
    (57, 19, 'Wireless Subwoofer', 'Yes'),

    -- Alienware m15
    (58, 20, 'Processor', 'Intel i9-12900H'),
    (59, 20, 'GPU', 'NVIDIA RTX 3080'),
    (60, 20, 'Storage', '2TB SSD');

INSERT INTO product_category (product_id, category_id)
VALUES
-- Laptops
(1, 1),  -- MacBook Pro 14" → Laptops
(5, 1),  -- Dell XPS 15 → Laptops
(10, 1), -- Dell Inspiron 15 → Laptops
(11, 1), -- MacBook Air 13" → Laptops
(15, 1), -- Dell Latitude 7420 → Laptops
(16, 1), -- Mac Studio → Laptops
(20, 1), -- Alienware m15 → Laptops, also an accessory

-- Smartphones
(2, 2),  -- Samsung Galaxy S23 → Smartphones
(12, 2), -- Samsung Galaxy A54 → Smartphones

-- Tablets (Sometimes used as Accessories or for Productivity)
(6, 3),  -- iPad Pro 12.9" → Tablets
(7, 3),  -- Samsung Galaxy Tab S8 → Tablets
(6, 5),  -- iPad Pro 12.9" → Accessories (Used with Keyboard Case)
(7, 5),  -- Galaxy Tab S8 → Accessories (Stylus, Keyboard Cases)

-- Televisions (Also used for Gaming & Streaming)
(4, 4),  -- LG OLED 55" → Televisions
(8, 4),  -- Sony Bravia 65" → Televisions
(9, 4),  -- LG NanoCell 75" → Televisions
(17, 4), -- Samsung QLED TV → Televisions
(4, 5),  -- LG OLED 55" → Accessories (Used with Wall Mounts & Audio Systems)
(17, 5), -- Samsung QLED TV → Accessories (Wall Mounts & Soundbars)

-- Gaming Consoles (Belongs to Televisions & Accessories)
(18, 4), -- PlayStation 5 → Televisions (for Gaming)
(18, 5), -- PlayStation 5 → Accessories (Extra Controllers, VR Headsets)

-- Accessories (Standalone electronics)
(3, 5),  -- Sony WH-1000XM5 → Accessories (Headphones)
(13, 5), -- Sony WF-1000XM4 → Accessories (Wireless Earbuds)
(14, 5), -- LG UltraGear Monitor → Accessories (Gaming Monitor)
(19, 5), -- LG Soundbar → Accessories (Audio Equipment)
(20, 5); -- Alienware m15 → Accessories (Gaming Laptop Add-ons)
