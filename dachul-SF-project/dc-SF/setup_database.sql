-- ============================================
-- daechul-SF 데이터베이스 설정 스크립트
-- MySQL 8.x용
-- ============================================

-- 1. 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS daechuldb 
  DEFAULT CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

-- 2. 사용자 생성 및 권한 부여
CREATE USER IF NOT EXISTS 'daechuluser'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON daechuldb.* TO 'daechuluser'@'localhost';
FLUSH PRIVILEGES;

-- 3. 데이터베이스 사용
USE daechuldb;

-- ============================================
-- 테이블 생성
-- ============================================

-- 4. 회원 테이블 (members)
CREATE TABLE IF NOT EXISTS members (
    id VARCHAR(50) PRIMARY KEY COMMENT '회원 아이디',
    password VARCHAR(200) NOT NULL COMMENT '비밀번호 (BCrypt 암호화)',
    name VARCHAR(100) NOT NULL COMMENT '회원 이름',
    email VARCHAR(200) NOT NULL COMMENT '이메일',
    role VARCHAR(20) DEFAULT 'MEMBER' COMMENT '권한 (MEMBER, ADMIN)',
    phone VARCHAR(20) COMMENT '전화번호',
    regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
    enabled BOOLEAN DEFAULT TRUE COMMENT '계정 활성화 여부'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. 회원 권한 테이블 (member_roles) - Spring Security용
CREATE TABLE IF NOT EXISTS member_roles (
    id VARCHAR(50) NOT NULL COMMENT '회원 아이디',
    role VARCHAR(50) NOT NULL COMMENT '권한 (ROLE_MEMBER, ROLE_ADMIN)',
    PRIMARY KEY (id, role),
    FOREIGN KEY (id) REFERENCES members(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. 게시판 테이블 (board)
CREATE TABLE IF NOT EXISTS board (
    seq INT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 번호',
    writer VARCHAR(50) NOT NULL COMMENT '작성자 ID',
    title VARCHAR(500) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    hit INT DEFAULT 0 COMMENT '조회수',
    regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    delflag BOOLEAN DEFAULT FALSE COMMENT '삭제 여부 (논리적 삭제)',
    FOREIGN KEY (writer) REFERENCES members(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. 댓글 테이블 (reply)
CREATE TABLE IF NOT EXISTS reply (
    rno INT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 번호',
    bno INT NOT NULL COMMENT '게시글 번호',
    replyText VARCHAR(500) NOT NULL COMMENT '댓글 내용',
    replyer VARCHAR(50) NOT NULL COMMENT '댓글 작성자',
    replydate TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '댓글 작성일시',
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '댓글 수정일시',
    deflag BOOLEAN DEFAULT FALSE COMMENT '삭제 여부 (논리적 삭제)',
    FOREIGN KEY (bno) REFERENCES board(seq) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. Remember Me 토큰 테이블 (persistent_logins) - Spring Security용
CREATE TABLE IF NOT EXISTS persistent_logins (
    username VARCHAR(64) NOT NULL COMMENT '사용자 아이디',
    series VARCHAR(64) PRIMARY KEY COMMENT '시리즈 토큰',
    token VARCHAR(64) NOT NULL COMMENT '인증 토큰',
    last_used TIMESTAMP NOT NULL COMMENT '마지막 사용 시간'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- 인덱스 생성 (이미 존재하면 무시)
-- ============================================

-- 인덱스가 없을 경우에만 생성 (프로시저 사용하지 않고 에러 무시)
CREATE INDEX idx_board_writer ON board(writer);
CREATE INDEX idx_board_regdate ON board(regdate DESC);
CREATE INDEX idx_members_email ON members(email);
CREATE INDEX idx_members_role ON members(role);
CREATE INDEX idx_reply_bno ON reply(bno);
CREATE INDEX idx_reply_regdate ON reply(replydate DESC);

-- ============================================
-- 테스트 데이터 삽입
-- ============================================

-- 관리자 계정 (비밀번호: admin123)
-- BCrypt 해시값
INSERT INTO members (id, password, name, email, role, phone, enabled) VALUES 
('admin', '$2a$10$N.zmdr9rW6K7e1SyRzd/I.eFD0PnYMwZz.6YwQxZ.9Nv6ynO4qP2e', '관리자', 'admin@daechul.com', 'ADMIN', '010-1234-5678', true)
ON DUPLICATE KEY UPDATE name = '관리자';

INSERT INTO member_roles (id, role) VALUES ('admin', 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE role = 'ROLE_ADMIN';

-- 일반 회원 계정 (비밀번호: user123)
INSERT INTO members (id, password, name, email, role, phone, enabled) VALUES 
('user01', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '홍길동', 'user01@daechul.com', 'MEMBER', '010-9876-5432', true)
ON DUPLICATE KEY UPDATE name = '홍길동';

INSERT INTO member_roles (id, role) VALUES ('user01', 'ROLE_MEMBER')
ON DUPLICATE KEY UPDATE role = 'ROLE_MEMBER';

-- 테스트 게시글
INSERT INTO board (writer, title, content) VALUES 
('admin', '공지사항입니다', '안녕하세요.\n\n대출 프로젝트 게시판에 오신 것을 환영합니다.\n\nSpring Framework + MyBatis + Spring Security로 구축되었습니다.'),
('user01', '첫 번째 게시글', '게시판 테스트 글입니다.\n잘 되는지 확인해보겠습니다.');

-- ============================================
-- 데이터 확인
-- ============================================

SELECT '=== 테이블 목록 ===' AS '';
SHOW TABLES;

SELECT '=== 회원 데이터 ===' AS '';
SELECT id, name, email, role, regdate FROM members;

SELECT '=== 회원 권한 ===' AS '';
SELECT * FROM member_roles;

SELECT '=== 게시글 데이터 ===' AS '';
SELECT seq, writer, title, hit, regdate FROM board ORDER BY seq DESC;

SELECT '=== 댓글 테이블 확인 ===' AS '';
SHOW TABLES LIKE 'reply';

-- ============================================
-- 완료!
-- ============================================
SELECT '데이터베이스 설정이 완료되었습니다!' AS 'Message';
