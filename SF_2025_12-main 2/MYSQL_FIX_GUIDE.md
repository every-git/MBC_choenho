# MySQL 접속 문제 해결 가이드

## 문제 상황
- MySQLWorkbench에서 접속하면 종료됨
- VS Code MySQL 익스텐션에서 접속 안 됨

## 원인
맥북 재설정 후 MySQL이 제대로 설치되지 않았거나 설정이 망가진 상태

## 해결 방법

### 방법 1: MySQL 완전 재설치 (권장)

**1단계: 기존 MySQL 완전 제거**

터미널에서 실행:
```bash
# MySQL 서비스 중지
sudo /usr/local/mysql/support-files/mysql.server stop

# 또는 launchd 서비스 제거
sudo launchctl unload -w /Library/LaunchDaemons/com.oracle.oss.mysql.mysqld.plist
sudo rm /Library/LaunchDaemons/com.oracle.oss.mysql.mysqld.plist

# MySQL 관련 파일 제거
sudo rm -rf /usr/local/mysql*
sudo rm -rf /Library/StartupItems/MySQLCOM
sudo rm -rf /Library/PreferencePanes/MySQL*
rm -rf ~/Library/PreferencePanes/MySQL*
sudo rm -rf /Library/Receipts/mysql*
sudo rm -rf /Library/Receipts/MySQL*
sudo rm -rf /var/db/receipts/com.mysql.*
```

**2단계: Homebrew로 MySQL 재설치**

```bash
# Homebrew로 MySQL 설치
brew install mysql

# MySQL 서비스 시작
brew services start mysql

# MySQL 보안 설정 (root 비밀번호 설정)
mysql_secure_installation
```

**3단계: 데이터베이스 및 사용자 생성**

```bash
# MySQL 접속
mysql -u root -p

# SQL 실행
```

MySQL 프롬프트에서:
```sql
-- 데이터베이스 생성
CREATE DATABASE springdb;

-- 사용자 생성
CREATE USER 'springdbuser'@'localhost' IDENTIFIED BY '1234';
CREATE USER 'springdbuser'@'%' IDENTIFIED BY '1234';

-- 권한 부여
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'localhost';
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'%';
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

-- 확인
SHOW DATABASES;
EXIT;
```

### 방법 2: MySQL 공식 사이트에서 재설치

1. https://dev.mysql.com/downloads/mysql/ 접속
2. macOS용 MySQL Community Server 다운로드 (DMG 파일)
3. 설치 파일 실행하여 설치
4. 시스템 환경 설정에서 MySQL 시작
5. 초기 root 비밀번호 확인 (설치 시 제공됨)
6. 위의 SQL 실행

### 방법 3: Docker로 MySQL 실행 (대안)

MySQL 설치가 계속 문제가 있다면 Docker 사용:

```bash
# Docker로 MySQL 실행
docker run --name mysql-springdb \
  -e MYSQL_ROOT_PASSWORD=1234 \
  -e MYSQL_DATABASE=springdb \
  -e MYSQL_USER=springdbuser \
  -e MYSQL_PASSWORD=1234 \
  -p 3306:3306 \
  -d mysql:8.0

# 테이블 생성
docker exec -i mysql-springdb mysql -uroot -p1234 springdb < reset_mysql.sql
```

이 경우 root-context.xml의 연결 정보는 그대로 사용 가능합니다.

## 확인 방법

설치/설정 완료 후:

```bash
# MySQL 프로세스 확인
ps aux | grep mysql

# MySQL 접속 테스트
mysql -u springdbuser -p1234 springdb

# MySQL 프롬프트에서
SHOW TABLES;
EXIT;
```

## MySQLWorkbench 연결 설정

새 연결 생성:
- Connection Name: springdbuser
- Hostname: localhost
- Port: 3306
- Username: springdbuser
- Password: 1234
- Default Schema: springdb
