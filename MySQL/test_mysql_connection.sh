#!/bin/bash
# MySQL 연결 테스트 스크립트

echo "🔍 MySQL 연결 진단 시작..."
echo ""

# 1. MySQL 서버 상태 확인
echo "1️⃣ MySQL 서버 상태 확인 중..."
if pgrep -x mysqld > /dev/null; then
    echo "   ✅ MySQL 서버 실행 중"
else
    echo "   ❌ MySQL 서버가 실행되지 않음"
    echo "   해결: brew services start mysql"
    exit 1
fi

# 2. 포트 확인
echo ""
echo "2️⃣ 포트 3306 확인 중..."
if lsof -i :3306 > /dev/null 2>&1; then
    echo "   ✅ 포트 3306 리스닝 중"
else
    echo "   ❌ 포트 3306이 사용되지 않음"
    exit 1
fi

# 3. 비밀번호 없이 연결 시도
echo ""
echo "3️⃣ 비밀번호 없이 연결 시도 중..."
mysql -u root -e "SELECT 1;" > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "   ✅ 비밀번호 없이 연결 성공"
    echo ""
    echo "📋 연결 정보:"
    mysql -u root -e "SELECT VERSION() as 'MySQL 버전', DATABASE() as '현재 DB';"
    exit 0
else
    echo "   ⚠️ 비밀번호가 필요합니다"
fi

# 4. 비밀번호 입력받아 연결 시도
echo ""
echo "4️⃣ 비밀번호로 연결 시도 중..."
echo "   root 비밀번호를 입력하세요 (비밀번호가 없으면 Enter):"
read -s PASSWORD

if [ -z "$PASSWORD" ]; then
    mysql -u root -e "SELECT 1;" > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "   ✅ 비밀번호 없이 연결 성공"
        mysql -u root -e "SELECT VERSION() as 'MySQL 버전', DATABASE() as '현재 DB';"
        exit 0
    else
        echo "   ❌ 비밀번호가 필요합니다"
    fi
else
    mysql -u root -p"$PASSWORD" -e "SELECT 1;" > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "   ✅ 비밀번호로 연결 성공!"
        echo ""
        echo "📋 연결 정보:"
        mysql -u root -p"$PASSWORD" -e "SELECT VERSION() as 'MySQL 버전', DATABASE() as '현재 DB';"
        echo ""
        echo "💡 이 비밀번호를 설정 파일에 업데이트하세요:"
        echo "   - config.json"
        echo "   - .vscode/database-connections.json"
        echo "   - Database Client 연결 설정"
        exit 0
    else
        echo "   ❌ 비밀번호가 잘못되었습니다"
    fi
fi

# 5. 해결 방법 제시
echo ""
echo "❌ 연결 실패"
echo ""
echo "🔧 해결 방법:"
echo "1. 비밀번호 재설정: ./reset_mysql_password_final.sh"
echo "2. Workbench에서 사용하는 비밀번호 확인"
echo "3. 설정 파일의 비밀번호 업데이트"
echo ""
echo "📝 설정 파일 위치:"
echo "   - config.json"
echo "   - .vscode/database-connections.json"
echo "   - ~/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json"
