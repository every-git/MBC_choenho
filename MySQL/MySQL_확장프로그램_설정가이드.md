# MySQL 확장 프로그램 설정 가이드

Workbench 연결 정보를 VS Code/Cursor MySQL 확장 프로그램에 설정하는 방법입니다.

---

## 🔧 확장 프로그램별 설정 방법

### 1️⃣ Database Client (cweijan.vscode-database-client2)
**가장 인기있는 MySQL 확장 프로그램**

#### 설정 파일 위치
- **macOS (Cursor)**: 
  ```
  ~/Library/Application Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json
  ```
- **macOS (VS Code)**: 
  ```
  ~/Library/Application Support/Code/User/globalStorage/cweijan.vscode-database-client2/connections.json
  ```

#### 설정 방법
1. 위 경로의 `connections.json` 파일을 엽니다
2. `database-client-connections.json` 파일의 내용을 복사해서 붙여넣습니다
3. 각 연결의 `password` 필드에 실제 비밀번호를 입력합니다
4. 확장 프로그램을 재시작하거나 새로고침합니다

#### 또는 GUI로 설정
1. 확장 프로그램 아이콘 클릭 (Database Client)
2. "+" 버튼 클릭하여 새 연결 추가
3. 아래 표의 값들을 입력:

| 연결 이름 | Host | Port | User | Password | Database | SSL |
|---------|------|------|------|----------|----------|-----|
| Local instance 3306 | localhost | 3306 | root | [입력] | dcproject | ❌ |
| jdbctest | 127.0.0.1 | 3306 | jdbctest | [입력] | dcproject | ✅ |
| NAS_DB | 192.168.0.7 | 3307 | jdbctest | [입력] | dcproject | ✅ |
| springdbuser | 127.0.0.1 | 3306 | root | [입력] | springdb | ✅ |

---

### 2️⃣ SQLTools + MySQL Driver

#### 설정 파일 위치
프로젝트 루트 또는 사용자 홈 디렉토리에 `.sqltoolsrc.json` 파일을 생성합니다.

#### 설정 방법
1. 프로젝트 루트에 `.sqltoolsrc.json` 파일이 이미 생성되어 있습니다
2. 각 연결의 `password` 필드에 실제 비밀번호를 입력합니다
3. SQLTools 확장 프로그램이 자동으로 파일을 읽습니다

#### 또는 GUI로 설정
1. SQLTools 확장 프로그램 설치
2. MySQL/MariaDB 드라이버 설치
3. SQLTools 아이콘 클릭 → "Add New Connection"
4. 연결 정보 입력

---

## 📋 각 연결 상세 정보

### 연결 1: Local instance 3306
```
Name: Local instance 3306
Host: localhost
Port: 3306
Username: root
Password: [실제 비밀번호 입력]
Database: dcproject
SSL: 끄기 ❌
```

### 연결 2: jdbctest
```
Name: jdbctest
Host: 127.0.0.1
Port: 3306
Username: jdbctest
Password: [실제 비밀번호 입력]
Database: dcproject
SSL: 켜기 ✅
```

### 연결 3: NAS_DB
```
Name: NAS_DB
Host: 192.168.0.7
Port: 3307  ⚠️ 주의!
Username: jdbctest
Password: [실제 비밀번호 입력]
Database: dcproject
SSL: 켜기 ✅
```

### 연결 4: springdbuser
```
Name: springdbuser
Host: 127.0.0.1
Port: 3306
Username: root
Password: [실제 비밀번호 입력]
Database: springdb  ⚠️ 주의!
SSL: 켜기 ✅
```

---

## 🚀 빠른 설정 (Database Client 자동 설정)

터미널에서 다음 명령어를 실행하면 자동으로 설정 파일을 복사할 수 있습니다:

### macOS (Cursor)
```bash
# 설정 파일 위치 확인 후 복사
mkdir -p ~/Library/Application\ Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/
cp database-client-connections.json ~/Library/Application\ Support/Cursor/User/globalStorage/cweijan.vscode-database-client2/connections.json
```

### macOS (VS Code)
```bash
mkdir -p ~/Library/Application\ Support/Code/User/globalStorage/cweijan.vscode-database-client2/
cp database-client-connections.json ~/Library/Application\ Support/Code/User/globalStorage/cweijan.vscode-database-client2/connections.json
```

**⚠️ 중요**: 복사 후 `connections.json` 파일을 열어서 각 연결의 `password` 필드에 실제 비밀번호를 입력해야 합니다!

---

## ✅ 설정 확인

1. VS Code/Cursor를 재시작합니다
2. Database Client 또는 SQLTools 확장 프로그램 아이콘을 클릭합니다
3. 왼쪽 사이드바에 연결 목록이 표시되는지 확인합니다
4. 연결을 클릭하여 연결을 테스트합니다

---

## 🔍 문제 해결

### 연결이 안 될 때
1. **비밀번호 확인**: `password` 필드에 실제 비밀번호를 입력했는지 확인
2. **SSL 설정**: 로컬 개발 환경에서는 SSL을 끄고 테스트
3. **포트 확인**: NAS_DB는 3307 포트 사용
4. **방화벽**: NAS_DB는 원격 서버이므로 방화벽 설정 확인

### 설정 파일을 찾을 수 없을 때
1. 확장 프로그램이 설치되어 있는지 확인
2. 최소 한 번은 확장 프로그램 GUI를 통해 연결을 시도 (이렇게 하면 설정 파일이 생성됨)
3. 그 다음 위의 설정 파일 경로를 확인

---

## 📝 생성된 파일 목록

- `database-client-connections.json` - Database Client 확장 프로그램용
- `.sqltoolsrc.json` - SQLTools 확장 프로그램용
- `.vscode/database-connections.json` - 범용 형식

이 파일들을 참고하여 사용하는 확장 프로그램에 맞게 설정하세요!
