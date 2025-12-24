#!/bin/bash
# MySQL 비밀번호 재설정 및 복구 스크립트

echo "🔧 MySQL 비밀번호 재설정 시작..."
echo ""

# MySQL 서비스 중지
echo "1️⃣ MySQL 서비스 중지 중..."
brew services stop mysql

# 잠시 대기
sleep 2

echo ""
echo "2️⃣ MySQL 안전 모드로 시작 중..."
echo "   (백그라운드에서 실행됩니다)"
mysqld_safe --skip-grant-tables > /dev/null 2>&1 &

# MySQL이 시작될 때까지 대기 (최대 30초)
echo "   MySQL 시작 대기 중..."
for i in {1..30}; do
    mysql -u root -e "SELECT 1;" > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "   ✅ MySQL 시작 완료"
        break
    fi
    if [ $i -eq 30 ]; then
        echo "   ⚠️ MySQL 시작 시간 초과, 계속 진행..."
    fi
    sleep 1
done

echo ""
echo "3️⃣ 비밀번호 재설정 중..."
echo ""
echo "새 비밀번호를 입력하세요 (예: password1234):"
read -s NEW_PASSWORD

if [ -z "$NEW_PASSWORD" ]; then
    NEW_PASSWORD="password1234"
    echo "기본 비밀번호 사용: password1234"
fi

mysql -u root <<EOF
USE mysql;
ALTER USER 'root'@'localhost' IDENTIFIED BY '$NEW_PASSWORD';
FLUSH PRIVILEGES;
EXIT;
EOF

if [ $? -eq 0 ]; then
    echo "✅ 비밀번호 재설정 완료!"
else
    echo "❌ 비밀번호 재설정 실패"
    exit 1
fi

echo ""
echo "4️⃣ MySQL 프로세스 종료 중..."
pkill mysqld
pkill mysqld_safe
sleep 2

echo ""
echo "5️⃣ MySQL 정상 모드로 재시작 중..."
brew services start mysql

sleep 3

echo ""
echo "6️⃣ 연결 테스트 중..."
mysql -u root -p"$NEW_PASSWORD" -e "SELECT 'MySQL 연결 성공!' as Status;" 2>&1

if [ $? -eq 0 ]; then
    echo "✅ MySQL 정상 작동 확인!"
    
    # 데이터베이스 복구
    echo ""
    echo "7️⃣ 데이터베이스 복구 중..."
    
    SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
    EXPORTS_DIR="$SCRIPT_DIR/database_exports"
    
    if [ -f "$EXPORTS_DIR/springdb_structure.sql" ]; then
        echo "   springdb 복구 중..."
        mysql -u root -p"$NEW_PASSWORD" < "$EXPORTS_DIR/springdb_structure.sql" 2>&1 | grep -v "Warning\|mysqldump" || true
        echo "   ✅ springdb 복구 완료"
    fi
    
    if [ -f "$EXPORTS_DIR/dcproject_structure.sql" ]; then
        echo "   dcproject 복구 중..."
        mysql -u root -p"$NEW_PASSWORD" < "$EXPORTS_DIR/dcproject_structure.sql" 2>&1 | grep -v "Warning\|mysqldump" || true
        echo "   ✅ dcproject 복구 완료"
    fi
    
    # 연결 설정 파일 비밀번호 업데이트
    echo ""
    echo "8️⃣ Database Client 연결 설정 업데이트 중..."
    CONNECTION_FILE="$HOME/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json"
    
    if [ -f "$CONNECTION_FILE" ]; then
        # macOS용 sed 명령어
        sed -i '' "s/\"password\": \"[^\"]*\"/\"password\": \"$NEW_PASSWORD\"/g" "$CONNECTION_FILE"
        echo "   ✅ 연결 설정 파일 비밀번호 업데이트 완료"
    fi
    
    echo ""
    echo "✅ 모든 복구 완료!"
    echo ""
    echo "📋 설정된 비밀번호: $NEW_PASSWORD"
    echo "📍 연결 설정 파일: $CONNECTION_FILE"
    echo ""
    echo "다음 단계:"
    echo "1. Cursor 재시작"
    echo "2. Database Client에서 연결 테스트"
    
else
    echo "❌ 연결 테스트 실패"
    echo "수동으로 확인하세요: mysql -u root -p"
fi
