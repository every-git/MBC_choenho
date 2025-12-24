#!/bin/bash

echo "🔧 MySQL 포트 3306 활성화 시도..."

# 1. 모든 프로세스 종료
echo "프로세스 종료 중..."
pkill -9 mysqld mysqld_safe 2>/dev/null
brew services stop mysql 2>/dev/null
sleep 3

# 2. 소켓으로 먼저 연결해서 포트 설정 확인
echo "소켓으로 MySQL 시작 중..."
/opt/homebrew/bin/mysqld_safe --datadir=/opt/homebrew/var/mysql > /tmp/mysql_socket.log 2>&1 &
sleep 10

# 3. 소켓으로 연결 확인
if mysql --socket=/tmp/mysql.sock -u root -ppassword1234 -e "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ 소켓 연결 성공"
    
    # 4. MySQL 설정 확인
    echo "현재 포트 설정 확인:"
    mysql --socket=/tmp/mysql.sock -u root -ppassword1234 -e "SHOW VARIABLES LIKE 'port'; SHOW VARIABLES LIKE 'bind_address';" 2>&1
    
    # 5. 데이터베이스 복구
    echo "데이터베이스 복구 중..."
    mysql --socket=/tmp/mysql.sock -u root -ppassword1234 < database_exports/springdb_structure.sql 2>&1 | tail -1
    mysql --socket=/tmp/mysql.sock -u root -ppassword1234 < database_exports/dcproject_structure.sql 2>&1 | tail -1
    
    echo ""
    echo "⚠️ MySQL이 소켓만 사용 중입니다."
    echo "포트 3306을 활성화하려면 MySQL 재시작이 필요합니다."
    echo ""
    echo "현재 상태:"
    echo "  - 소켓 연결: ✅ 작동"
    echo "  - 포트 3306: ❌ 비활성화"
    echo ""
    echo "Database Client에서는 localhost 연결이 실패할 수 있습니다."
    echo "소켓 경로: /tmp/mysql.sock"
else
    echo "❌ 소켓 연결도 실패"
    tail -20 /tmp/mysql_socket.log
fi

