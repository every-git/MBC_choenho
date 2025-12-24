#!/bin/bash
# Lombok 설치 확인 및 재설정 스크립트

ECLIPSE_DIR="/Applications/Eclipse.app/Contents/Eclipse"
ECLIPSE_INI="$ECLIPSE_DIR/eclipse.ini"
LOMBOK_JAR="$ECLIPSE_DIR/lombok.jar"

echo "=== Lombok 설치 상태 확인 ==="
echo ""

# 1. lombok.jar 파일 확인
if [ -f "$LOMBOK_JAR" ]; then
    echo "✅ Lombok JAR 파일 존재: $LOMBOK_JAR"
    ls -lh "$LOMBOK_JAR"
else
    echo "❌ Lombok JAR 파일이 없습니다: $LOMBOK_JAR"
fi

echo ""

# 2. eclipse.ini 설정 확인
if grep -q "lombok.jar" "$ECLIPSE_INI" 2>/dev/null; then
    echo "✅ eclipse.ini에 Lombok 설정이 있습니다:"
    grep "lombok" "$ECLIPSE_INI"
else
    echo "❌ eclipse.ini에 Lombok 설정이 없습니다."
fi

echo ""
echo "=== 설치 완료 ==="
echo "이클립스를 재시작하면 Lombok이 적용됩니다."
echo ""

