-- 빠른 MySQL 설정 스크립트
-- MySQLWorkbench에서 root로 접속한 후 이 스크립트를 실행하세요

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS springdb;

-- 기존 사용자 삭제 (있다면)
DROP USER IF EXISTS 'springdbuser'@'localhost';
DROP USER IF EXISTS 'springdbuser'@'%';

-- 사용자 생성
CREATE USER 'springdbuser'@'localhost' IDENTIFIED BY '1234';
CREATE USER 'springdbuser'@'%' IDENTIFIED BY '1234';

-- 권한 부여
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'localhost';
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'%';
FLUSH PRIVILEGES;

-- springdb 사용
USE springdb;

-- 테이블 생성
CREATE TABLE IF NOT EXISTS table_board(
    bno INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    writer VARCHAR(50) NOT NULL,
    regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delflag BOOLEAN DEFAULT FALSE
);

-- 확인
SELECT '✓ Database and user created successfully!' AS Status;
SHOW DATABASES;
SELECT User, Host FROM mysql.user WHERE User = 'springdbuser';
SHOW TABLES;
