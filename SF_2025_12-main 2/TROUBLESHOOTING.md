# sp1 프로젝트 404 에러 해결 가이드

## 문제 진단

맥북 재설정 후 발생한 404 에러의 주요 원인:

1. **MySQL 데이터베이스/사용자 미설정** (가장 가능성 높음)
2. 프로젝트 빌드/배포 문제
3. Tomcat 서버 설정 문제

## 해결 순서

### 1단계: MySQL 데이터베이스 설정 (필수!)

MySQL이 없으면 Spring 애플리케이션이 시작 시 에러가 발생합니다.

**MySQLWorkbench 사용:**

1. MySQLWorkbench 실행
2. root 연결 (비밀번호 없이 또는 기존 비밀번호로)
3. `reset_mysql.sql` 파일 열기
4. 전체 스크립트 실행

또는 수동으로:

```sql
-- root 비밀번호 설정
ALTER USER 'root'@'localhost' IDENTIFIED BY '1234';
FLUSH PRIVILEGES;

-- 데이터베이스 및 사용자 생성
CREATE DATABASE IF NOT EXISTS springdb;
CREATE USER IF NOT EXISTS 'springdbuser'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'localhost';
FLUSH PRIVILEGES;

-- 테이블 생성
USE springdb;
CREATE TABLE IF NOT EXISTS table_board(
    bno INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    writer VARCHAR(50) NOT NULL,
    regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delflag BOOLEAN DEFAULT FALSE
);
```

### 2단계: 프로젝트 빌드 확인

**Eclipse/STS 사용 시:**
1. 프로젝트 우클릭 → `Clean...`
2. 프로젝트 우클릭 → `Build Project`
3. 또는 `Project` → `Clean...` → `sp1` 선택

**터미널에서 Maven 빌드:**
```bash
cd "/Volumes/Samsung X5/dev_study/MBC_choenho/SF_2025_12-main 2/sp1"
mvn clean install
```

### 3단계: Tomcat 서버 확인

1. **서버 정지**
   - Servers 탭에서 Tomcat 서버 우클릭 → `Stop`

2. **서버 정리**
   - Tomcat 서버 우클릭 → `Clean...`
   - 또는 `Clean Tomcat Work Directory`

3. **프로젝트 다시 추가**
   - Tomcat 서버 우클릭 → `Add and Remove...`
   - sp1을 Available에서 Configured로 이동

4. **서버 시작**
   - Tomcat 서버 우클릭 → `Start`

### 4단계: 접속 확인

서버 시작 후:

1. **루트 경로:** `http://localhost:8080/`
   - `index.jsp` → `/board/list`로 리다이렉트

2. **게시판 목록:** `http://localhost:8080/board/list`
   - 정상 작동하면 게시판 목록 페이지 표시

3. **샘플 컨트롤러:** `http://localhost:8080/sample/ex1`
   - 테스트용 엔드포인트

### 5단계: 로그 확인

**Eclipse Console 또는 로그 파일 확인:**

에러 메시지가 있다면:
- MySQL 연결 오류: 1단계 다시 확인
- 클래스 로딩 오류: 2단계 빌드 다시 확인
- 서블릿 매핑 오류: web.xml 확인

## 예상되는 에러 메시지

### MySQL 연결 오류
```
Communications link failure
Access denied for user 'springdbuser'@'localhost'
```
→ 1단계 MySQL 설정 확인

### 클래스 Not Found 오류
```
java.lang.ClassNotFoundException
```
→ 2단계 프로젝트 빌드 필요

### 404 Not Found
```
HTTP Status 404 – Not Found
```
→ 서버 로그 확인 후 위 단계 순서대로 확인

## 빠른 체크리스트

- [ ] MySQL 서버 실행 중?
- [ ] `springdb` 데이터베이스 존재?
- [ ] `springdbuser` 사용자 존재 및 권한 부여?
- [ ] 프로젝트 빌드 완료?
- [ ] Tomcat 서버에 프로젝트 배포됨?
- [ ] 서버 로그에 에러 없음?
