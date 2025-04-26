-- 重新创建外键约束
-- 电影类型表
CREATE TABLE movie_types
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL COMMENT '类型名称'
) COMMENT = '电影类型';

-- 演员表
CREATE TABLE actors
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '演员名称'
) COMMENT = '演员';

-- 电影表
CREATE TABLE movies
(
    movie_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_name    VARCHAR(255) COMMENT '电影名',
    movie_status  TINYINT(1) COMMENT '状态 1启用 0禁用',
    create_time   DATETIME,
    update_time   DATETIME,
    remarks       VARCHAR(255),
    movie_type_id BIGINT,
    CONSTRAINT fk_type FOREIGN KEY (movie_type_id) REFERENCES movie_types (id)
) COMMENT = '电影表';

-- 电影和演员多对多关系表
CREATE TABLE movie_actors
(
    movie_id BIGINT,
    actor_id BIGINT,
    PRIMARY KEY (movie_id, actor_id),
    CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movies (movie_id),
    CONSTRAINT fk_actor FOREIGN KEY (actor_id) REFERENCES actors (id)
) COMMENT = '电影演员中间表';

-- 插入 mock 数据
INSERT INTO movie_types(type_name)
VALUES ('动作'),
       ('喜剧'),
       ('爱情'),
       ('科幻'),
       ('恐怖');

INSERT INTO actors(name)
VALUES ('张三'),
       ('李四'),
       ('王五'),
       ('赵六'),
       ('周七');

INSERT INTO movies(movie_name, movie_status, create_time, update_time, remarks, movie_type_id)
VALUES ('电影A', 1, NOW(), NOW(), '备注A', 1),
       ('电影B', 1, NOW(), NOW(), '备注B', 2),
       ('电影C', 0, NOW(), NOW(), '备注C', 3),
       ('电影D', 1, NOW(), NOW(), '备注D', 4),
       ('电影E', 0, NOW(), NOW(), '备注E', 5);

-- 建立关联关系
INSERT INTO movie_actors(movie_id, actor_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 3),
       (3, 4),
       (4, 4),
       (4, 5),
       (5, 1),
       (5, 5);
