-- MySQL 데이터베이스 및 사용자 초기화 스크립트
-- 이 스크립트를 MySQL 서버에서 실행하세요 (root 권한 필요)

-- 📌 1) Database 생성 (이미 존재하면 에러 무시)
CREATE DATABASE IF NOT EXISTS springdb;

-- 📌 2) 사용자 생성 (이미 존재하면 에러 무시)
-- 모든 호스트에서 접근 가능한 사용자
CREATE USER IF NOT EXISTS 'springdbuser'@'%' IDENTIFIED BY '1234';

-- localhost에서 접근 가능한 사용자 (로컬 개발용)
CREATE USER IF NOT EXISTS 'springdbuser'@'localhost' IDENTIFIED BY '1234';

-- 📌 3) 권한 부여
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'%';
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'localhost';

-- 권한 적용
FLUSH PRIVILEGES;

-- 📌 4) springdb 데이터베이스 선택
USE springdb;

-- 📌 5) table_board 테이블 생성 (이미 존재하면 에러 무시)
CREATE TABLE IF NOT EXISTS table_board(
    bno INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    writer VARCHAR(50) NOT NULL,
    regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delflag BOOLEAN DEFAULT FALSE
);

-- 📌 확인용 쿼리
SHOW DATABASES;
SELECT User, Host FROM mysql.user WHERE User = 'springdbuser';
SHOW TABLES;
