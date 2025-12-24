-- 완전한 테이블 설정 스크립트
-- 프로젝트에서 사용하는 모든 테이블 생성

USE springdb;

-- 1. tbl_board 테이블 (게시판)
CREATE TABLE IF NOT EXISTS tbl_board(
    bno INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    writer VARCHAR(50) NOT NULL,
    regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delflag BOOLEAN DEFAULT FALSE
);

-- 2. tbl_reply 테이블 (댓글)
CREATE TABLE IF NOT EXISTS tbl_reply(
    rno INT AUTO_INCREMENT PRIMARY KEY,
    bno INT NOT NULL,
    replyText VARCHAR(500) NOT NULL,
    replyer VARCHAR(50) NOT NULL,
    replydate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deflag BOOLEAN DEFAULT FALSE,
    INDEX idx_bno (bno)
);

-- 3. tbl_member 테이블 (회원 - sp2 프로젝트용)
CREATE TABLE IF NOT EXISTS tbl_member(
    mid VARCHAR(50) PRIMARY KEY,
    mpw VARCHAR(200) NOT NULL,
    mname VARCHAR(100) NOT NULL,
    regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 확인
SELECT '✓ All tables created successfully!' AS Status;
SHOW TABLES;
SELECT 
    'tbl_board' AS TableName, 
    COUNT(*) AS RecordCount 
FROM tbl_board
UNION ALL
SELECT 
    'tbl_reply' AS TableName, 
    COUNT(*) AS RecordCount 
FROM tbl_reply
UNION ALL
SELECT 
    'tbl_member' AS TableName, 
    COUNT(*) AS RecordCount 
FROM tbl_member;

