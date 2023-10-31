DROP DATABASE IF EXISTS bookmanagement;
 
CREATE DATABASE bookmanagement;

USE bookmanagement;

CREATE TABLE author (
	id INT AUTO_INCREMENT PRIMARY KEY,
    fullname NVARCHAR(255) NOT NULL,
    date_birth DATETIME NOT NULL
);

CREATE TABLE category (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name NVARCHAR(255) NOT NULL 
);

CREATE TABLE borrow_status (
	id VARCHAR(50) PRIMARY KEY UNIQUE,
    name NVARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_status (
	id VARCHAR(50) PRIMARY KEY UNIQUE,
    name NVARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE role (
	id VARCHAR(50) PRIMARY KEY,
    name NVARCHAR(255) NOT NULL 
);

CREATE Table user (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullname NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(10) NOT NULL UNIQUE,
    date_birth DATETIME NOT NULL,
    status_id VARCHAR(50) NOT NULL,
    role_id VARCHAR(50) NOT NULL,
	create_at DATETIME NOT NULL,
    FOREIGN KEY (status_id) REFERENCES user_status(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE user_session (
	id INT AUTO_INCREMENT PRIMARY KEY,
    session_id NVARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    is_active BOOLEAN NOT NULL,
    is_refresh_token BOOLEAN NOT NULL,
    create_at DATETIME NOT NULL,
    expire_at DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE book (
	id INT AUTO_INCREMENT PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    rating INT,
    description NVARCHAR(255) NOT NULL,
    author_id INT,
    category_id INT NOT NULL,
    release_date DATETIME NOT NULL,
	FOREIGN KEY (author_id) REFERENCES author(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE Table borrow_management (
	id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    status_id VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
    quantity INT NOT NULL,
    create_date DATETIME NOT NULL,
    borrow_date DATETIME NOT NULL,
    return_date DATETIME NOT NULL,
	FOREIGN KEY (book_id) REFERENCES book(id),
	FOREIGN KEY (status_id) REFERENCES borrow_status(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

INSERT INTO role (id, name) 
VALUES 
		('USER', 'USER'),
        ('ADMIN', 'ADMIN');
        

INSERT INTO user_status (id, name) 
VALUES 
		('SUSPENDED', 'Bị cấm'),
        ('DISABLE', 'Vô hiệu hóa'),
		('NOTACTIVATED', 'Chưa kích hoạt'),
        ('ACTIVATED', 'Đã kích hoạt');
        
INSERT INTO borrow_status (id, name) 
VALUES 
		('NOTAPPROVED', 'Chưa duyệt'),
        ('PAID', 'Đã trả'),
		('UNPAID', 'Chưa trả');
        












