# MySQL 진단 및 수정 가이드

## 현재 상황
- 맥북 재설정 후 MySQL 재설치 완료
- MySQLWorkbench 접속 시 종료됨
- VS Code MySQL 익스텐션 접속 불가
- sp1 프로젝트 404 에러 발생

## 진단 단계

### 1단계: MySQL 상태 확인

터미널에서 실행:
```bash
cd "/Volumes/Samsung X5/dev_study/MBC_choenho/SF_2025_12-main 2"
chmod +x test_mysql_connection.sh
./test_mysql_connection.sh
```

이 스크립트가 다음을 확인합니다:
- MySQL 프로세스 실행 여부
- 포트 3306 사용 여부
- MySQL 명령어 경로
- root/springdbuser 연결 테스트
- springdb 데이터베이스 존재 여부

### 2단계: MySQL 초기 설정 (데이터베이스/사용자 생성)

MySQL이 정상적으로 실행 중이라면:
```bash
chmod +x setup_mysql_after_reinstall.sh
./setup_mysql_after_reinstall.sh
```

또는 수동으로:

1. **MySQL 접속** (터미널에서):
   ```bash
   mysql -u root
   # 또는 비밀번호가 있다면
   mysql -u root -p
   ```

2. **다음 SQL 실행**:
   ```sql
   -- root 비밀번호 설정 (필요시)
   ALTER USER 'root'@'localhost' IDENTIFIED BY '1234';
   FLUSH PRIVILEGES;
   
   -- 데이터베이스 및 사용자 생성
   CREATE DATABASE springdb;
   CREATE USER 'springdbuser'@'localhost' IDENTIFIED BY '1234';
   CREATE USER 'springdbuser'@'%' IDENTIFIED BY '1234';
   GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'localhost';
   GRANT ALL PRIVILEGES ON springdb.* TO 'springdbuser'@'%';
   FLUSH PRIVILEGES;
   
   -- 테이블 생성
   USE springdb;
   CREATE TABLE table_board(
       bno INT AUTO_INCREMENT PRIMARY KEY,
       title VARCHAR(500) NOT NULL,
       content VARCHAR(2000) NOT NULL,
       writer VARCHAR(50) NOT NULL,
       regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       delflag BOOLEAN DEFAULT FALSE
   );
   
   -- 확인
   SHOW DATABASES;
   SELECT User, Host FROM mysql.user WHERE User = 'springdbuser';
   SHOW TABLES;
   EXIT;
   ```

### 3단계: MySQLWorkbench 연결 설정

MySQLWorkbench에서 새 연결 생성:

1. **MySQL Connections**에서 `+` 버튼 클릭
2. **Connection Name**: `springdbuser`
3. **Hostname**: `localhost` 또는 `127.0.0.1`
4. **Port**: `3306`
5. **Username**: `springdbuser`
6. **Password**: `Store in Keychain` 체크 후 `1234` 입력
7. **Default Schema**: `springdb`
8. **Test Connection** 클릭하여 연결 테스트

**연결 시 종료되는 문제 해결:**

MySQLWorkbench가 종료되는 경우:
- MySQL 서버가 불안정할 수 있음
- MySQL 에러 로그 확인 필요:
  ```bash
  tail -f /usr/local/mysql/data/*.err
  # 또는 Homebrew 설치 시
  tail -f /opt/homebrew/var/mysql/*.err
  ```

### 4단계: Spring 애플리케이션 설정 확인

**root-context.xml** 설정 확인:
- 파일 위치: `sp1/src/main/webapp/WEB-INF/spring/root-context.xml`
- 현재 설정 (정상):
  ```xml
  <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/springdb?serverTimezone=Asia/Seoul" />
  <property name="username" value="springdbuser" />
  <property name="password" value="1234" />
  ```

### 5단계: 프로젝트 빌드 및 서버 실행

1. **프로젝트 Clean & Build**
   - Eclipse/STS: 프로젝트 우클릭 → Clean...
   - 또는 터미널: `cd sp1 && mvn clean install`

2. **Tomcat 서버 재시작**
   - 서버 정지 → Clean → 프로젝트 재배포 → 시작

3. **접속 테스트**
   - `http://localhost:8080/`
   - `http://localhost:8080/board/list`
   - `http://localhost:8080/sample/ex1`

## 문제별 해결 방법

### MySQL 서버가 시작되지 않는 경우

**Homebrew로 설치한 경우:**
```bash
brew services start mysql
# 또는
mysql.server start
```

**공식 설치인 경우:**
```bash
sudo /usr/local/mysql/support-files/mysql.server start
```

**시스템 환경 설정:**
- 시스템 설정 → MySQL → Start MySQL Server

### 접속이 안 되는 경우

1. **MySQL 서버 프로세스 확인**:
   ```bash
   ps aux | grep mysql
   ```

2. **포트 확인**:
   ```bash
   lsof -i :3306
   ```

3. **MySQL 에러 로그 확인**:
   ```bash
   tail -100 /usr/local/mysql/data/*.err
   ```

### 데이터베이스/사용자가 없는 경우

위의 2단계를 실행하여 생성

## 확인 체크리스트

- [ ] MySQL 서버 실행 중
- [ ] 포트 3306 열림
- [ ] root로 접속 가능
- [ ] springdb 데이터베이스 존재
- [ ] springdbuser 사용자 존재 및 권한 부여됨
- [ ] table_board 테이블 생성됨
- [ ] MySQLWorkbench에서 연결 성공
- [ ] VS Code MySQL 익스텐션에서 연결 성공
- [ ] Spring 애플리케이션 시작 성공
- [ ] 브라우저에서 페이지 로드 성공
