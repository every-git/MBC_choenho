# 데이터베이스 생성 가이드

## 📋 생성할 데이터베이스

1. **dcproject** - jdbctest 연결용
2. **springdb** - springdbuser 연결용

---

## 🔧 방법 1: SQL 스크립트 실행 (추천)

### jdbctest 연결로 dcproject 생성

1. Database Client 확장 프로그램에서 **jdbctest** 연결 선택
2. 새 쿼리 창 열기
3. 다음 SQL 실행:

```sql
CREATE DATABASE IF NOT EXISTS `dcproject`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

SHOW DATABASES LIKE 'dcproject';
```

### springdbuser 연결로 springdb 생성

1. Database Client 확장 프로그램에서 **springdbuser** 연결 선택
2. 새 쿼리 창 열기
3. 다음 SQL 실행:

```sql
CREATE DATABASE IF NOT EXISTS `springdb`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

SHOW DATABASES LIKE 'springdb';
```

---

## 🔧 방법 2: 터미널에서 실행

### jdbctest로 dcproject 생성

```bash
mysql -h 127.0.0.1 -P 3306 -u jdbctest -p -e "CREATE DATABASE IF NOT EXISTS dcproject DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;"
```

### root로 springdb 생성

```bash
mysql -h 127.0.0.1 -P 3306 -u root -p -e "CREATE DATABASE IF NOT EXISTS springdb DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;"
```

---

## 🔧 방법 3: SQL 파일 실행

`create_databases.sql` 파일을 실행하세요.

### Database Client에서:
1. 연결 선택 (jdbctest 또는 springdbuser)
2. SQL 파일 열기
3. 실행

### 터미널에서:
```bash
# jdbctest 연결로 실행
mysql -h 127.0.0.1 -P 3306 -u jdbctest -p < create_databases.sql

# 또는 root로 실행
mysql -h 127.0.0.1 -P 3306 -u root -p < create_databases.sql
```

---

## ✅ 생성 확인

SQL 실행 후 다음 명령어로 확인:

```sql
SHOW DATABASES;
```

다음 데이터베이스들이 보여야 합니다:
- `dcproject`
- `springdb`

---

## 📝 참고사항

- `IF NOT EXISTS`: 데이터베이스가 이미 존재해도 오류 없이 실행
- `utf8mb4`: 이모지 및 모든 유니코드 문자 지원
- `utf8mb4_unicode_ci`: 대소문자 구분 없는 정렬

---

## ⚠️ 권한 확인

만약 권한 오류가 발생하면:

1. **root 계정으로 접속하여 생성**
2. 또는 해당 사용자에게 권한 부여:

```sql
-- root로 접속 후 실행
GRANT ALL PRIVILEGES ON `dcproject`.* TO 'jdbctest'@'localhost';
GRANT ALL PRIVILEGES ON `dcproject`.* TO 'jdbctest'@'127.0.0.1';
FLUSH PRIVILEGES;
```




