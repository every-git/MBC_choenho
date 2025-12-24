#!/bin/bash
# MySQL 완전 복구 스크립트

echo "🔧 MySQL 복구 시작..."
echo ""

# MySQL 비밀번호 확인
echo "MySQL root 비밀번호를 입력하세요:"
read -s MYSQL_PASSWORD

# 연결 테스트
echo ""
echo "📡 MySQL 연결 테스트 중..."
mysql -u root -p"$MYSQL_PASSWORD" -e "SELECT 'Connection OK' as Status;" 2>&1

if [ $? -ne 0 ]; then
    echo "❌ MySQL 연결 실패. 비밀번호를 확인하세요."
    exit 1
fi

echo "✅ MySQL 연결 성공!"
echo ""

# 현재 작업 디렉토리
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
EXPORTS_DIR="$SCRIPT_DIR/database_exports"

echo "🗄️  데이터베이스 복구 중..."
echo ""

# 1. springdb 복구
echo "1️⃣ springdb 데이터베이스 복구 중..."
if [ -f "$EXPORTS_DIR/springdb_structure.sql" ]; then
    mysql -u root -p"$MYSQL_PASSWORD" < "$EXPORTS_DIR/springdb_structure.sql" 2>&1
    if [ $? -eq 0 ]; then
        echo "  ✅ springdb 복구 완료"
    else
        echo "  ⚠️ springdb 복구 중 오류 (무시 가능)"
    fi
else
    echo "  ⚠️ springdb_structure.sql 파일이 없습니다"
fi

echo ""

# 2. dcproject 복구
echo "2️⃣ dcproject 데이터베이스 복구 중..."
if [ -f "$EXPORTS_DIR/dcproject_structure.sql" ]; then
    mysql -u root -p"$MYSQL_PASSWORD" < "$EXPORTS_DIR/dcproject_structure.sql" 2>&1
    if [ $? -eq 0 ]; then
        echo "  ✅ dcproject 복구 완료"
    else
        echo "  ⚠️ dcproject 복구 중 오류 (무시 가능)"
    fi
else
    echo "  ⚠️ dcproject_structure.sql 파일이 없습니다"
fi

echo ""

# 3. Database Client 연결 설정 복구
echo "3️⃣ Database Client 연결 설정 복구 중..."
CONNECTION_FILE="$HOME/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json"
mkdir -p "$(dirname "$CONNECTION_FILE")"

if [ -f "$SCRIPT_DIR/database-client-connections-v2.json" ]; then
    cp "$SCRIPT_DIR/database-client-connections-v2.json" "$CONNECTION_FILE"
    echo "  ✅ 연결 설정 파일 복구 완료: $CONNECTION_FILE"
    echo ""
    echo "⚠️  연결 설정 파일의 비밀번호를 실제 비밀번호로 수정하세요:"
    echo "   파일 열기: open '$CONNECTION_FILE'"
else
    echo "  ⚠️ database-client-connections-v2.json 파일이 없습니다"
fi

echo ""
echo "✅ 복구 완료!"
echo ""
echo "📋 다음 단계:"
echo "1. Database Client 설정 파일에서 비밀번호 확인"
echo "2. Cursor 재시작"
echo "3. Database Client에서 연결 테스트"
