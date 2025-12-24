#!/bin/bash
killall -9 mysqld mysqld_safe 2>/dev/null; sleep 2; brew services stop mysql 2>/dev/null; sleep 1; /opt/homebrew/bin/mysqld_safe --datadir=/opt/homebrew/var/mysql --port=3306 --bind-address=127.0.0.1 > /tmp/mysql.log 2>&1 & sleep 12; mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 -e "SELECT 1;" && echo "✅ 성공" || echo "❌ 실패"

