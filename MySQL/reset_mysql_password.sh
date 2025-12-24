#!/bin/bash
# MySQL root 비밀번호 재설정 스크립트

echo "⚠️  MySQL root 비밀번호 재설정을 시작합니다."
echo "이 스크립트는 MySQL 서비스를 일시적으로 중단할 수 있습니다."
echo ""

read -p "계속하시겠습니까? (y/n): " confirm
if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
    echo "취소되었습니다."
    exit 1
fi

# MySQL 설치 경로 확인
MYSQL_PATH=""
if [ -f "/usr/local/mysql/support-files/mysql.server" ]; then
    MYSQL_PATH="/usr/local/mysql"
elif [ -f "/opt/homebrew/bin/mysql" ]; then
    MYSQL_PATH="homebrew"
else
    echo "❌ MySQL 설치 경로를 찾을 수 없습니다."
    echo "수동으로 MySQL 비밀번호를 재설정해주세요."
    exit 1
fi

echo ""
echo "새 비밀번호를 입력하세요:"
read -s NEW_PASSWORD

if [ -z "$NEW_PASSWORD" ]; then
    echo "❌ 비밀번호가 비어있습니다."
    exit 1
fi

echo ""
echo "비밀번호 확인을 위해 다시 입력하세요:"
read -s CONFIRM_PASSWORD

if [ "$NEW_PASSWORD" != "$CONFIRM_PASSWORD" ]; then
    echo "❌ 비밀번호가 일치하지 않습니다."
    exit 1
fi

echo ""
echo "📋 다음 단계를 수동으로 수행해야 합니다:"
echo ""
echo "1. 새 터미널 창을 열어주세요"
echo "2. 다음 명령어를 실행하세요:"
echo ""
echo "   sudo mysqld_safe --skip-grant-tables &"
echo ""
echo "3. 그 다음 이 터미널로 돌아와서 Enter를 눌러주세요"
echo ""

read -p "Enter를 눌러 계속하세요..."

echo ""
echo "4. 다음 명령어를 실행하세요:"
echo ""
echo "   mysql -u root"
echo ""
echo "5. MySQL 프롬프트에서 다음 명령어들을 실행하세요:"
echo ""
echo "   USE mysql;"
echo "   ALTER USER 'root'@'localhost' IDENTIFIED BY '${NEW_PASSWORD}';"
echo "   FLUSH PRIVILEGES;"
echo "   EXIT;"
echo ""
echo "6. MySQL 안전 모드를 종료하고 정상 모드로 재시작하세요:"
echo ""
if [ "$MYSQL_PATH" = "homebrew" ]; then
    echo "   brew services restart mysql"
else
    echo "   sudo /usr/local/mysql/support-files/mysql.server restart"
fi
echo ""

echo "✅ 완료되면 Database Client에서 새 비밀번호로 연결을 시도하세요."




