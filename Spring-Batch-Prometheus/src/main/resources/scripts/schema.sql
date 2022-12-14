--SELECT user,authentication_string,plugin,host FROM mysql.user;
--GRANT ALL PRIVILEGES ON *.* TO 'sa'@'%' WITH GRANT OPTION;
--CREATE USER 'sa'@'172.19.0.1' IDENTIFIED BY 'sa01020304';
--GRANT ALL PRIVILEGES ON *.* TO 'sa'@'172.19.0.1' WITH GRANT OPTION;
--flush privileges;

CREATE TABLE IF NOT EXISTS product (
    `id` BIGINT(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(64) NOT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `product_type` ENUM('PRODUCT_1', 'PRODUCT_2', 'PRODUCT_3', 'PRODUCT_4') DEFAULT 'PRODUCT_1' NOT NULL,
    is_main BIT(1) DEFAULT b'1' NOT NULL,
    created_at DATE
);