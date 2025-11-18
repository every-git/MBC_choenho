-- ============================================
-- 운영 서버 배포용 SQL 스크립트
-- 프로젝트: web-study-10
-- 실행 순서: 이 파일 하나만 실행하면 됩니다
-- ============================================

-- ============================================
-- 1. 데이터베이스 생성
-- ============================================
CREATE DATABASE IF NOT EXISTS edudb 
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE edudb;

-- ============================================
-- 2. 사용자 생성 및 권한 부여 (선택사항)
-- ============================================
-- 보안을 위해 별도 사용자 생성 권장
-- 비밀번호는 반드시 변경하세요!

-- DROP USER IF EXISTS 'edudb_user'@'%';
-- CREATE USER 'edudb_user'@'%' IDENTIFIED BY 'Change_This_Password!123';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON edudb.* TO 'edudb_user'@'%';
-- FLUSH PRIVILEGES;

-- ============================================
-- 3. Product 테이블 생성
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

-- 인덱스 생성 (성능 향상)
CREATE INDEX idx_product_name ON product(name);
CREATE INDEX idx_product_price ON product(price);

-- ============================================
-- 4. 샘플 데이터 삽입 (선택사항)
-- ============================================
-- 운영 서버에서는 샘플 데이터가 필요 없다면 이 섹션을 주석 처리하세요

INSERT INTO product (name, price, pictureurl, description) VALUES
('개념을 콕콕 잡아주는 데이터베이스', 27000, 'db.jpg', 
 '데이터베이스에 관한 모든 것을 쉽고 재미있게 정리한 교재입니다. 초보자도 쉽게 이해할 수 있도록 구성되어 있습니다.'),

('jQuery 기본편', 32000, 'jquery.jpg', 
 'jQuery의 기초부터 실전 활용까지 상세하게 설명합니다. 실무 예제를 통해 빠르게 학습할 수 있습니다.'),

('HTML5 웹 프로그래밍 입문', 25000, 'html5.jpg', 
 'HTML5의 새로운 기능과 API를 활용한 웹 프로그래밍 완벽 가이드입니다.'),

('오라클 SQL과 PL/SQL', 35000, 'oracle.jpg', 
 '오라클 데이터베이스의 SQL과 PL/SQL을 체계적으로 학습할 수 있는 교재입니다.'),

('짜파구리 라면', 4500, 'jjapaguri.jpg', 
 '짜파게티와 너구리의 완벽한 조화! 깊고 진한 맛이 일품입니다.'),

('처음처음 소주', 3500, 'soju.png', 
 '부드러운 목넘김과 깔끔한 뒷맛의 소주입니다. 도수 16.9도'),

('MFC 윈도우 프로그래밍', 38000, 'mfc.jpg', 
 'Visual C++ MFC를 이용한 윈도우 애플리케이션 개발 완벽 가이드입니다.'),

('Adobe Photoshop 완전정복', 28000, 'Image_fx.jpg', 
 '포토샵의 모든 기능을 상세하게 설명하는 실무 중심 교재입니다.'),

('디지털 이미지 편집 기초', 22000, 'Image_fx1.jpg', 
 '디지털 이미지 편집의 기초부터 고급 기법까지 단계별로 학습합니다.'),

('실전 이미지 프로세싱', 33000, 'Image_fx2.jpg', 
 '이미지 처리와 컴퓨터 비전의 실전 기법을 배우는 전문 교재입니다.');

-- ============================================
-- 5. 배포 후 확인 쿼리
-- ============================================
-- 테이블 목록 확인
SHOW TABLES;

-- 테이블 구조 확인
DESC product;

-- 데이터 확인
SELECT COUNT(*) AS '총 상품 수' FROM product;
SELECT * FROM product ORDER BY code DESC LIMIT 5;

-- 문자셋 확인 (한글 깨짐 방지)
SHOW VARIABLES LIKE 'character_set%';

-- ============================================
-- 배포 완료!
-- ============================================
-- 다음 단계:
-- 1. DBManager.java의 DB 연결 정보 수정
-- 2. WAR 파일 빌드 (mvn clean package)
-- 3. Tomcat에 배포
-- 4. 웹 애플리케이션 테스트
-- ============================================

