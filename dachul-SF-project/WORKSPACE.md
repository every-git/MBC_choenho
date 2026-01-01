# Workspace Context

Eclipse IDE 워크스페이스 환경. Spring Framework 프로젝트 개발 환경.

## Environment

- IDE: Eclipse IDE for Enterprise Java and Web Developers
- Java: JDK 21
- Server: Apache Tomcat 10.1
- Build Tool: Maven 3.x
- SCM: Git

## Workspace Structure

```
dachul-SF-project/
├── .metadata/           # Eclipse 워크스페이스 메타데이터
├── Servers/             # Tomcat 서버 설정
│   └── Tomcat v10.1 Server at localhost-config/
├── dc-SF/               # 대철이제철 게시판 프로젝트 (Spring Framework)
└── AGENTS.md            # 루트 거버넌스 문서
```

# Server Configuration

## Tomcat v10.1 Settings

### Location

- Configuration: ./Servers/Tomcat v10.1 Server at localhost-config/
- server.xml: Tomcat 서버 설정
- context.xml: 컨텍스트 설정

### Server Settings

#### Port Configuration

- HTTP: 8080
- HTTPS: 8443 (선택사항)
- AJP: 8009
- Shutdown: 8005

#### Context Path

- 프로젝트 Context Root: / (루트)
- 접속 URL: <http://localhost:8080/>

### Server.xml Configuration

```xml
<Server port="8005" shutdown="SHUTDOWN">
    <Service name="Catalina">
        <Connector port="8080" protocol="HTTP/1.1"
                   connectionTimeout="20000"
                   redirectPort="8443"
                   URIEncoding="UTF-8" />

        <Engine name="Catalina" defaultHost="localhost">
            <Host name="localhost" appBase="webapps"
                  unpackWARs="true" autoDeploy="true">
            </Host>
        </Engine>
    </Service>
</Server>
```

# Eclipse Project Settings

## dc-SF Project

### Project Facets

- Dynamic Web Module 6.0
- Java 21
- Maven
- Spring

### Build Path

- JRE System Library: JavaSE-21
- Maven Dependencies
- Apache Tomcat v10.1

### Deployment Assembly

- src/main/java -> WEB-INF/classes
- src/main/resources -> WEB-INF/classes
- src/main/webapp -> /
- Maven Dependencies -> WEB-INF/lib

# Operational Commands

## Server Management (Eclipse)

### Start Server

1. Servers 탭 열기
2. "Tomcat v10.1 Server at localhost" 우클릭
3. Start 선택

### Stop Server

1. Servers 탭에서 서버 우클릭
2. Stop 선택

### Restart Server

1. Servers 탭에서 서버 우클릭
2. Restart 선택

### Clean Server

1. Servers 탭에서 서버 우클릭
2. Clean... 선택
3. OK

### Debug Mode

1. Servers 탭에서 서버 우클릭
2. Debug 선택

## Project Management

### Clean & Build

1. 프로젝트 우클릭
2. Maven -> Update Project
3. Clean 체크
4. OK

### Maven Build

1. 프로젝트 우클릭
2. Run As -> Maven build...
3. Goals: clean install
4. Run

### Deploy to Server

1. 프로젝트 우클릭
2. Run As -> Run on Server
3. Tomcat v10.1 Server 선택
4. Finish

## Troubleshooting

### 서버 시작 실패

1. Clean Server
2. Clean Project
3. Maven Update Project
4. 서버 재시작

### Port Already in Use

1. 다른 Tomcat 프로세스 종료
2. server.xml에서 포트 변경
3. 서버 재시작

### 변경사항 반영 안 됨

1. Clean Server
2. Clean Project
3. 프로젝트 재배포

### Out of Memory

1. Eclipse 실행 옵션 수정 (eclipse.ini)
2. -Xms512m -Xmx2048m

# Development Workflow

## 일반 개발 흐름

1. Eclipse에서 코드 작성
2. 자동 빌드 (Build Automatically 활성화)
3. 서버 재시작 (변경사항 반영)
4. 브라우저에서 테스트
5. Git Commit & Push

## 최근 업데이트 (2025-12-30)

### 댓글 기능 추가
- ReplyDTO, ReplyMapper, ReplyService, ReplyController 구현
- REST API 기반 댓글 CRUD 기능
- 댓글 페이징 처리 (ReplyListPaginDTO)
- 댓글 작성/수정/삭제 UI 구현

### 게시글 목록 페이징 처리
- BoardListPaginDTO 추가
- BoardMapper에 페이징 쿼리 메서드 추가
- 게시글 목록 페이지에 페이징 UI 추가

### UI/UX 개선
- CSS에서 모든 이모티콘 제거
- 깔끔하고 직관적인 네이버 스타일 디자인 적용
- 메타 정보, 테이블, 댓글 섹션 스타일 개선

### 버그 수정
- @Param 어노테이션 import 추가
- 댓글 등록 시 JavaScript 변수 선언 순서 수정
- bno 파라미터 숫자 변환 처리

## 데이터베이스 업데이트

### 댓글 테이블 생성 (필수)

댓글 기능 사용을 위해 데이터베이스에 `reply` 테이블을 생성해야 합니다.

#### 방법 1: 전체 스크립트 실행 (권장)

```bash
mysql -u daechuluser -p1234 < dc-SF/setup_database.sql
```

#### 방법 2: 댓글 테이블만 생성

```bash
mysql -u daechuluser -p1234 < dc-SF/create_reply_table.sql
```

#### 방법 3: MySQL 클라이언트에서 직접 실행

```sql
USE daechuldb;

CREATE TABLE IF NOT EXISTS reply (
    rno INT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 번호',
    bno INT NOT NULL COMMENT '게시글 번호',
    replyText VARCHAR(500) NOT NULL COMMENT '댓글 내용',
    replyer VARCHAR(50) NOT NULL COMMENT '댓글 작성자',
    replydate TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '댓글 작성일시',
    updatedate TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '댓글 수정일시',
    deflag BOOLEAN DEFAULT FALSE COMMENT '삭제 여부 (논리적 삭제)',
    FOREIGN KEY (bno) REFERENCES board(seq) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_reply_bno ON reply(bno);
CREATE INDEX idx_reply_regdate ON reply(replydate DESC);
```

### 테이블 생성 확인

```sql
USE daechuldb;
SHOW TABLES LIKE 'reply';
DESCRIBE reply;
```

## Hot Swap (빈번한 변경 시)

1. 서버를 Debug 모드로 시작
2. Java 코드 변경 시 자동 Hot Swap
3. JSP, XML 변경 시 서버 재시작 불필요

# Git Configuration

## .gitignore

프로젝트에 포함하지 않을 파일:

```
# Eclipse
.metadata/
.settings/
.project
.classpath

# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup

# Server
Servers/

# Logs
logs/
*.log

# OS
.DS_Store
Thumbs.db

# IDE
*.swp
*.swo
*~
```

# Best Practices

## Eclipse Settings

### Auto Build

- Project -> Build Automatically 활성화

### Save Actions

- Save Actions 활성화 (자동 포맷팅)
- Remove trailing whitespace
- Organize imports

### Code Formatter

- Java 코드 포맷터 설정 (Tab = 4 spaces)

### Text Encoding

- Workspace: UTF-8
- JSP: UTF-8

## Server Settings

### Development

- Auto Reload 활성화
- Debug 모드 사용
- 로그 레벨: DEBUG

### Production

- Auto Reload 비활성화
- 로그 레벨: INFO
- Connection Pool 최적화

# Related Contexts

- Main Project: ./dc-SF/AGENTS.md
- Root Governance: ./AGENTS.md
