# SP1 프로젝트 전체 흐름 점검 보고서

## 📋 프로젝트 구조

### 1. 계층 구조 (Layered Architecture)
```
Controller Layer (org.zerock.controller)
    ↓
Service Layer (org.zerock.service)
    ↓
Mapper Layer (org.zerock.mapper)
    ↓
Database (MySQL)
```

### 2. 주요 컴포넌트

#### **Controller 계층**
- `BoardController`: 게시판 CRUD 처리 (`/board/*`)
- `ReplyController`: 댓글 REST API (`/replies/*`)
- `HelloController`, `HelloController2`: 샘플 컨트롤러

#### **Service 계층**
- `BoardService`: 게시판 비즈니스 로직
- `ReplyService`: 댓글 비즈니스 로직
- `ReplyException`: 댓글 예외 처리

#### **Mapper 계층**
- `BoardMapper`: 게시판 데이터 접근
- `ReplyMapper`: 댓글 데이터 접근
- MyBatis XML 매퍼 사용

## 🔄 요청 처리 흐름

### 게시판 목록 조회
```
1. 브라우저: GET /board/list?page=1&size=10
2. DispatcherServlet: 요청 수신
3. BoardController.list() 호출
4. BoardService.getList() 호출
5. BoardMapper.listSearch() 실행
6. 결과를 BoardListPaginDTO로 래핑
7. Model에 "dto"로 저장
8. ViewResolver: /WEB-INF/views/board/list.jsp 렌더링
```

### 게시글 조회
```
1. 브라우저: GET /board/read/{bno}
2. BoardController.read() 호출
3. BoardService.read() 호출
4. BoardMapper.selectOne() 실행
5. Model에 "board"로 저장
6. ViewResolver: /WEB-INF/views/board/read.jsp 렌더링
```

### 댓글 등록 (REST API)
```
1. 브라우저: POST /replies (JSON 데이터)
2. DispatcherServlet: 요청 수신
3. ReplyController.add() 호출 (@RequestBody로 JSON 파싱)
4. ReplyService.add() 호출
5. ReplyMapper.insert() 실행
6. ResponseEntity로 JSON 응답 반환
```

### 댓글 목록 조회 (REST API)
```
1. 브라우저: GET /replies/{bno}/list?page=1&size=10
2. ReplyController.listOfBoard() 호출
3. ReplyService.listOfBoard() 호출
4. ReplyMapper.listOfBoard() 실행
5. ReplyListPaginDTO로 래핑
6. ResponseEntity로 JSON 응답 반환
```

## ⚙️ 설정 파일

### web.xml
- DispatcherServlet 설정
- ContextLoaderListener 설정
- root-context.xml 로드 (서비스, 매퍼)
- servlet-context.xml 로드 (컨트롤러)

### servlet-context.xml
- Spring MVC 활성화 (`<mvc:annotation-driven/>`)
- ViewResolver 설정 (JSP 경로)
- Controller 스캔 (`org.zerock.controller`)
- 정적 리소스 매핑 (`/resources/**`)

### root-context.xml
- Service 스캔 (`org.zerock.service`)
- HikariCP 데이터소스 설정
- MyBatis SqlSessionFactory 설정
- Mapper 스캔 (`org.zerock.mapper`)

### mybatis-config.xml
- DTO 타입 별칭 설정 (`org.zerock.dto`)

## 🐛 발견된 문제점

### 1. **Critical: read.jsp 페이징 코드 오타**
- **위치**: `read.jsp` 288번 줄
- **문제**: `pagginStr` 변수명 오타 (정확한 변수명: `pageStr`)
- **영향**: 페이징 "이전" 버튼이 작동하지 않음
- **수정 필요**: ✅

### 2. **Critical: 댓글 목록 쿼리에서 delflag 체크 누락**
- **위치**: `ReplyMapper.xml` 35-40번 줄
- **문제**: 삭제된 댓글(delflag=true)도 조회됨
- **영향**: 삭제된 댓글이 화면에 표시될 수 있음
- **수정 필요**: ✅

### 3. **Medium: 템플릿 리터럴 백슬래시 문제**
- **위치**: `read.jsp` 267, 270, 273, 277번 줄
- **문제**: `\${replyDTO.rno}` 형태로 백슬래시가 있어서 변수 치환이 안 됨
- **영향**: 댓글 목록이 제대로 표시되지 않음
- **상태**: 사용자가 되돌림 (의도적일 수 있음)

### 4. **Low: BoardController 로깅 오류**
- **위치**: `BoardController.java` 50-51번 줄
- **문제**: `types`와 `keyword`가 반대로 출력됨
- **영향**: 로그만 잘못 출력 (기능에는 영향 없음)
- **수정 필요**: ⚠️ (선택사항)

### 5. **Low: 페이징 링크 클릭 이벤트 미구현**
- **위치**: `read.jsp` 페이징 부분
- **문제**: `<a>` 태그의 `href`에 숫자만 있어서 제대로 작동하지 않을 수 있음
- **영향**: 페이징 버튼 클릭 시 페이지 이동이 안 될 수 있음
- **수정 필요**: ⚠️ (선택사항)

## ✅ 정상 작동하는 부분

1. ✅ Spring MVC 설정이 올바르게 구성됨
2. ✅ MyBatis 매퍼 설정 정상
3. ✅ 데이터소스 연결 설정 정상
4. ✅ 의존성 주입(DI) 정상 작동
5. ✅ REST API 구조 정상
6. ✅ 예외 처리 구조 정상

## 📝 권장 수정 사항

### 즉시 수정 필요
1. `read.jsp` 288번 줄: `pagginStr` → `pageStr`
2. `ReplyMapper.xml`: `listOfBoard`와 `countOfBoard`에 `delflag = false` 조건 추가

### 개선 권장
1. 댓글 목록 조회 시 삭제된 댓글 제외
2. 페이징 링크 클릭 이벤트 구현
3. BoardController 로깅 수정

## 🔍 데이터베이스 스키마 확인 필요

- `tbl_reply` 테이블의 실제 컬럼명 확인
  - `replyText` vs `replytext` (대소문자)
  - `replyDate` vs `replydate` (MyBatis 자동 매핑 확인)

