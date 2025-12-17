#!/bin/bash
# Database Client 확장 프로그램 자동 설정 스크립트

echo "🔧 MySQL 확장 프로그램 설정을 시작합니다..."

# Cursor 경로
CURSOR_PATH="$HOME/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2"

# VS Code 경로
VSCODE_PATH="$HOME/Library/Application Support/Code/User/globalStorage/cweijan.vscode-database-client2"

# 현재 스크립트 위치
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CONFIG_FILE="$SCRIPT_DIR/database-client-connections.json"

# 설정 파일이 존재하는지 확인
if [ ! -f "$CONFIG_FILE" ]; then
    echo "❌ 오류: $CONFIG_FILE 파일을 찾을 수 없습니다."
    exit 1
fi

# Cursor 또는 VS Code 선택
echo ""
echo "어떤 에디터를 사용하시나요?"
echo "1) Cursor"
echo "2) VS Code"
read -p "선택 (1 또는 2): " choice

case $choice in
    1)
        TARGET_PATH="$CURSOR_PATH"
        EDITOR_NAME="Cursor"
        ;;
    2)
        TARGET_PATH="$VSCODE_PATH"
        EDITOR_NAME="VS Code"
        ;;
    *)
        echo "❌ 잘못된 선택입니다."
        exit 1
        ;;
esac

# 디렉토리 생성
echo ""
echo "📁 $EDITOR_NAME 설정 디렉토리 생성 중..."
mkdir -p "$TARGET_PATH"

# 설정 파일 복사
echo "📋 설정 파일 복사 중..."
cp "$CONFIG_FILE" "$TARGET_PATH/connections.json"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 설정 파일이 성공적으로 복사되었습니다!"
    echo "📍 위치: $TARGET_PATH/connections.json"
    echo ""
    echo "⚠️  중요: 다음 단계를 수행하세요:"
    echo "1. 위 경로의 connections.json 파일을 엽니다"
    echo "2. 각 연결의 'password' 필드에 실제 비밀번호를 입력합니다"
    echo "3. $EDITOR_NAME를 재시작하거나 확장 프로그램을 새로고침합니다"
    echo ""
    echo "💡 파일 열기: open '$TARGET_PATH/connections.json'"
    read -p "지금 파일을 여시겠습니까? (y/n): " open_file
    if [ "$open_file" = "y" ] || [ "$open_file" = "Y" ]; then
        open "$TARGET_PATH/connections.json"
    fi
else
    echo "❌ 파일 복사 중 오류가 발생했습니다."
    exit 1
fi
