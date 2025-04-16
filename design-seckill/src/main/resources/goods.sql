CREATE
DATABASE IF NOT EXISTS goods;
USE
goods;

CREATE TABLE `product`
(
    `id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(255) NOT NULL,
    `stock` INT          NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO product (name, stock)
VALUES ('iPhone 15', 100);
