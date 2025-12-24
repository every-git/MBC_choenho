# Workbench와 동일한 환경 설정 가이드

## 📋 Connect to Server 다이얼로그 입력값 (Workbench 기준)

Workbench의 connections.xml에서 추출한 정확한 설정값입니다.

---

### 연결 1: Local instance 3306

```
Name: Local instance 3306
Group: (비워두기)
Scope: Global

Server Type: MySQL

Host: localhost
Port: 3306
Username: root
Password: [실제 비밀번호 입력]
Database: dcproject

SSL: OFF (끄기) ⚠️ Workbench에서 SSL 미사용
```

**주요 차이점**: 이 연결만 SSL을 사용하지 않습니다.

---

### 연결 2: jdbctest

```
Name: jdbctest
Group: (비워두기)
Scope: Global

Server Type: MySQL

Host: 127.0.0.1
Port: 3306
Username: jdbctest
Password: [실제 비밀번호 입력]
Database: dcproject

SSL: ON (켜기) ⚠️ Workbench에서 SSL 사용
```

**Workbench 설정 상세**:
- `useSSL: 1` (SSL 사용)
- `DbSqlEditor:LastDefaultSchema: dcproject` (기본 스키마)

---

### 연결 3: NAS_DB

```
Name: NAS_DB
Group: Remote (또는 비워두기)
Scope: Global

Server Type: MySQL

Host: 192.168.0.7
Port: 3307
Username: jdbctest
Password: [실제 비밀번호 입력]
Database: dcproject

SSL: ON (켜기) ⚠️ Workbench에서 SSL 사용
```

**Workbench 설정 상세**:
- `useSSL: 1` (SSL 사용)
- `DbSqlEditor:LastDefaultSchema: dcproject` (기본 스키마)
- 서버 버전: 8.0.44

---

### 연결 4: springdbuser

```
Name: springdbuser
Group: (비워두기)
Scope: Global

Server Type: MySQL

Host: 127.0.0.1
Port: 3306
Username: root
Password: [실제 비밀번호 입력]
Database: springdb

SSL: ON (켜기) ⚠️ Workbench에서 SSL 사용
```

**Workbench 설정 상세**:
- `useSSL: 1` (SSL 사용)
- `DbSqlEditor:LastDefaultSchema: springdb` (기본 스키마)

---

## 🔍 Workbench와 동일하게 설정하기 위한 체크리스트

### ✅ 반드시 확인해야 할 항목:

1. **SSL 설정**
   - ✅ Local instance 3306: **SSL OFF**
   - ✅ 나머지 모든 연결: **SSL ON**

2. **기본 스키마/데이터베이스**
   - ✅ Local instance 3306: `dcproject`
   - ✅ jdbctest: `dcproject`
   - ✅ NAS_DB: `dcproject`
   - ✅ springdbuser: `springdb`

3. **포트 번호**
   - ✅ 대부분: `3306`
   - ✅ NAS_DB만: `3307`

4. **호스트 주소**
   - ✅ Local instance 3306: `localhost` (다른 연결과 다름!)
   - ✅ 나머지: `127.0.0.1` 또는 `192.168.0.7`

---

## 📝 빠른 참고표

| 연결 이름 | Host | Port | User | Database | SSL | 비고 |
|---------|------|------|------|----------|-----|------|
| Local instance 3306 | **localhost** | 3306 | root | dcproject | **OFF** | ⚠️ SSL 끄기 |
| jdbctest | 127.0.0.1 | 3306 | jdbctest | dcproject | **ON** | |
| NAS_DB | 192.168.0.7 | **3307** | jdbctest | dcproject | **ON** | ⚠️ 다른 포트 |
| springdbuser | 127.0.0.1 | 3306 | root | **springdb** | **ON** | ⚠️ 다른 DB |

---

## ⚠️ 중요 사항

1. **비밀번호**: connections.xml에는 비밀번호가 저장되어 있지 않으므로, 각 사용자의 실제 비밀번호를 입력해야 합니다.

2. **SSL 인증서**: SSL을 사용하는 연결의 경우, 인증서 경로가 비어있으므로 기본 SSL 설정으로 연결을 시도합니다. 연결이 안 되면 SSL을 끄거나 SSL 설정을 조정해야 할 수 있습니다.

3. **로컬 개발 환경**: 로컬 개발에서는 SSL을 끄는 것이 일반적이지만, Workbench 설정을 그대로 따르려면 위의 설정을 따라야 합니다.

---

## 🔧 연결 테스트 순서

1. **Local instance 3306** 먼저 테스트 (SSL OFF, 가장 간단)
2. **springdbuser** 테스트 (SSL ON, 로컬)
3. **jdbctest** 테스트 (SSL ON, 로컬)
4. **NAS_DB** 테스트 (SSL ON, 원격, 다른 포트)

연결이 안 되면:
- SSL을 일시적으로 끄고 테스트
- 비밀번호 확인
- 방화벽 설정 확인 (특히 NAS_DB)







