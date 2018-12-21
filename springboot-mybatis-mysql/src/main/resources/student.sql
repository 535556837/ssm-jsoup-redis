CREATE DATABASE student;
USE student;

CREATE TABLE student(
id      BIGINT PRIMARY KEY AUTO_INCREMENT,
name    VARCHAR(30) NOT NULL,
gender  VARCHAR(10) NOT NULL,
course	VARCHAR(30) NOT NULL,
addTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO student(name,gender,course)VALUES('Jack','male','Chinese');
INSERT INTO student(name,gender,course)VALUES('Tom','male','Computer Science');
INSERT INTO student(name,gender,course)VALUES('Marry','female','Molecular Biology');