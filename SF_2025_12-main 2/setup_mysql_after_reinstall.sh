#!/bin/bash

# MySQL 재설치 후 초기 설정 스크립트

echo "=========================================="
echo "MySQL 초기 설정 스크립트"
echo "=========================================="
echo ""

# MySQL 경로 찾기
if command -v mysql &> /dev/null; then
    MYSQL_CMD="mysql"
elif [ -f /usr/local/mysql/bin/mysql ]; then
    MYSQL_CMD="/usr/local/mysql/bin/mysql"
elif [ -f /opt/homebrew/bin/mysql ]; then
    MYSQL_CMD="/opt/homebrew/bin/mysql"
else
    echo "✗ MySQL 명령어를 찾을 수 없습니다."
    echo "  MySQL이 설치되어 있는지 확인하세요."
    exit 1
fi

echo "MySQL 경로: $MYSQL_CMD"
echo ""

# root 비밀번호 설정 확인
echo "root 비밀번호를 입력하세요 (비밀번호가 없다면 Enter를 누르세요):"
read -s ROOT_PASSWORD

if [ -z "$ROOT_PASSWORD" ]; then
    MYSQL_ROOT_CMD="$MYSQL_CMD -u root"
else
    MYSQL_ROOT_CMD="$MYSQL_CMD -u root -p$ROOT_PASSWORD"
fi

echo ""
echo "MySQL에 연결 시도 중..."

# 연결 테스트
$MYSQL_ROOT_CMD -e "SELECT VERSION();" 2>&1
if [ $? -ne 0 ]; then
    echo "✗ MySQL 연결 실패"
    echo "  비밀번호를 확인하거나 MySQL 서버가 실행 중인지 확인하세요."
    exit 1
fi

echo "✓ MySQL 연결 성공"
echo ""

# SQL 실행
echo "데이터베이스 및 사용자 생성 중..."
$MYSQL_ROOT_CMD << EOF

-- root 비밀번호 설정 (아직 설정되지 않았다면)
-- ALTER USER 'root'@'localhost' IDENTIFIED BY '1234';
-- FLUSH PRIVILEGES;

-- 기존 데이터베이스/사용자 삭제 (있다면)
DROP DATABASE IF EXISTS springdb;
DROP USER IF EXISTS 'springdbuser'@'localhost';
DROP USER IF EXISTS 'springdbuser'@'%';

-- 데이터베이스 생성
CREATE DATABASE springdb;

-- 사용자 생성
CREATE USER 'springdbuser'@'localhost' IDENTIFIED BY '1234';
CREATE USER 'springdbuser'@'%' IDENTIFIED BY '1234';

-- 권한 부여
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'localhost';
GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'%';
FLUSH PRIVILEGES;

-- springdb 사용
USE springdb;

-- 테이블 생성
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
SELECT '✓ Database and user created successfully!' AS Status;
SHOW DATABASES;
SELECT User, Host FROM mysql.user WHERE User = 'springdbuser';
SHOW TABLES;

EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✓ 설정 완료!"
    echo "=========================================="
    echo ""
    echo "연결 정보:"
    echo "  데이터베이스: springdb"
    echo "  사용자: springdbuser"
    echo "  비밀번호: 1234"
    echo "  호스트: localhost"
    echo "  포트: 3306"
    echo ""
else
    echo ""
    echo "✗ 설정 중 오류 발생"
    exit 1
fi
