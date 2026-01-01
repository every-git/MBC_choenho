# 🦀 daechul-SF

대철이제철 게시판 프로젝트 - Spring Framework 마이그레이션 버전

## 프로젝트 개요

daechul-project (Servlet+DAO 패턴)를 **Spring Legacy Framework**로 마이그레이션한 게시판 시스템입니다.

> "대게가 지금 제철이라고? 우리 학원은 대철이 제철!"

## 기술 스택

| 구분 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Framework 6.2.1 |
| Security | Spring Security 6.4.2 |
| ORM | MyBatis 3.5.17 |
| Database | MySQL 8.x |
| View | JSP + JSTL |
| Build | Maven |
| Server | Apache Tomcat 10.1 |

## 시작하기

### 1. 데이터베이스 설정

먼저 MySQL에 데이터베이스와 테이블을 생성합니다:

```bash
mysql -u root -p < setup_database.sql
```

또는 MySQL Workbench에서 `setup_database.sql` 파일을 실행합니다.

**기존 데이터베이스에 댓글 테이블만 추가하는 경우:**

```bash
mysql -u daechuluser -p1234 < create_reply_table.sql
```

**테스트 계정:**

- 관리자: `admin` / `admin123`
- 일반 회원: `user01` / `user123`

**데이터베이스 테이블:**

- `members`: 회원 정보
- `member_roles`: 회원 권한 (Spring Security)
- `board`: 게시글
- `reply`: 댓글 (댓글 기능 사용 시 필수)
- `persistent_logins`: Remember Me 토큰 (Spring Security)

### 2. 이클립스 설정

1. **프로젝트 Import**
   - `File` → `Import` → `Existing Maven Projects`
   - 프로젝트 폴더 선택 → `Finish`

2. **Lombok 설치** (필수)
   - [LOMBOK_SETUP.md](LOMBOK_SETUP.md) 참조

3. **Maven Update**
   - 프로젝트 우클릭 → `Maven` → `Update Project...`

### 3. Tomcat 서버 설정

1. **Tomcat 10.1 다운로드**
   - <https://tomcat.apache.org/download-10.cgi>

2. **서버 추가**
   - `Window` → `Preferences` → `Server` → `Runtime Environments`
   - `Add...` → `Apache Tomcat v10.1` → Tomcat 경로 선택

3. **프로젝트 배포**
   - Servers 뷰에서 서버 우클릭 → `Add and Remove...`
   - `daechul-SF` 추가 → `Finish`

4. **서버 시작**
   - 서버 우클릭 → `Start`
   - 브라우저에서 `http://localhost:8080/` 접속

## 프로젝트 구조

```
src/main/java/org/zerock/
├── controller/          # HTTP 요청 처리
│   ├── HomeController.java
│   ├── BoardController.java
│   ├── MemberController.java
│   └── ReplyController.java
├── service/             # 비즈니스 로직
│   ├── BoardService.java
│   ├── BoardServiceImpl.java
│   ├── MemberService.java
│   ├── MemberServiceImpl.java
│   ├── ReplyService.java
│   └── ReplyServiceImpl.java
├── mapper/              # MyBatis 인터페이스
│   ├── BoardMapper.java
│   ├── MemberMapper.java
│   └── ReplyMapper.java
├── dto/                 # 데이터 전송 객체
│   ├── BoardDTO.java
│   ├── BoardListPaginDTO.java
│   ├── MemberDTO.java
│   ├── ReplyDTO.java
│   └── ReplyListPaginDTO.java
└── security/            # Spring Security 설정
    ├── SecurityConfig.java
    ├── PasswordEncoderConfig.java
    ├── CustomUserDetailsService.java
    ├── CustomLoginSuccessHandler.java
    └── Custom403Handler.java
```

## 주요 기능

### 댓글 기능
- 댓글 등록/조회/수정/삭제 (REST API)
- 댓글 페이징 처리 (페이지당 10개)
- 게시글별 댓글 목록 조회
- 댓글 작성자 정보 표시
- 댓글 작성일시 표시

### 게시판 기능

- ✅ 게시글 목록 (페이징 처리)
- ✅ 게시글 조회 (조회수 증가)
- ✅ 게시글 작성 (로그인 필요)
- ✅ 게시글 수정 (본인만)
- ✅ 게시글 삭제 (본인만, 논리적 삭제)

### 댓글 기능

- ✅ 댓글 등록/조회/수정/삭제 (REST API)
- ✅ 댓글 페이징 처리 (페이지당 10개)
- ✅ 게시글별 댓글 목록 조회
- ✅ 댓글 작성자 정보 표시
- ✅ 댓글 작성일시 표시

### 회원 기능

- ✅ 회원가입 (BCrypt 비밀번호 암호화)
- ✅ 로그인/로그아웃 (Spring Security)
- ✅ 아이디 중복 확인 (AJAX)
- ✅ Remember Me (로그인 상태 유지)

### 보안

- ✅ Spring Security 기반 인증/인가
- ✅ BCrypt 비밀번호 암호화
- ✅ 권한 기반 접근 제어 (MEMBER, ADMIN)
- ✅ 접근 거부 페이지 (403)

## URL 매핑

### 일반 페이지

| URL | 메서드 | 설명 | 권한 |
|-----|--------|------|------|
| `/` | GET | 홈페이지 | 전체 |
| `/member/login` | GET | 로그인 페이지 | 전체 |
| `/member/join` | GET/POST | 회원가입 | 전체 |
| `/member/idCheck` | GET | 아이디 중복확인 | 전체 |
| `/board/list` | GET | 게시글 목록 (페이징) | 전체 |
| `/board/view` | GET | 게시글 상세 | 전체 |
| `/board/write` | GET/POST | 게시글 작성 | 로그인 |
| `/board/update` | GET/POST | 게시글 수정 | 본인 |
| `/board/delete` | GET | 게시글 삭제 | 본인 |

### 댓글 REST API

| URL | 메서드 | 설명 | 권한 |
|-----|--------|------|------|
| `/replies` | POST | 댓글 등록 | 로그인 |
| `/replies/{bno}/list` | GET | 댓글 목록 조회 (페이징) | 전체 |
| `/replies/{rno}` | GET | 댓글 조회 | 전체 |
| `/replies/{rno}` | PUT | 댓글 수정 | 작성자 |
| `/replies/{rno}` | DELETE | 댓글 삭제 | 작성자 |

## 라이선스

MIT License
