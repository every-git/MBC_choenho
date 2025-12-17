# Workbench Name & Group 정보 (connections.xml 기반)

## ✅ Workbench에 저장된 정확한 Name 정보

connections.xml 파일에서 추출한 각 연결의 정확한 이름입니다:

### 연결 1
- **Name**: `Local instance 3306` (라인 17에서 확인)
- **Host**: localhost
- **User**: root
- **Database**: dcproject

### 연결 2
- **Name**: `jdbctest` (라인 41에서 확인)
- **Host**: 127.0.0.1
- **User**: jdbctest
- **Database**: dcproject

### 연결 3
- **Name**: `NAS_DB` (라인 65에서 확인)
- **Host**: 192.168.0.7
- **User**: jdbctest
- **Database**: dcproject

### 연결 4
- **Name**: `springdbuser` (라인 89에서 확인)
- **Host**: 127.0.0.1
- **User**: root
- **Database**: springdb

---

## 📋 Group 정보

**Group 정보는 connections.xml에 명시적으로 저장되어 있지 않습니다.**

하지만 연결 특성을 보면 다음과 같이 구분할 수 있습니다:

- **Local instance 3306**: localhost → `Local` 그룹 추천
- **jdbctest**: 127.0.0.1 (로컬) → `Local` 그룹 추천
- **NAS_DB**: 192.168.0.7 (원격 서버) → `Remote` 그룹 추천
- **springdbuser**: 127.0.0.1 (로컬) → `Local` 그룹 추천

**Group은 선택사항이므로 비워두어도 됩니다!**

---

## 🎯 Connect to Server 다이얼로그 입력값 (정확한 Name 사용)

### 연결 1: Local instance 3306
```
Name:        Local instance 3306  ← Workbench에서 사용한 정확한 이름
Group:       Local (또는 비워두기)
Host:        localhost
Port:        3306
Username:    root
Password:    [비밀번호]
Database:    dcproject
SSL:         끄기 ❌
```

### 연결 2: jdbctest
```
Name:        jdbctest  ← Workbench에서 사용한 정확한 이름
Group:       Local (또는 비워두기)
Host:        127.0.0.1
Port:        3306
Username:    jdbctest
Password:    [비밀번호]
Database:    dcproject
SSL:         켜기 ✅
```

### 연결 3: NAS_DB
```
Name:        NAS_DB  ← Workbench에서 사용한 정확한 이름
Group:       Remote (또는 비워두기)  ← 원격 서버이므로 Remote 추천
Host:        192.168.0.7
Port:        3307
Username:    jdbctest
Password:    [비밀번호]
Database:    dcproject
SSL:         켜기 ✅
```

### 연결 4: springdbuser
```
Name:        springdbuser  ← Workbench에서 사용한 정확한 이름
Group:       Local (또는 비워두기)
Host:        127.0.0.1
Port:        3306
Username:    root
Password:    [비밀번호]
Database:    springdb
SSL:         켜기 ✅
```

---

## 💡 요약

### Name (이름)
- **Workbench에서 사용한 정확한 이름을 사용하세요!**
- 연결 1: `Local instance 3306`
- 연결 2: `jdbctest`
- 연결 3: `NAS_DB`
- 연결 4: `springdbuser`

### Group (그룹)
- **선택사항** - 비워두어도 됩니다
- 원하면 다음처럼 구분 가능:
  - Local 그룹: Local instance 3306, jdbctest, springdbuser
  - Remote 그룹: NAS_DB

---

## ⚠️ 중요

**Name은 Workbench와 동일하게 입력하는 것을 추천합니다!**
- 나중에 Workbench와 비교할 때 편리합니다
- 다른 개발자와 협업할 때도 일관성 유지됩니다

하지만 **아무 이름이나 입력해도 기능상 문제는 없습니다**. 단지 구분을 위한 이름일 뿐입니다.



