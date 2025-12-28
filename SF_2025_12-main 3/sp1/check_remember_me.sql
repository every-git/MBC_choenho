-- Remember Me 토큰 확인 스크립트

-- 1. 테이블 존재 확인
SHOW TABLES LIKE 'persistent_logins';

-- 2. 테이블 구조 확인
DESCRIBE persistent_logins;

-- 3. 현재 저장된 토큰 확인
SELECT * FROM persistent_logins;

-- 4. 특정 사용자의 토큰 확인 (user99로 로그인했다면)
SELECT * FROM persistent_logins WHERE username = 'user99';

-- 5. 토큰 개수 확인
SELECT COUNT(*) as token_count FROM persistent_logins;

