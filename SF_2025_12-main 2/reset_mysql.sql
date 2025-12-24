-- MySQL root 비밀번호 재설정 및 데이터베이스 초기화 스크립트
-- MySQLWorkbench에서 root로 접속한 후 이 스크립트를 실행하세요

-- 📌 1) root 비밀번호 재설정 (1234로 설정)
ALTER USER 'root'@'localhost' IDENTIFIED BY '1234';
FLUSH PRIVILEGES;

-- 📌 2) 기존 사용자와 데이터베이스 삭제 (있다면)
DROP DATABASE IF EXISTS springdb;
DROP USER IF EXISTS 'springdbuser'@'%';
DROP USER IF EXISTS 'springdbuser'@'localhost';

-- 📌 3) Database 생성
CREATE DATABASE springdb;

-- 📌 4) 사용자 생성
CREATE USER 'springdbuser'@'%' IDENTIFIED BY '1234';
CREATE USER 'springdbuser'@'localhost' IDENTIFIED BY '1234';

-- 📌 5) 권한 부여
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'%';
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'localhost';

-- 권한 적용
FLUSH PRIVILEGES;

-- 📌 6) springdb 데이터베이스 선택
USE springdb;

-- 📌 7) table_board 테이블 생성
CREATE TABLE IF NOT EXISTS table_board(
    bno INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    writer VARCHAR(50) NOT NULL,
    regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delflag BOOLEAN DEFAULT FALSE
);

-- 📌 8) 확인
SELECT 'Database and user created successfully!' AS Status;
SHOW DATABASES;
SELECT User, Host FROM mysql.user WHERE User = 'springdbuser';
SHOW TABLES;

