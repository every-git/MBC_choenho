#!/bin/bash
# Workbench의 모든 설정과 데이터베이스 구조를 export하는 스크립트

WB_DIR="$HOME/Library/Application Support/MySQL/Workbench"
OUTPUT_DIR="$(pwd)/workbench_export"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

echo "🔧 MySQL Workbench 설정 및 데이터베이스 Export 시작..."
echo ""

# 출력 디렉토리 생성
mkdir -p "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR/sql_history"
mkdir -p "$OUTPUT_DIR/database_dumps"

echo "📋 1. Workbench 설정 파일 복사 중..."

# 설정 파일 복사
if [ -f "$WB_DIR/connections.xml" ]; then
    cp "$WB_DIR/connections.xml" "$OUTPUT_DIR/connections.xml"
    echo "  ✅ connections.xml 복사 완료"
fi

if [ -f "$WB_DIR/server_instances.xml" ]; then
    cp "$WB_DIR/server_instances.xml" "$OUTPUT_DIR/server_instances.xml"
    echo "  ✅ server_instances.xml 복사 완료"
fi

if [ -f "$WB_DIR/wb_options.xml" ]; then
    cp "$WB_DIR/wb_options.xml" "$OUTPUT_DIR/wb_options.xml"
    echo "  ✅ wb_options.xml 복사 완료"
fi

if [ -f "$WB_DIR/wb_state.xml" ]; then
    cp "$WB_DIR/wb_state.xml" "$OUTPUT_DIR/wb_state.xml"
    echo "  ✅ wb_state.xml 복사 완료"
fi

echo ""
echo "📝 2. SQL 히스토리 복사 중..."

# SQL 히스토리 복사
if [ -d "$WB_DIR/sql_history" ]; then
    cp -r "$WB_DIR/sql_history"/* "$OUTPUT_DIR/sql_history/" 2>/dev/null
    echo "  ✅ SQL 히스토리 복사 완료"
fi

echo ""
echo "🗄️  3. 데이터베이스 구조 Export 준비..."

# connections.xml에서 데이터베이스 정보 추출
echo "다음 데이터베이스들을 export할 수 있습니다:"
echo ""
echo "  1. dcproject (jdbctest 연결)"
echo "  2. springdb (springdbuser 연결)"
echo ""

read -p "데이터베이스 구조를 export하시겠습니까? (y/n): " export_db

if [ "$export_db" = "y" ] || [ "$export_db" = "Y" ]; then
    echo ""
    echo "📤 데이터베이스 구조 export를 위해 다음 명령어를 실행하세요:"
    echo ""
    echo "# dcproject 데이터베이스 구조 export:"
    echo "mysqldump -h 127.0.0.1 -P 3306 -u jdbctest -p --no-data --routines --triggers dcproject > $OUTPUT_DIR/database_dumps/dcproject_structure.sql"
    echo ""
    echo "# springdb 데이터베이스 구조 export:"
    echo "mysqldump -h 127.0.0.1 -P 3306 -u root -p --no-data --routines --triggers springdb > $OUTPUT_DIR/database_dumps/springdb_structure.sql"
    echo ""
    echo "# 데이터 포함 전체 export (선택사항):"
    echo "mysqldump -h 127.0.0.1 -P 3306 -u jdbctest -p dcproject > $OUTPUT_DIR/database_dumps/dcproject_full.sql"
    echo "mysqldump -h 127.0.0.1 -P 3306 -u root -p springdb > $OUTPUT_DIR/database_dumps/springdb_full.sql"
fi

echo ""
echo "✅ Export 완료!"
echo "📂 결과 위치: $OUTPUT_DIR"
echo ""
ls -la "$OUTPUT_DIR"



