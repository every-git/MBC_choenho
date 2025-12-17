#!/bin/bash
# MySQL 연결 테스트 스크립트

echo "🔍 MySQL 연결 테스트..."
echo ""

echo "1️⃣ root 계정으로 localhost 연결 테스트:"
mysql -h localhost -P 3306 -u root -p -e "SELECT 'Connection successful!' as Status, DATABASE() as CurrentDB;" 2>&1

echo ""
echo "2️⃣ root 계정으로 127.0.0.1 연결 테스트:"
mysql -h 127.0.0.1 -P 3306 -u root -p -e "SELECT 'Connection successful!' as Status, DATABASE() as CurrentDB;" 2>&1

echo ""
echo "3️⃣ springdb 데이터베이스 존재 여부 확인:"
mysql -h 127.0.0.1 -P 3306 -u root -p -e "SHOW DATABASES LIKE 'springdb';" 2>&1

echo ""
echo "4️⃣ springdb 데이터베이스 내 테이블 확인 (존재하는 경우):"
mysql -h 127.0.0.1 -P 3306 -u root -p -e "USE springdb; SHOW TABLES;" 2>&1


