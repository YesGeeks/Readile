DROP DATABASE IF EXISTS Readile;
CREATE DATABASE Readile;
USE Readile;

CREATE TABLE IF NOT EXISTS Book (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(128) NOT NULL,
    description VARCHAR(512) DEFAULT NULL,
    cover_id VARCHAR(1024) NOT NULL,
    length INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Author (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS Author_Book (
    authorId INT,
    book_id INT,
    PRIMARY KEY (authorId, book_id),
    FOREIGN KEY (authorId) REFERENCES Author(id),
    FOREIGN KEY (book_id) REFERENCES Book(id)
);

CREATE TABLE IF NOT EXISTS Category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Book_Category (
    category_id INT,
    book_id INT,
    PRIMARY KEY (category_id, book_id),
    FOREIGN KEY (category_id) REFERENCES Category(id),
    FOREIGN KEY (book_id) REFERENCES Book(id)
);

CREATE TABLE IF NOT EXISTS User_Profile (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL,
    email VARCHAR(64) UNIQUE NOT NULL,
    profile_image VARCHAR(1024)
);

CREATE TABLE IF NOT EXISTS User_Book (
    user_id INT,
    book_id INT,
    current_page INT NOT NULL,
    startDate DATE DEFAULT NULL,
    endDate DATE DEFAULT NULL,
    rating ENUM ('ONE_STAR', 'TWO_STARS', 'THREE_STARS', 'FOUR_STARS', 'FIVE_STARS') DEFAULT 'ONE_STAR',
    status ENUM ('TO_READ', 'CURRENTLY_READING', 'READ') DEFAULT 'TO_READ',
    PRIMARY KEY (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES User_Profile(id),
    FOREIGN KEY (book_id) REFERENCES Book(id) 
);

CREATE TABLE IF NOT EXISTS Highlight (
    id INT,
    book_id INT,
    highlight VARCHAR(512) NOT NULL,
    PRIMARY KEY (id, book_id),
    FOREIGN KEY (book_id) REFERENCES Book(id)
);

CREATE TABLE IF NOT EXISTS Login_Info (
    user_id INT NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User_Profile(id)
);

DELIMITER $$
CREATE TRIGGER encrypt_password
BEFORE INSERT ON Login_Info FOR EACH ROW
BEGIN
    SET NEW.password = MD5(NEW.password);
END $$