create table movie
(
    movie_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_name  VARCHAR(255) NOT NULL,
    movie_uuid  VARCHAR(255) NOT NULL,
    create_time DATETIME     NOT NULL
) engine = innodb
  auto_increment = 1
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment = 'Movies table';


DROP PROCEDURE IF EXISTS insert_mock_movies;

DELIMITER $$

CREATE PROCEDURE insert_mock_movies()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 100000
        DO
            INSERT INTO movie (movie_name, movie_uuid, create_time)
            VALUES (CONCAT('Movie Name ', i),
                    CONCAT('uuid', i),
                    NOW());
            SET i = i + 1;
        END WHILE;
END$$

DELIMITER ;

-- 执行插入
CALL insert_mock_movies();
