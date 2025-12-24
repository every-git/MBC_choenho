# MySQL 설정 가이드

## 문제 상황
- MySQL 서버는 실행 중이지만 root 비밀번호가 설정되지 않았거나 인증 실패
- 데이터베이스와 사용자가 사라진 상태

## 해결 방법

### 방법 1: MySQLWorkbench 사용 (가장 쉬움)

1. **MySQLWorkbench 실행**
2. **root 연결을 더블클릭** (비밀번호 없이 연결 시도)
   - 만약 비밀번호를 요구하면 비워두고 OK
   - 연결이 안 되면 아래 "방법 2" 참조
3. **쿼리 창에서 `reset_mysql.sql` 파일 열기**
   - File → Open SQL Script → `reset_mysql.sql` 선택
4. **전체 스크립트 실행** (번개 아이콘 클릭 또는 Cmd+Shift+Enter)

### 방법 2: MySQL 서버 안전 모드로 재시작 (비밀번호 재설정)

터미널에서 실행:

```bash
# 1. MySQL 서버 중지 (MySQLWorkbench 또는 시스템 설정에서)
# 또는:
sudo /usr/local/mysql/support-files/mysql.server stop

# 2. 안전 모드로 MySQL 시작
sudo /usr/local/mysql/bin/mysqld_safe --skip-grant-tables &

# 3. MySQL 접속 (비밀번호 없이)
/usr/local/mysql/bin/mysql -u root

# 4. MySQL 프롬프트에서 실행:
USE mysql;
UPDATE user SET authentication_string=PASSWORD('1234') WHERE User='root';
FLUSH PRIVILEGES;
EXIT;

# 5. MySQL 서버 재시작 (정상 모드)
sudo /usr/local/mysql/support-files/mysql.server restart
```

### 방법 3: 완전 초기화 (모든 데이터 삭제)

**주의: 이 방법은 모든 데이터베이스를 삭제합니다!**

```bash
# MySQL 데이터 디렉토리 백업 (선택사항)
sudo cp -r /usr/local/mysql/data /usr/local/mysql/data_backup

# MySQL 서버 중지
sudo /usr/local/mysql/support-files/mysql.server stop

# 데이터 디렉토리 삭제
sudo rm -rf /usr/local/mysql/data

# MySQL 초기화
sudo /usr/local/mysql/bin/mysqld --initialize-insecure --user=mysql

# MySQL 서버 시작
sudo /usr/local/mysql/support-files/mysql.server start

# 접속 (비밀번호 없이)
/usr/local/mysql/bin/mysql -u root

# MySQL 프롬프트에서 reset_mysql.sql 내용 실행
```

## 확인 사항

설정 완료 후 다음을 확인하세요:

1. **VS Code MySQL 익스텐션에서 연결 확인**
   - root 사용자: 비밀번호 `1234`
   - 또는 springdbuser 사용자: 비밀번호 `1234`

2. **데이터베이스 확인**
   ```sql
   SHOW DATABASES;
   USE springdb;
   SHOW TABLES;
   ```

3. **Spring 애플리케이션 연결 테스트**
   - `sp1` 또는 `sp2` 프로젝트 실행
   - 데이터베이스 연결 로그 확인

## 기본 설정 정보

- **데이터베이스 이름**: springdb
- **사용자 이름**: springdbuser
- **비밀번호**: 1234
- **호스트**: localhost
- **포트**: 3306

