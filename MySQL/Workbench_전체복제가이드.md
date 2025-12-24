# MySQL Workbench 전체 복제 가이드

Workbench의 모든 설정과 데이터베이스 구조를 현재 환경으로 복제하는 방법입니다.

---

## 📋 복제 가능한 항목

### ✅ 이미 복제 완료
1. **연결 설정** (connections.xml) - Database Client에 설정 완료
2. **연결 정보** - JSON 형식으로 변환 완료

### 🔄 추가로 복제 가능한 항목
1. **데이터베이스 구조** (스키마, 테이블, 뷰, 프로시저 등)
2. **SQL 히스토리** (Workbench에서 실행한 SQL 명령어들)
3. **서버 인스턴스 설정** (server_instances.xml)
4. **Workbench 옵션** (wb_options.xml)

---

## 🗄️ 데이터베이스 구조 Export

### 방법 1: 자동 스크립트 사용 (추천)

```bash
./export_database_structure.sh
```

이 스크립트는:
- `dcproject` 데이터베이스 구조 export
- `springdb` 데이터베이스 구조 export
- 결과를 `database_exports/` 폴더에 저장

### 방법 2: 수동으로 mysqldump 실행

#### dcproject 구조만 export (데이터 제외)
```bash
mysqldump -h 127.0.0.1 -P 3306 -u jdbctest -p \
  --no-data \
  --routines \
  --triggers \
  dcproject > dcproject_structure.sql
```

#### springdb 구조만 export (데이터 제외)
```bash
mysqldump -h 127.0.0.1 -P 3306 -u root -p \
  --no-data \
  --routines \
  --triggers \
  springdb > springdb_structure.sql
```

#### 데이터 포함 전체 export
```bash
# dcproject 전체 (구조 + 데이터)
mysqldump -h 127.0.0.1 -P 3306 -u jdbctest -p dcproject > dcproject_full.sql

# springdb 전체 (구조 + 데이터)
mysqldump -h 127.0.0.1 -P 3306 -u root -p springdb > springdb_full.sql
```

---

## 📤 Export 옵션 설명

### `--no-data`
- 데이터를 제외하고 구조만 export
- 테이블 생성문, 인덱스, 제약조건 등 포함

### `--routines`
- 저장 프로시저와 함수 포함

### `--triggers`
- 트리거 포함

### `--single-transaction`
- InnoDB 테이블에서 일관된 스냅샷 생성

### 전체 옵션 예시
```bash
mysqldump -h 127.0.0.1 -P 3306 -u root -p \
  --no-data \
  --routines \
  --triggers \
  --events \
  --single-transaction \
  --add-drop-database \
  --databases springdb > springdb_complete.sql
```

---

## 🔄 Import (다른 서버에 복제)

Export한 SQL 파일을 다른 서버에 import:

```bash
# 구조만 import
mysql -h 127.0.0.1 -P 3306 -u root -p < dcproject_structure.sql

# 전체 import (구조 + 데이터)
mysql -h 127.0.0.1 -P 3306 -u root -p < dcproject_full.sql
```

또는 Database Client에서:
1. SQL 파일 열기
2. 연결 선택
3. 실행

---

## 📁 Workbench 설정 파일 복제

### 자동 스크립트 사용
```bash
./export_workbench_all.sh
```

이 스크립트는:
- 모든 설정 파일 복사
- SQL 히스토리 복사
- `workbench_export/` 폴더에 저장

### 수동 복사
```bash
# 설정 파일 복사
cp ~/Library/Application\ Support/MySQL/Workbench/connections.xml ./
cp ~/Library/Application\ Support/MySQL/Workbench/server_instances.xml ./
cp ~/Library/Application\ Support/MySQL/Workbench/wb_options.xml ./

# SQL 히스토리 복사
cp -r ~/Library/Application\ Support/MySQL/Workbench/sql_history ./
```

---

## ✅ 복제 체크리스트

- [ ] 연결 설정 복제 (Database Client에 설정 완료)
- [ ] dcproject 데이터베이스 구조 export
- [ ] springdb 데이터베이스 구조 export
- [ ] (선택) 데이터 포함 전체 export
- [ ] (선택) SQL 히스토리 복사
- [ ] (선택) Workbench 설정 파일 복사

---

## 🎯 빠른 시작

1. **데이터베이스 구조 export**:
   ```bash
   ./export_database_structure.sh
   ```

2. **결과 확인**:
   ```bash
   ls -lh database_exports/
   ```

3. **다른 환경에 import** (필요시):
   ```bash
   mysql -h 127.0.0.1 -P 3306 -u root -p < database_exports/springdb_structure.sql
   ```

---

## ⚠️ 주의사항

- Export 시 비밀번호가 필요합니다
- 데이터를 포함한 export는 파일 크기가 커질 수 있습니다
- Import 전에 데이터베이스가 생성되어 있어야 합니다
- 권한 문제가 발생할 수 있으므로 root 권한으로 실행하는 것을 권장합니다




