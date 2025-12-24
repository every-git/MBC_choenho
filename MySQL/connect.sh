#!/bin/bash
# MySQL 서버 연결 스크립트
# 사용법: ./connect.sh [connection_name]
# 예: ./connect.sh local_3306

case "$1" in
  "local_3306"|"1")
    echo "연결: Local instance 3306"
    mysql -h localhost -P 3306 -u root -p dcproject
    ;;
  "jdbctest"|"2")
    echo "연결: jdbctest"
    mysql -h 127.0.0.1 -P 3306 -u jdbctest -p dcproject
    ;;
  "nas_db"|"3")
    echo "연결: NAS_DB"
    mysql -h 192.168.0.7 -P 3307 -u jdbctest -p dcproject
    ;;
  "springdb"|"4")
    echo "연결: springdbuser"
    mysql -h 127.0.0.1 -P 3306 -u root -p springdb
    ;;
  *)
    echo "사용 가능한 연결:"
    echo "  1) local_3306   - Local instance 3306 (localhost:3306, root, dcproject)"
    echo "  2) jdbctest     - jdbctest (127.0.0.1:3306, jdbctest, dcproject)"
    echo "  3) nas_db       - NAS_DB (192.168.0.7:3307, jdbctest, dcproject)"
    echo "  4) springdb     - springdbuser (127.0.0.1:3306, root, springdb)"
    echo ""
    echo "사용법: ./connect.sh [connection_name]"
    ;;
esac







