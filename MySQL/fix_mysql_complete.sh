#!/bin/bash
set -e

echo "🔧 MySQL 완전 복구 시작..."

# 1. 모든 프로세스 종료
echo "1️⃣ 모든 MySQL 프로세스 종료 중..."
pkill -9 mysqld mysqld_safe 2>/dev/null || true
sleep 3

# 2. brew services 정지
echo "2️⃣ brew services 정지 중..."
brew services stop mysql 2>/dev/null || true
sleep 2

# 3. 소켓 파일 정리
echo "3️⃣ 소켓 파일 정리 중..."
rm -f /tmp/mysql.sock 2>/dev/null || true

# 4. MySQL 정상 시작 (포트 3306 활성화)
echo "4️⃣ MySQL 정상 모드로 시작 중..."
/opt/homebrew/bin/mysqld_safe --datadir=/opt/homebrew/var/mysql --port=3306 --bind-address=127.0.0.1 > /tmp/mysql_fix.log 2>&1 &
MYSQL_PID=$!
echo "MySQL PID: $MYSQL_PID"

# 5. MySQL 시작 대기 (최대 20초)
echo "5️⃣ MySQL 시작 대기 중..."
for i in {1..20}; do
    if mysql --socket=/tmp/mysql.sock -u root -ppassword1234 -e "SELECT 1;" > /dev/null 2>&1; then
        echo "✅ MySQL 시작 완료!"
        break
    fi
    if [ $i -eq 20 ]; then
        echo "❌ MySQL 시작 실패"
        tail -20 /tmp/mysql_fix.log
        exit 1
    fi
    sleep 1
done

# 6. 포트 3306 확인
echo "6️⃣ 포트 3306 확인 중..."
sleep 2
if netstat -an | grep -q ":3306"; then
    echo "✅ 포트 3306 활성화됨"
else
    echo "⚠️ 포트 3306이 아직 활성화되지 않음"
fi

# 7. 연결 테스트
echo "7️⃣ 연결 테스트 중..."
if mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 -e "SELECT '✅ 포트 3306 연결 성공!' as Status;" 2>&1 | grep -q "연결 성공"; then
    echo "✅ 포트 3306 연결 성공!"
    
    # 8. 데이터베이스 복구
    echo "8️⃣ 데이터베이스 복구 중..."
    mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 < database_exports/springdb_structure.sql 2>&1 | tail -1
    mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 < database_exports/dcproject_structure.sql 2>&1 | tail -1
    
    echo ""
    echo "✅✅✅ 모든 복구 완료!"
    echo "비밀번호: password1234"
    echo "포트: 3306"
    echo "데이터베이스: springdb, dcproject"
else
    echo "⚠️ 포트 연결 실패, 소켓으로 확인 중..."
    mysql --socket=/tmp/mysql.sock -u root -ppassword1234 -e "SHOW DATABASES;" 2>&1 | grep -E "springdb|dcproject" || echo "데이터베이스 확인 필요"
fi

echo ""
echo "완료!"

