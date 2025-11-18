-- ============================================
-- 상품관리 시스템 데이터베이스 스키마
-- 프로젝트: web-study-10
-- ============================================

-- 데이터베이스 생성 (필요시)
CREATE DATABASE IF NOT EXISTS edudb 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE edudb;

-- ============================================
-- Product 테이블 생성
-- ============================================
DROP TABLE IF EXISTS product;

CREATE TABLE product (
    code INT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 코드',
    name VARCHAR(100) NOT NULL COMMENT '상품명',
    price INT NOT NULL COMMENT '가격',
    pictureurl VARCHAR(50) COMMENT '이미지 파일명',
    description VARCHAR(1000) COMMENT '상품 설명',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) ENGINE=InnoDB 
  DEFAULT CHARSET=utf8mb4 
  COLLATE=utf8mb4_unicode_ci 
  COMMENT='상품 정보 테이블';

-- 인덱스 생성
CREATE INDEX idx_product_name ON product(name);
CREATE INDEX idx_product_price ON product(price);

-- ============================================
-- 테이블 생성 확인
-- ============================================
SHOW TABLES;
DESC product;

