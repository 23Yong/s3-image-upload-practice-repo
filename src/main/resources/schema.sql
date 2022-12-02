CREATE TABLE directory
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    directory_name      VARCHAR(255)    NOT NULL,
    parent_directory_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE image
(
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    image_name          VARCHAR(255)    NOT NULL,
    extension           VARCHAR(10)     NOT NULL,
    directory_id        BIGINT          NOT NULL,
    PRIMARY KEY (id)
);