# MySQL 복구 가이드

## 🚨 현재 상황
맥북 재설정 후 MySQL이 망가진 상태

---

## ✅ 1단계: MySQL 서버 상태 확인

MySQL은 실행 중입니다:
- 설치 위치: `/opt/homebrew/bin/mysql`
- 서비스 상태: 실행 중

---

## 🔧 2단계: 비밀번호 확인 및 재설정

### 비밀번호 없이 접속 가능한지 확인:
```bash
mysql -u root
```

### 안 되면 비밀번호 재설정:
```bash
# MySQL 안전 모드로 시작
brew services stop mysql
mysqld_safe --skip-grant-tables &

# 새 터미널에서
mysql -u root
USE mysql;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password1234';
FLUSH PRIVILEGES;
EXIT;

# MySQL 재시작
brew services restart mysql
```

---

## 🗄️ 3단계: 데이터베이스 복구

### springdb 복구:
```bash
mysql -u root -p < database_exports/springdb_structure.sql
```

### dcproject 복구:
```bash
mysql -u root -p < database_exports/dcproject_structure.sql
```

---

## 🔌 4단계: Database Client 연결 설정 복구

연결 설정 파일은 이미 복구했습니다:
```
~/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json
```

**비밀번호 업데이트 필요:**
1. 파일 열기: `open ~/Library/Application\ Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json`
2. 모든 `"password": "1234"` 부분을 실제 비밀번호로 변경

---

## 🚀 빠른 복구 방법

```bash
# 복구 스크립트 실행
./복구_스크립트.sh
```

스크립트가 자동으로:
- 데이터베이스 복구
- 연결 설정 파일 복구

---

## 📋 체크리스트

- [ ] MySQL 비밀번호 확인/재설정
- [ ] springdb 데이터베이스 복구
- [ ] dcproject 데이터베이스 복구  
- [ ] Database Client 연결 설정 복구
- [ ] 연결 설정 파일의 비밀번호 업데이트
- [ ] Cursor 재시작
- [ ] 연결 테스트

---

## ⚠️ 중요

재설정 후 MySQL 비밀번호가 바뀌었을 수 있습니다.
먼저 비밀번호를 확인하거나 재설정해야 합니다.
