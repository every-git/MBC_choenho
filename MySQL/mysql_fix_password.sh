#!/bin/bash
# MySQL 비밀번호 재설정 (소켓 방식)

echo "🔧 MySQL 비밀번호 재설정..."

# MySQL 서비스 시작
brew services start mysql > /dev/null 2>&1
sleep 5

# 소켓 파일 확인
if [ -S /tmp/mysql.sock ]; then
    echo "✅ MySQL 소켓 확인: /tmp/mysql.sock"
    
    # 비밀번호 없이 접속 시도
    mysql -u root --socket=/tmp/mysql.sock -e "SELECT 1;" > /dev/null 2>&1
    
    if [ $? -eq 0 ]; then
        echo "✅ 비밀번호 없이 접속 가능"
        echo ""
        NEW_PASSWORD="password1234"
        echo "📝 비밀번호 자동 설정: $NEW_PASSWORD"
        
        mysql -u root --socket=/tmp/mysql.sock <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY '$NEW_PASSWORD';
FLUSH PRIVILEGES;
EOF
        
        echo ""
        echo "✅ 비밀번호 설정 완료: $NEW_PASSWORD"
        
    # 데이터베이스 복구 (공통)
    echo ""
    echo "🗄️  데이터베이스 복구 중..."
    SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
    
    if [ -f "$SCRIPT_DIR/database_exports/springdb_structure.sql" ]; then
        mysql -u root -p"$NEW_PASSWORD" --socket=/tmp/mysql.sock < "$SCRIPT_DIR/database_exports/springdb_structure.sql" 2>&1 | grep -v "Warning\|mysqldump\|ERROR" || true
        echo "  ✅ springdb 복구 완료"
    fi
    
    if [ -f "$SCRIPT_DIR/database_exports/dcproject_structure.sql" ]; then
        mysql -u root -p"$NEW_PASSWORD" --socket=/tmp/mysql.sock < "$SCRIPT_DIR/database_exports/dcproject_structure.sql" 2>&1 | grep -v "Warning\|mysqldump\|ERROR" || true
        echo "  ✅ dcproject 복구 완료"
    fi
        
    # 연결 설정 업데이트
    CONNECTION_FILE="$HOME/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json"
    if [ -f "$CONNECTION_FILE" ]; then
        sed -i '' "s/\"password\": \"[^\"]*\"/\"password\": \"$NEW_PASSWORD\"/g" "$CONNECTION_FILE"
        echo "  ✅ 연결 설정 업데이트 완료"
    fi
    
    echo ""
    echo "✅ 모든 복구 완료!"
    echo "📋 설정된 비밀번호: $NEW_PASSWORD"
    echo ""
    echo "다음 단계:"
    echo "1. Cursor 재시작"
    echo "2. Database Client에서 연결 테스트"
        
    else
        echo "⚠️ 비밀번호 필요 또는 안전 모드 필요"
        echo ""
        echo "MySQL 안전 모드로 재시작 중..."
        
        # MySQL 종료
        brew services stop mysql > /dev/null 2>&1
        pkill mysqld > /dev/null 2>&1
        pkill mysqld_safe > /dev/null 2>&1
        sleep 3
        
        # 안전 모드로 시작
        mysqld_safe --skip-grant-tables --socket=/tmp/mysql.sock > /dev/null 2>&1 &
        sleep 8
        
        # 비밀번호 설정
        NEW_PASSWORD="password1234"
        echo "📝 비밀번호 자동 설정: $NEW_PASSWORD"
        
        mysql -u root --socket=/tmp/mysql.sock <<EOF 2>&1 | grep -v "Warning" || true
USE mysql;
ALTER USER 'root'@'localhost' IDENTIFIED BY '$NEW_PASSWORD';
FLUSH PRIVILEGES;
EXIT;
EOF
        
        # MySQL 종료 후 정상 모드로 재시작
        pkill mysqld > /dev/null 2>&1
        pkill mysqld_safe > /dev/null 2>&1
        sleep 2
        brew services start mysql > /dev/null 2>&1
        sleep 5
        
        echo "✅ 비밀번호 설정 완료"
    fi
else
    echo "❌ MySQL 소켓 파일을 찾을 수 없습니다"
    echo "MySQL이 제대로 실행되고 있는지 확인하세요: brew services list"
fi
