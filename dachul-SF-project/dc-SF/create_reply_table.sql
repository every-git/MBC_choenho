-- ============================================
-- 댓글 테이블 생성 스크립트
-- 기존 데이터베이스에 댓글 테이블 추가용
-- ============================================

USE daechuldb;

-- 댓글 테이블 생성
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

-- 인덱스 생성
CREATE INDEX IF NOT EXISTS idx_reply_bno ON reply(bno);
CREATE INDEX IF NOT EXISTS idx_reply_regdate ON reply(replydate DESC);

-- 테이블 생성 확인
SELECT '=== 댓글 테이블 생성 완료 ===' AS '';
SHOW TABLES LIKE 'reply';

SELECT '=== 댓글 테이블 구조 확인 ===' AS '';
DESCRIBE reply;
