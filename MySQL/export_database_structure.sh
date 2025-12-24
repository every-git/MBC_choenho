#!/bin/bash
# 데이터베이스 구조만 export하는 스크립트

OUTPUT_DIR="$(pwd)/database_exports"
mkdir -p "$OUTPUT_DIR"

echo "🗄️  데이터베이스 구조 Export"
echo ""

# dcproject 데이터베이스 구조 export
echo "1️⃣ dcproject 데이터베이스 구조 export 중..."
read -p "jdbctest 비밀번호를 입력하세요: " -s JDBCTEST_PASS
echo ""

mysqldump -h 127.0.0.1 -P 3306 -u jdbctest -p"$JDBCTEST_PASS" \
  --no-data \
  --routines \
  --triggers \
  --single-transaction \
  dcproject > "$OUTPUT_DIR/dcproject_structure.sql" 2>&1

if [ $? -eq 0 ]; then
    echo "  ✅ dcproject 구조 export 완료: $OUTPUT_DIR/dcproject_structure.sql"
else
    echo "  ❌ dcproject export 실패 (비밀번호 확인 필요)"
fi

echo ""

# springdb 데이터베이스 구조 export
echo "2️⃣ springdb 데이터베이스 구조 export 중..."
read -p "root 비밀번호를 입력하세요: " -s ROOT_PASS
echo ""

mysqldump -h 127.0.0.1 -P 3306 -u root -p"$ROOT_PASS" \
  --no-data \
  --routines \
  --triggers \
  --single-transaction \
  springdb > "$OUTPUT_DIR/springdb_structure.sql" 2>&1

if [ $? -eq 0 ]; then
    echo "  ✅ springdb 구조 export 완료: $OUTPUT_DIR/springdb_structure.sql"
else
    echo "  ❌ springdb export 실패 (비밀번호 확인 필요)"
fi

echo ""
echo "✅ Export 완료!"
echo "📂 결과 위치: $OUTPUT_DIR"
ls -lh "$OUTPUT_DIR"




