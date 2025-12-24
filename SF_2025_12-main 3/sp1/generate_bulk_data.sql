-- 대량 테스트 데이터 생성 스크립트
-- 게시글 50000개 생성
-- 마지막 10개 게시글에 댓글 100개씩 생성

USE springdb;

-- 기존 데이터 확인
SELECT COUNT(*) as '현재 게시글 수' FROM tbl_board;
SELECT COUNT(*) as '현재 댓글 수' FROM tbl_reply;

-- 게시글 50000개 생성
SET @start_time = NOW();
SET @counter = 1;

-- 프로시저를 사용하여 대량 데이터 생성
DELIMITER $$

DROP PROCEDURE IF EXISTS generate_boards$$

CREATE PROCEDURE generate_boards(IN total_count INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE writer_num INT;
    
    WHILE i <= total_count DO
        SET writer_num = ((i - 1) % 100) + 1;
        
        INSERT INTO tbl_board(title, content, writer, regdate, updatedate, delflag)
        VALUES (
            CONCAT('테스트 게시글 제목 ', i),
            CONCAT('이것은 테스트 게시글 ', i, '번의 내용입니다. 페이징과 검색 기능을 테스트하기 위한 샘플 데이터입니다.'),
            CONCAT('작성자', writer_num),
            NOW(),
            NOW(),
            FALSE
        );
        
        IF i % 1000 = 0 THEN
            SELECT CONCAT('게시글 ', i, '개 생성 완료') as progress;
        END IF;
        
        SET i = i + 1;
    END WHILE;
END$$

DELIMITER ;

-- 게시글 생성 실행
CALL generate_boards(50000);

-- 생성된 게시글 수 확인
SELECT COUNT(*) as '생성된 게시글 수' FROM tbl_board;

-- 마지막 게시글의 bno 확인
SELECT MAX(bno) as '마지막 게시글 bno' FROM tbl_board;

-- 마지막 10개 게시글에 댓글 100개씩 생성
DELIMITER $$

DROP PROCEDURE IF EXISTS generate_replies$$

CREATE PROCEDURE generate_replies(IN start_bno INT, IN board_count INT, IN replies_per_board INT)
BEGIN
    DECLARE board_idx INT DEFAULT 0;
    DECLARE reply_idx INT DEFAULT 1;
    DECLARE current_bno INT;
    DECLARE replyer_num INT;
    
    WHILE board_idx < board_count DO
        SET current_bno = start_bno - board_idx;
        
        SET reply_idx = 1;
        WHILE reply_idx <= replies_per_board DO
            SET replyer_num = ((reply_idx - 1) % 50) + 1;
            
            INSERT INTO tbl_reply(bno, replyText, replyer, replydate, updatedate, deflag)
            VALUES (
                current_bno,
                CONCAT('게시글 ', current_bno, '번의 ', reply_idx, '번째 댓글입니다. 테스트용 댓글 데이터입니다.'),
                CONCAT('댓글작성자', replyer_num),
                NOW(),
                NOW(),
                FALSE
            );
            
            SET reply_idx = reply_idx + 1;
        END WHILE;
        
        IF (board_idx + 1) % 5 = 0 THEN
            SELECT CONCAT('게시글 ', (board_idx + 1), '개에 댓글 생성 완료') as progress;
        END IF;
        
        SET board_idx = board_idx + 1;
    END WHILE;
END$$

DELIMITER ;

-- 마지막 게시글의 bno를 변수에 저장
SET @last_bno = (SELECT MAX(bno) FROM tbl_board);

-- 댓글 생성 실행 (마지막 10개 게시글에 각각 100개씩)
CALL generate_replies(@last_bno, 10, 100);

-- 생성된 댓글 수 확인
SELECT COUNT(*) as '생성된 댓글 수' FROM tbl_reply;

-- 최종 통계
SELECT 
    (SELECT COUNT(*) FROM tbl_board) as '전체 게시글 수',
    (SELECT COUNT(*) FROM tbl_reply) as '전체 댓글 수',
    (SELECT COUNT(*) FROM tbl_reply WHERE bno >= @last_bno - 9) as '마지막 10개 게시글의 댓글 수';

-- 프로시저 정리
DROP PROCEDURE IF EXISTS generate_boards;
DROP PROCEDURE IF EXISTS generate_replies;

SELECT '데이터 생성 완료!' as result;

