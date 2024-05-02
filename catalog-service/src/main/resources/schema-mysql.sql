USE `catalog-db`;

create table if not exists books (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    isbn BIGINT UNIQUE,
    catalog_id VARCHAR(36),
    title VARCHAR(255),
    collection VARCHAR(255),
    edition VARCHAR(255),
    publisher VARCHAR(255),
    synopsis VARCHAR(550),
    language VARCHAR(50),
    status VARCHAR(50),
    first_name VARCHAR(50),
    last_name VARCHAR(50)
    );

create table if not exists catalogs (
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    catalog_id VARCHAR(36),
    type VARCHAR(50),
    size INTEGER
    );
