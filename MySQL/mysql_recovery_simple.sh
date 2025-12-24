#!/bin/bash
# 간단한 MySQL 복구 방법

echo "🔧 MySQL 복구 (간단 버전)"
echo ""

# MySQL 데이터 디렉토리 확인
MYSQL_DATA_DIR="/opt/homebrew/var/mysql"

echo "1️⃣ MySQL 데이터 디렉토리 확인:"
ls -la "$MYSQL_DATA_DIR" | head -5
echo ""

echo "2️⃣ MySQL 초기화가 필요한지 확인..."
if [ ! -f "$MYSQL_DATA_DIR/mysql/user.MYD" ] && [ ! -f "$MYSQL_DATA_DIR/mysql/user.ibd" ]; then
    echo "   ⚠️ MySQL이 초기화되지 않았습니다."
    echo ""
    echo "   MySQL 초기화를 진행하시겠습니까? (y/n)"
    read -p "   > " init_mysql
    
    if [ "$init_mysql" = "y" ] || [ "$init_mysql" = "Y" ]; then
        echo ""
        echo "   MySQL 초기화 중..."
        mysqld --initialize-insecure --datadir="$MYSQL_DATA_DIR"
        if [ $? -eq 0 ]; then
            echo "   ✅ 초기화 완료"
        else
            echo "   ❌ 초기화 실패"
            exit 1
        fi
    fi
fi

echo ""
echo "3️⃣ MySQL 서비스 시작..."
brew services start mysql

echo ""
echo "   MySQL 시작 대기 중... (10초)"
sleep 10

echo ""
echo "4️⃣ 비밀번호 없이 접속 시도..."
mysql -u root -e "SELECT '접속 성공!' as Status;" 2>&1

if [ $? -eq 0 ]; then
    echo "   ✅ 비밀번호 없이 접속 가능!"
    echo ""
    echo "   비밀번호를 설정하시겠습니까? (y/n)"
    read -p "   > " set_password
    
    if [ "$set_password" = "y" ] || [ "$set_password" = "Y" ]; then
        echo ""
        echo "   새 비밀번호를 입력하세요:"
        read -s NEW_PASSWORD
        
        mysql -u root <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED BY '$NEW_PASSWORD';
FLUSH PRIVILEGES;
EOF
        
        echo ""
        echo "   ✅ 비밀번호 설정 완료!"
        
        # 데이터베이스 복구
        echo ""
        echo "5️⃣ 데이터베이스 복구 중..."
        SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
        
        if [ -f "$SCRIPT_DIR/database_exports/springdb_structure.sql" ]; then
            mysql -u root -p"$NEW_PASSWORD" < "$SCRIPT_DIR/database_exports/springdb_structure.sql" 2>&1 | grep -v "Warning" || true
            echo "   ✅ springdb 복구"
        fi
        
        if [ -f "$SCRIPT_DIR/database_exports/dcproject_structure.sql" ]; then
            mysql -u root -p"$NEW_PASSWORD" < "$SCRIPT_DIR/database_exports/dcproject_structure.sql" 2>&1 | grep -v "Warning" || true
            echo "   ✅ dcproject 복구"
        fi
        
        # 연결 설정 업데이트
        CONNECTION_FILE="$HOME/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json"
        if [ -f "$CONNECTION_FILE" ]; then
            sed -i '' "s/\"password\": \"[^\"]*\"/\"password\": \"$NEW_PASSWORD\"/g" "$CONNECTION_FILE"
            echo "   ✅ 연결 설정 업데이트"
        fi
        
    fi
else
    echo "   ⚠️ 비밀번호 필요 또는 접속 불가"
    echo ""
    echo "   비밀번호를 입력해서 접속해보세요:"
    echo "   mysql -u root -p"
fi

echo ""
echo "✅ 완료!"
