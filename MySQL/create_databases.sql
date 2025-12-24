-- MySQL 데이터베이스 생성 스크립트
-- Workbench 연결 정보를 기반으로 작성

-- ============================================
-- 1. dcproject 데이터베이스 생성 (jdbctest 연결용)
-- ============================================
CREATE DATABASE IF NOT EXISTS `dcproject`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- dcproject 사용자에게 권한 부여 (jdbctest)
-- GRANT ALL PRIVILEGES ON `dcproject`.* TO 'jdbctest'@'localhost';
-- GRANT ALL PRIVILEGES ON `dcproject`.* TO 'jdbctest'@'127.0.0.1';
-- GRANT ALL PRIVILEGES ON `dcproject`.* TO 'jdbctest'@'%';
-- FLUSH PRIVILEGES;

-- 데이터베이스 확인
SHOW DATABASES LIKE 'dcproject';

-- ============================================
-- 2. springdb 데이터베이스 생성 (springdbuser 연결용)
-- ============================================
CREATE DATABASE IF NOT EXISTS `springdb`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- springdb 사용자에게 권한 부여 (root)
-- root는 이미 모든 권한을 가지고 있으므로 별도 권한 부여 불필요

-- 데이터베이스 확인
SHOW DATABASES LIKE 'springdb';

-- ============================================
-- 생성된 데이터베이스 목록 확인
-- ============================================
SHOW DATABASES;




