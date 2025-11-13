create table member(
    name varchar2(10),
    userid varchar2(10) primary key,
    pwd varchar2(10),
    email varchar2(30),
    phone varchar2(13),
    admin number(1) DEFAULT 0
);

SELECT * FROM member;

INSERT INTO member VALUES ('이소미', 'somi', 'somi123', 'gmd@naver.com', '010-1234-5678', 0);
INSERT INTO member VALUES ('김철수', 'chulsoo', 'chul123', 'hal12@naver.com', '010-9876-5432', 1);
INSERT INTO member VALUES ('박영희', 'younghee', 'young123', 'ph@naver.com', '010-1111-2222', 0);
INSERT INTO member VALUES ('최민수', 'minsu', 'minsu123', 'minsu@naver.com', '010-3333-4444', 0);
INSERT INTO member VALUES ('정다은', 'daeun', 'daeun123', 'daeun@naver.com', '010-5555-6666', 0);

SELECT * FROM member;
COMMIT;

SELECT name, userid FROM member;

SELECT * FROM member
WHERE userid = 'somi';

SELECT phone FROM member
WHERE userid = 'chulsoo';

