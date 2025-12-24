#!/bin/bash

# MySQL 연결 테스트 스크립트

echo "=========================================="
echo "MySQL 연결 진단 스크립트"
echo "=========================================="
echo ""

# 1. MySQL 프로세스 확인
echo "1. MySQL 프로세스 확인:"
ps aux | grep -i mysql | grep -v grep
if [ $? -eq 0 ]; then
    echo "✓ MySQL 프로세스 실행 중"
else
    echo "✗ MySQL 프로세스가 실행되지 않음"
fi
echo ""

# 2. 포트 3306 확인
echo "2. 포트 3306 연결 확인:"
lsof -i :3306
if [ $? -eq 0 ]; then
    echo "✓ 포트 3306 사용 중 (MySQL 실행 중일 가능성)"
else
    echo "✗ 포트 3306이 사용되지 않음 (MySQL이 실행되지 않음)"
fi
echo ""

# 3. MySQL 명령어 경로 확인
echo "3. MySQL 명령어 경로 확인:"
which mysql
if [ $? -eq 0 ]; then
    MYSQL_PATH=$(which mysql)
    echo "✓ MySQL 발견: $MYSQL_PATH"
else
    echo "✗ MySQL 명령어를 찾을 수 없음"
    echo "  가능한 경로 확인:"
    ls -la /usr/local/mysql/bin/mysql 2>/dev/null || echo "  /usr/local/mysql/bin/mysql 없음"
    ls -la /opt/homebrew/bin/mysql 2>/dev/null || echo "  /opt/homebrew/bin/mysql 없음"
fi
echo ""

# 4. root 사용자로 연결 테스트 (비밀번호 없이)
echo "4. MySQL root 연결 테스트 (비밀번호 없이):"
if command -v mysql &> /dev/null; then
    mysql -u root -e "SELECT VERSION();" 2>&1
    if [ $? -eq 0 ]; then
        echo "✓ root로 연결 성공"
    else
        echo "✗ root로 연결 실패"
    fi
elif [ -f /usr/local/mysql/bin/mysql ]; then
    /usr/local/mysql/bin/mysql -u root -e "SELECT VERSION();" 2>&1
    if [ $? -eq 0 ]; then
        echo "✓ root로 연결 성공"
    else
        echo "✗ root로 연결 실패"
    fi
elif [ -f /opt/homebrew/bin/mysql ]; then
    /opt/homebrew/bin/mysql -u root -e "SELECT VERSION();" 2>&1
    if [ $? -eq 0 ]; then
        echo "✓ root로 연결 성공"
    else
        echo "✗ root로 연결 실패"
    fi
fi
echo ""

# 5. springdbuser 연결 테스트
echo "5. springdbuser 연결 테스트:"
if command -v mysql &> /dev/null; then
    mysql -u springdbuser -p1234 -e "SELECT DATABASE();" springdb 2>&1
    if [ $? -eq 0 ]; then
        echo "✓ springdbuser로 연결 성공"
    else
        echo "✗ springdbuser로 연결 실패 (사용자 또는 데이터베이스가 없을 수 있음)"
    fi
fi
echo ""

# 6. 데이터베이스 목록 확인
echo "6. 데이터베이스 목록 확인:"
if command -v mysql &> /dev/null; then
    mysql -u root -e "SHOW DATABASES;" 2>&1 | grep -i springdb
    if [ $? -eq 0 ]; then
        echo "✓ springdb 데이터베이스 존재"
    else
        echo "✗ springdb 데이터베이스가 없음"
    fi
fi
echo ""

echo "=========================================="
echo "진단 완료"
echo "=========================================="
