#!/bin/bash

echo "🔧 MySQL 완전 재설정 시작..."

# 모든 프로세스 강제 종료
killall -9 mysqld mysqld_safe 2>/dev/null || true
pkill -9 mysqld mysqld_safe 2>/dev/null || true
sleep 2

# brew services 정지
brew services stop mysql 2>/dev/null || true
sleep 1

# 소켓 파일 정리
rm -f /tmp/mysql.sock

# MySQL 직접 시작 (포트 명시)
echo "MySQL 시작 중..."
nohup /opt/homebrew/bin/mysqld_safe --datadir=/opt/homebrew/var/mysql --port=3306 --bind-address=127.0.0.1 > /tmp/mysql.log 2>&1 &

# 시작 대기
sleep 10

# 연결 테스트
if mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 -e "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ 포트 3306 연결 성공!"
    
    # 데이터베이스 복구
    mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 < database_exports/springdb_structure.sql 2>&1 | tail -1
    mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 < database_exports/dcproject_structure.sql 2>&1 | tail -1
    
    echo "✅✅✅ 복구 완료!"
    echo "비밀번호: password1234"
    echo "포트: 3306"
else
    echo "❌ 포트 연결 실패"
    echo "로그 확인: tail -20 /tmp/mysql.log"
fi

