-- 오류 원인 요약 (한글 설명):
-- ORA-00942: table or view "SYSTEM"."MEMBER" does not exist
-- 이 오류는 현재 접속한 스키마(또는 USER)가 SYSTEM이어서
-- "MEMBER" 테이블을 SYSTEM 스키마에서 찾으려 했지만 테이블이 없을 때 발생합니다.
-- 보통 원인은 다음 중 하나입니다:
--  1) 테이블이 다른 스키마(예: APPUSER)에 생성되어 있고 현재 사용자에게 권한이 없거나 동의어가 없음
--  2) CREATE TABLE이 실제로 실행되지 않았음(오류로 인해 생성되지 않음)
--  3) 잘못된 계정(SYSTEM)으로 작업함 — 애플리케이션 테이블을 SYSTEM에 만들지 마세요.

-- 1) 진단용 쿼리 (현재 접속한 사용자와 MEMBER 테이블 존재 여부 확인)
SELECT USER FROM dual; -- 현재 접속 사용자
SELECT sys_context('USERENV','CURRENT_SCHEMA') FROM dual; -- 현재 스키마

-- 데이터베이스 전체에서 MEMBER 테이블이 어디에 있는지 확인
SELECT owner, table_name
FROM all_tables
WHERE table_name = 'MEMBER';

-- 현재 사용자 스키마에 MEMBER가 있는지 확인
SELECT table_name FROM user_tables WHERE table_name = 'MEMBER';

-- 만약 다른 스키마에 존재하고 접근 권한이 없다면 아래 쿼리로 권한/동의어 확인
SELECT owner, grantee, privilege FROM all_tab_privs WHERE table_name = 'MEMBER';
SELECT owner, synonym_name, table_owner, table_name FROM all_synonyms WHERE synonym_name = 'MEMBER';

-- 2) 권장 수정(안전한 방법)
-- 권장: 애플리케이션용 전용 사용자(appuser)를 만들고 그 계정으로 테이블을 생성하세요.
-- 아래 CREATE USER / GRANT 구문은 DBA(또는 SYSTEM) 계정으로 실행해야 합니다. 주석 처리되어 있습니다.
-- (관리자 권한으로 한 번만 실행)
--
-- CREATE USER appuser IDENTIFIED BY appuser_pass;
-- GRANT CREATE SESSION, CREATE TABLE TO appuser;
-- -- 필요시 추가 권한: CREATE SEQUENCE, CREATE VIEW 등
--
-- 3) 실제 테이블 생성 및 데이터 입력 (appuser로 CONNECT 후 실행하세요)
-- 아래 블록은 반드시 애플리케이션 계정(appuser)으로 접속한 상태에서 실행하세요.
-- SQL*Plus나 SQL Developer에서 한 번에 실행하려면 CONNECT 명령으로 전환하세요.

-- 예: SQL*Plus에서
-- CONNECT appuser/appuser_pass

CREATE TABLE member (
    name  VARCHAR2(10),
    userid VARCHAR2(10) PRIMARY KEY,
    pwd   VARCHAR2(10),
    email VARCHAR2(30),
    phone VARCHAR2(13),
    admin NUMBER(1) DEFAULT 0
);

-- 데이터 입력
INSERT INTO member (name, userid, pwd, email, phone, admin)
VALUES ('이소미', 'somi', 'somi123', 'gmd@naver.com', '010-1234-5678', 0);
INSERT INTO member (name, userid, pwd, email, phone, admin)
VALUES ('김철수', 'chulsoo', 'chul123', 'hal12@naver.com', '010-9876-5432', 1);
INSERT INTO member (name, userid, pwd, email, phone, admin)
VALUES ('박영희', 'younghee', 'young123', 'ph@naver.com', '010-1111-2222', 0);

COMMIT;

-- 간단 조회 예제
SELECT * FROM member;
SELECT name, userid FROM member;
SELECT * FROM member WHERE userid = 'somi';
SELECT phone FROM member WHERE userid = 'chulsoo';

-- 만약 테이블이 다른 스키마(other_schema)에 있고 그 테이블을 사용하고 싶다면
-- (owner가 다른 스키마일 경우) 아래처럼 스키마명을 붙여 사용하거나
-- 스키마 소유자가 권한을 부여해야 합니다.
-- 예: SELECT * FROM other_schema.MEMBER;
-- 또는 소유자가 권한 부여: GRANT SELECT, INSERT ON member TO your_user;
-- 또는 your_user에서 동의어 생성: CREATE SYNONYM member FOR other_schema.member;

-- 주의: 절대 애플리케이션 테이블을 SYSTEM 또는 SYS 스키마에 생성하지 마세요.