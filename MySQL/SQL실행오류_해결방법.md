# SQL 실행 오류 해결 방법

## 🔍 문제 진단

SQL 파일 실행 시 처음부터 오류가 발생하는 경우, 다음을 확인해야 합니다:

---

## 1️⃣ 비밀번호 확인

### 테스트 방법:
```bash
# 연결 테스트
mysql -h localhost -P 3306 -u root -p

# 또는
mysql -h 127.0.0.1 -P 3306 -u root -p
```

**오류 메시지 확인:**
- `Access denied for user 'root'@'localhost'` → 비밀번호가 잘못됨
- `Can't connect to MySQL server` → MySQL 서버가 실행되지 않음

---

## 2️⃣ 간단한 SQL 파일로 테스트

복잡한 SQL 대신 간단한 버전으로 테스트:

### 방법 1: Database Client에서 직접 실행

1. Database Client에서 `springdbuser` 연결 선택
2. 새 쿼리 창 열기
3. 다음 SQL을 하나씩 실행:

```sql
-- 1단계: 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS `springdb`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- 2단계: 데이터베이스 선택
USE `springdb`;

-- 3단계: 테이블 생성 (하나씩)
CREATE TABLE `simple_todo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `done` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

### 방법 2: 간단한 SQL 파일 사용

`springdb_structure_safe.sql` 파일을 사용하세요. 이 파일은:
- 복잡한 mysqldump 설정 제거
- 간단한 CREATE 문만 포함
- 외래키 제약조건 처리 포함

---

## 3️⃣ 데이터베이스가 이미 존재하는 경우

이미 `springdb` 데이터베이스가 있고 테이블이 있다면:

### 옵션 1: 기존 데이터베이스 삭제 후 재생성 (주의!)
```sql
DROP DATABASE IF EXISTS `springdb`;
-- 그 다음 springdb_structure.sql 실행
```

### 옵션 2: 테이블만 재생성
```sql
USE `springdb`;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `tbl_reply`;
DROP TABLE IF EXISTS `tbl_board`;
DROP TABLE IF EXISTS `tbl_member`;
DROP TABLE IF EXISTS `simple_todo`;
SET FOREIGN_KEY_CHECKS = 1;
-- 그 다음 테이블 생성문 실행
```

---

## 4️⃣ 권한 문제

### root 계정 권한 확인:
```sql
SHOW GRANTS FOR 'root'@'localhost';
```

### 필요 권한 부여 (root로 접속 후):
```sql
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

---

## 5️⃣ MySQL 서버 상태 확인

```bash
# MySQL 서비스 상태 확인 (macOS)
brew services list | grep mysql

# 또는
sudo /usr/local/mysql/support-files/mysql.server status

# MySQL 재시작 (필요시)
brew services restart mysql
# 또는
sudo /usr/local/mysql/support-files/mysql.server restart
```

---

## 🎯 권장 해결 순서

1. **연결 테스트**: `test_connection.sh` 실행하여 연결 확인
2. **간단한 SQL로 테스트**: `springdb_structure_safe.sql` 사용
3. **단계별 실행**: SQL을 나눠서 하나씩 실행하며 오류 위치 확인
4. **비밀번호 재설정**: 필요시 root 비밀번호 재설정

---

## 💡 Database Client에서 실행 시 주의사항

1. **연결 선택**: 올바른 연결(`springdbuser`) 선택했는지 확인
2. **전체 실행 vs 부분 실행**: 
   - 전체 실행: Ctrl+A (전체 선택) → 실행
   - 부분 실행: 원하는 부분만 선택 → 실행
3. **에러 메시지 확인**: 오류 발생 시 정확한 오류 메시지 확인

---

## 🔧 즉시 시도해볼 것

### 방법 1: 간단한 버전 사용
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p < database_exports/springdb_structure_safe.sql
```

### 방법 2: Database Client에서 단계별 실행
1. CREATE DATABASE 부분만 실행
2. USE 문 실행
3. 테이블 하나씩 생성

어떤 오류 메시지가 나타나는지 알려주시면 더 정확히 도와드릴 수 있습니다!



