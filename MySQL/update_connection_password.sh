#!/bin/bash
# 연결 설정 파일의 비밀번호 업데이트 스크립트

echo "🔧 MySQL 연결 설정 파일 비밀번호 업데이트"
echo ""

if [ -z "$1" ]; then
    echo "사용법: $0 [비밀번호]"
    echo "예: $0 password1234"
    echo ""
    echo "비밀번호를 입력하세요:"
    read -s PASSWORD
else
    PASSWORD="$1"
fi

if [ -z "$PASSWORD" ]; then
    echo "❌ 비밀번호가 입력되지 않았습니다"
    exit 1
fi

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
UPDATED=0

# 1. config.json 업데이트
if [ -f "$SCRIPT_DIR/config.json" ]; then
    echo "1️⃣ config.json 업데이트 중..."
    # macOS용 sed 명령어
    sed -i '' "s/\"password\": \"[^\"]*\"/\"password\": \"$PASSWORD\"/g" "$SCRIPT_DIR/config.json"
    echo "   ✅ config.json 업데이트 완료"
    UPDATED=1
fi

# 2. .vscode/database-connections.json 업데이트
if [ -f "$SCRIPT_DIR/.vscode/database-connections.json" ]; then
    echo "2️⃣ .vscode/database-connections.json 업데이트 중..."
    sed -i '' "s/\"password\": \"[^\"]*\"/\"password\": \"$PASSWORD\"/g" "$SCRIPT_DIR/.vscode/database-connections.json"
    echo "   ✅ database-connections.json 업데이트 완료"
    UPDATED=1
fi

# 3. Database Client 연결 설정 파일 업데이트
CONNECTION_FILE="$HOME/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json"
if [ -f "$CONNECTION_FILE" ]; then
    echo "3️⃣ Database Client 연결 설정 업데이트 중..."
    sed -i '' "s/\"password\": \"[^\"]*\"/\"password\": \"$PASSWORD\"/g" "$CONNECTION_FILE"
    echo "   ✅ Database Client 설정 업데이트 완료"
    UPDATED=1
fi

# 4. .sqltoolsrc.json 업데이트 (있는 경우)
if [ -f "$SCRIPT_DIR/.sqltoolsrc.json" ]; then
    echo "4️⃣ .sqltoolsrc.json 업데이트 중..."
    sed -i '' "s/\"password\": \"[^\"]*\"/\"password\": \"$PASSWORD\"/g" "$SCRIPT_DIR/.sqltoolsrc.json"
    echo "   ✅ .sqltoolsrc.json 업데이트 완료"
    UPDATED=1
fi

if [ $UPDATED -eq 1 ]; then
    echo ""
    echo "✅ 모든 연결 설정 파일 업데이트 완료!"
    echo ""
    echo "📋 다음 단계:"
    echo "1. Cursor 재시작 (중요!)"
    echo "2. Database Client에서 연결 테스트"
    echo "3. 연결이 안 되면: ./test_mysql_connection.sh"
else
    echo ""
    echo "⚠️ 업데이트할 파일을 찾을 수 없습니다"
fi
