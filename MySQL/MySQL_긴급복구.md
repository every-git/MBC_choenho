# MySQL 긴급 복구 가이드

현재 MySQL이 포트 3306을 열지 못하는 문제가 있습니다. 다음 단계를 따라 수동으로 복구하세요.

## 🔧 복구 단계

### 1단계: 모든 MySQL 프로세스 종료
터미널에서 실행:
```bash
pkill -9 mysqld mysqld_safe
brew services stop mysql
```

### 2단계: 5초 대기
```bash
sleep 5
```

### 3단계: MySQL 정상 모드로 시작
```bash
/opt/homebrew/bin/mysqld_safe --datadir=/opt/homebrew/var/mysql --port=3306 --bind-address=127.0.0.1 > /tmp/mysql.log 2>&1 &
```

### 4단계: 15초 대기 (MySQL 시작 시간)
```bash
sleep 15
```

### 5단계: 연결 테스트
```bash
mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 -e "SELECT '연결 성공!' as Status;"
```

### 6단계: 성공하면 데이터베이스 복구
```bash
# springdb 복구
mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 < database_exports/springdb_structure.sql

# dcproject 복구
mysql -u root -ppassword1234 -h 127.0.0.1 -P 3306 < database_exports/dcproject_structure.sql
```

## ⚠️ 문제 해결

### 연결이 안 되면:
1. 로그 확인:
   ```bash
   tail -20 /tmp/mysql.log
   ```

2. 소켓으로 연결 확인:
   ```bash
   mysql --socket=/tmp/mysql.sock -u root -ppassword1234 -e "SHOW DATABASES;"
   ```

### 계속 안 되면 MySQL 재설치 고려:
```bash
brew services stop mysql
brew uninstall mysql
brew install mysql@8.0
brew services start mysql@8.0
```

## 📋 현재 설정 정보

- **비밀번호**: password1234
- **포트**: 3306
- **데이터베이스**: springdb, dcproject
- **연결 파일**: `~/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json`

