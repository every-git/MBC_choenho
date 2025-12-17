# Connect to Server 다이얼로그 입력 가이드

> **Workbench와 동일한 환경으로 설정하려면**: `workbench_to_vscode_guide.md` 파일을 참고하세요.

## 📋 필수 입력 항목 (별표 * 표시)

### 1️⃣ 기본 정보
- **Name (연결 이름)**: 이 연결을 구분할 이름 (예: "Local instance 3306", "jdbctest" 등)
- **Group (그룹)**: 연결을 그룹으로 분류 (선택사항, 예: "Local", "Remote" 등)
- **Scope (범위)**: 
  - **Global**: 모든 작업 공간에서 사용 가능 (현재 선택됨)
  - **Workspace**: 현재 작업 공간에서만 사용 (Premium 필요)

### 2️⃣ 서버 연결 정보
- **Host (호스트)**: MySQL 서버 주소
  - 로컬: `localhost` 또는 `127.0.0.1`
  - 원격: 예) `192.168.0.7`
  
- **Port (포트)**: MySQL 서버 포트
  - 기본값: `3306`
  - 커스텀: 예) `3307`

- **Username (사용자명)**: MySQL 사용자 이름
  - 예: `root`, `jdbctest`
  
- **Password (비밀번호)**: 해당 사용자의 비밀번호
  - ⚠️ connections.xml에는 저장되어 있지 않으므로 직접 입력해야 합니다

- **Database (데이터베이스)**: 연결할 기본 데이터베이스
  - 예: `dcproject`, `springdb`
  - 선택사항이지만 연결 즉시 사용할 데이터베이스를 지정하면 편리합니다

---

## 🔧 각 연결별 입력 값

### 연결 1: Local instance 3306
```
Name: Local instance 3306
Group: Local (또는 비워두기)
Host: localhost
Port: 3306
Username: root
Password: [root 비밀번호 입력]
Database: dcproject
SSL: OFF (끄기)
```

### 연결 2: jdbctest
```
Name: jdbctest
Group: Local (또는 비워두기)
Host: 127.0.0.1
Port: 3306
Username: jdbctest
Password: [jdbctest 비밀번호 입력]
Database: dcproject
SSL: ON (켜기) ⚠️ Workbench 설정에 SSL 사용으로 되어있음
```

### 연결 3: NAS_DB
```
Name: NAS_DB
Group: Remote (또는 비워두기)
Host: 192.168.0.7
Port: 3307
Username: jdbctest
Password: [jdbctest 비밀번호 입력]
Database: dcproject
SSL: ON (켜기) ⚠️ Workbench 설정에 SSL 사용으로 되어있음
```

### 연결 4: springdbuser
```
Name: springdbuser
Group: Local (또는 비워두기)
Host: 127.0.0.1
Port: 3306
Username: root
Password: [root 비밀번호 입력]
Database: springdb
SSL: ON (켜기) ⚠️ Workbench 설정에 SSL 사용으로 되어있음
```

---

## ⚙️ 선택사항

### Socket Path (소켓 경로)
- Unix/Linux에서 소켓 파일을 통한 연결 시 사용
- 일반적으로는 비워둠
- 예: `/var/run/mysqld/mysqld.sock`

### SSL 설정
- 로컬 개발 환경: 보통 **OFF**
- 원격 서버: 보통 **ON**
- SSL ON 시 추가 인증서 설정 필요할 수 있음

### Advanced 설정
- 고급 옵션이 필요할 때만 사용
- 타임아웃, 인코딩, 기타 연결 옵션 설정

### Features
- **Event**: MySQL 이벤트 스케줄러 기능 사용 여부
- **Trigger**: 트리거 기능 사용 여부

---

## 💡 팁

1. **비밀번호 확인**: connections.xml에는 비밀번호가 저장되지 않으므로, 실제 MySQL 서버의 비밀번호를 입력해야 합니다.

2. **SSL 설정**: 로컬 개발 환경에서는 SSL을 끄는 것이 일반적입니다. 원격 서버나 프로덕션 환경에서는 보안을 위해 SSL을 켜야 합니다.

3. **연결 테스트**: 입력 후 "Connect" 버튼을 눌러 연결이 되는지 확인하세요.

4. **저장**: 연결이 성공하면 "Save" 버튼을 눌러 나중에 다시 사용할 수 있도록 저장하세요.
