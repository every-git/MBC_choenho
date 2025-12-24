#!/bin/bash
# SQL 파일에서 mysqldump 경고 메시지 제거 및 USE 문 추가

echo "🔧 SQL 파일 수정 중..."

# springdb_structure.sql 수정
if [ -f "springdb_structure.sql" ]; then
    # 첫 번째 줄의 mysqldump 경고 메시지 제거
    sed -i.bak '1d' springdb_structure.sql
    
    # USE 문 추가 (데이터베이스 생성 부분 다음에)
    sed -i.bak '/^USE/!b; s/^USE.*$/USE `springdb`;/' springdb_structure.sql
    
    # 데이터베이스 생성 부분 추가 (없다면)
    if ! grep -q "CREATE DATABASE" springdb_structure.sql; then
        sed -i.bak '/^-- Server version/a\
\
--\
-- 데이터베이스 생성 (없다면)\
--\
CREATE DATABASE IF NOT EXISTS `springdb`\
  DEFAULT CHARACTER SET utf8mb4\
  DEFAULT COLLATE utf8mb4_unicode_ci;\
\
USE `springdb`;' springdb_structure.sql
    fi
    
    echo "  ✅ springdb_structure.sql 수정 완료"
fi

# dcproject_structure.sql 수정
if [ -f "dcproject_structure.sql" ]; then
    # 첫 번째 줄의 mysqldump 경고 메시지 제거
    sed -i.bak '1d' dcproject_structure.sql
    
    # USE 문 추가
    if ! grep -q "USE \`dcproject\`" dcproject_structure.sql; then
        sed -i.bak '/^-- Server version/a\
\
--\
-- 데이터베이스 생성 (없다면)\
--\
CREATE DATABASE IF NOT EXISTS `dcproject`\
  DEFAULT CHARACTER SET utf8mb4\
  DEFAULT COLLATE utf8mb4_unicode_ci;\
\
USE `dcproject`;' dcproject_structure.sql
    fi
    
    echo "  ✅ dcproject_structure.sql 수정 완료"
fi

echo ""
echo "✅ 모든 파일 수정 완료!"
echo "백업 파일은 .bak 확장자로 저장되었습니다."



