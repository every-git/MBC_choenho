# 데이터 복원 가이드

## 복원 가능한 방법들

### 1. Time Machine 백업 확인 (가장 가능성 높음)

맥북 재설정 전에 Time Machine 백업이 있었다면:

1. **Time Machine 열기**
   - 시스템 설정 → Time Machine
   - 또는 메뉴바의 Time Machine 아이콘 클릭

2. **MySQL 데이터 디렉토리 복원**
   - Time Machine에서 다음 경로로 이동:
     - `/opt/homebrew/var/mysql/springdb/` (Homebrew 설치 시)
     - 또는 `/usr/local/mysql/data/springdb/` (공식 설치 시)
   - 재설정 전 날짜로 이동
   - `springdb` 폴더 전체 복원

3. **복원 후 MySQL 재시작**
   ```bash
   brew services restart mysql
   ```

### 2. MySQL 데이터 디렉토리 백업 확인

다른 위치에 백업이 있을 수 있습니다:

```bash
# 홈 디렉토리에서 백업 찾기
find ~ -name "*mysql*backup*" -o -name "*springdb*" 2>/dev/null

# 외부 드라이브 확인
find /Volumes -name "*mysql*" -o -name "*springdb*" 2>/dev/null
```

### 3. SQL 덤프 파일 확인

이전에 export한 SQL 파일이 있는지 확인:

```bash
# 프로젝트 디렉토리에서
find . -name "*.sql" -exec grep -l "INSERT INTO" {} \;

# 홈 디렉토리에서
find ~ -name "*dump*.sql" -o -name "*backup*.sql" 2>/dev/null
```

### 4. Git 히스토리 확인

Git에 데이터가 커밋되어 있다면 (일반적이지 않지만):

```bash
cd "/Volumes/Samsung X5/dev_study/MBC_choenho/SF_2025_12-main 2"
git log --all --full-history -- "*.sql"
git log --all --full-history -- "*.dump"
```

### 5. iCloud/클라우드 백업 확인

- iCloud Drive
- Dropbox
- Google Drive
- OneDrive

등에 백업이 있는지 확인

### 6. 다른 컴퓨터/서버 확인

- 다른 개발 머신
- 원격 서버
- Docker 컨테이너

## 복원 불가능한 경우

백업이 없다면 데이터는 복원할 수 없습니다. 하지만:

1. **테이블 구조는 이미 복원됨**
   - `tbl_board`
   - `tbl_reply`
   - `tbl_member`

2. **새로 데이터 입력 가능**
   - 애플리케이션을 실행하고
   - 게시판에서 새 글 작성

## 빠른 확인 명령어

```bash
# Time Machine 백업 확인
tmutil listbackups | tail -5

# MySQL 데이터 디렉토리 확인
ls -la /opt/homebrew/var/mysql/springdb/

# 백업 파일 검색
find ~ -name "*springdb*" -type f 2>/dev/null
```

