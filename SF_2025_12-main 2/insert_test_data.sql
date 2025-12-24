-- 테스트 게시글 및 댓글 데이터 삽입 스크립트
USE springdb;

-- 기존 테스트 데이터 삭제 (선택사항)
-- DELETE FROM tbl_reply WHERE bno IN (49899, 49898, 49897);
-- DELETE FROM tbl_board WHERE bno IN (49899, 49898, 49897);

-- 테스트 게시글 삽입 (tbl_board 테이블 사용)
INSERT INTO tbl_board (bno, title, content, writer, delflag) VALUES 
(1, '첫 번째 테스트 게시글', '이것은 첫 번째 테스트 게시글의 내용입니다. Spring Boot와 MyBatis를 사용한 게시판 프로젝트입니다.', '테스트작성자1', FALSE),
(2, '두 번째 테스트 게시글', '이것은 두 번째 테스트 게시글의 내용입니다. 다양한 기능을 테스트하기 위한 샘플 데이터입니다.', '테스트작성자2', FALSE),
(3, '세 번째 테스트 게시글', '이것은 세 번째 테스트 게시글의 내용입니다. 댓글 기능을 테스트하기 위한 게시글입니다.', '테스트작성자3', FALSE),
(4, '네 번째 테스트 게시글', '이것은 네 번째 테스트 게시글의 내용입니다. 페이징 기능을 테스트하기 위한 게시글입니다.', '테스트작성자1', FALSE),
(5, '다섯 번째 테스트 게시글', '이것은 다섯 번째 테스트 게시글의 내용입니다. 검색 기능을 테스트하기 위한 게시글입니다.', '테스트작성자2', FALSE)
ON DUPLICATE KEY UPDATE title=VALUES(title), content=VALUES(content), writer=VALUES(writer);

-- 테스트 댓글 삽입 (1번 게시글에 댓글 5개)
INSERT INTO tbl_reply (bno, replyText, replyer, deflag) VALUES
(1, '첫 번째 게시글의 첫 번째 댓글입니다. 좋은 내용이네요!', '댓글작성자1', FALSE),
(1, '첫 번째 게시글의 두 번째 댓글입니다. 공감합니다.', '댓글작성자2', FALSE),
(1, '첫 번째 게시글의 세 번째 댓글입니다. 추가 정보가 필요합니다.', '댓글작성자3', FALSE),
(1, '첫 번째 게시글의 네 번째 댓글입니다. 감사합니다!', '댓글작성자1', FALSE),
(1, '첫 번째 게시글의 다섯 번째 댓글입니다. 유용한 정보네요.', '댓글작성자2', FALSE);

-- 테스트 댓글 삽입 (2번 게시글에 댓글 3개)
INSERT INTO tbl_reply (bno, replyText, replyer, deflag) VALUES
(2, '두 번째 게시글의 첫 번째 댓글입니다.', '댓글작성자1', FALSE),
(2, '두 번째 게시글의 두 번째 댓글입니다.', '댓글작성자2', FALSE),
(2, '두 번째 게시글의 세 번째 댓글입니다.', '댓글작성자3', FALSE);

-- 테스트 댓글 삽입 (3번 게시글에 댓글 4개)
INSERT INTO tbl_reply (bno, replyText, replyer, deflag) VALUES
(3, '세 번째 게시글의 첫 번째 댓글입니다. 댓글 기능이 잘 작동하네요!', '댓글작성자1', FALSE),
(3, '세 번째 게시글의 두 번째 댓글입니다.', '댓글작성자2', FALSE),
(3, '세 번째 게시글의 세 번째 댓글입니다. 테스트 중입니다.', '댓글작성자3', FALSE),
(3, '세 번째 게시글의 네 번째 댓글입니다.', '댓글작성자1', FALSE);

-- 확인 쿼리
SELECT '✓ Test data inserted successfully!' AS Status;
SELECT COUNT(*) AS '게시글 수' FROM tbl_board WHERE delflag = FALSE;
SELECT COUNT(*) AS '댓글 수' FROM tbl_reply WHERE deflag = FALSE;
SELECT bno, title, writer, 
       (SELECT COUNT(*) FROM tbl_reply WHERE tbl_reply.bno = tbl_board.bno AND deflag = FALSE) AS reply_count
FROM tbl_board 
WHERE delflag = FALSE 
ORDER BY bno;
