# JSP 로그인 시스템 완벽 가이드 (중학생용) - Part 2

> Part 1에서 이어집니다

---

## 6. 회원정보 수정 과정

철수가 자신의 정보를 수정하는 과정을 따라가봅시다!

**수정할 내용**:
- 이메일: chulsoo@example.com → chulsoo_new@naver.com
- 전화번호: 010-1234-5678 → 010-9999-8888

### 📍 1단계: 수정 페이지 접속

**철수가 main.jsp에서 "회원정보변경" 버튼 클릭**:
```jsp
<button type="button" 
        onclick="location.href='<%=request.getContextPath()%>/memberUpdate.do?userid=${loginUser.userid}'">
    회원정보변경
</button>
```

**URL**: `/memberUpdate.do?userid=chulsoo`

---

### 📍 2단계: UpdateServlet doGet() 실행

**UpdateServlet doGet()**:
```java
protected void doGet(...) {
    // 1. 세션에서 로그인 사용자 정보 가져오기
    HttpSession session = request.getSession();
    MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
    
    // 2. 로그인 체크
    if(loginUser == null) {
        // 로그인 안 했으면 로그인 페이지로
        response.sendRedirect("/login.do");
        return;
    }
    
    // 3. URL 파라미터에서 userid 가져오기
    String userid = request.getParameter("userid");  // "chulsoo"
    
    // 4. 보안: 세션의 userid와 다르면 세션 것 사용
    if(!userid.equals(loginUser.getUserid())) {
        userid = loginUser.getUserid();
    }
    
    // 5. DB에서 최신 정보 가져오기 (세션은 오래된 정보일 수 있음)
    MemberDAO dao = MemberDAO.getInstance();
    MemberVO mvo = dao.getMember(userid);
    
    // 6. JSP로 전달
    request.setAttribute("mvo", mvo);
    request.getRequestDispatcher("/member/memberUpdate.jsp")
           .forward(request, response);
}
```

**보안 기능 설명**:
```
철수가 URL을 "memberUpdate.do?userid=younghee"로 바꾸면?
→ 서버: "어? 네 아이디는 chulsoo인데?"
→ userid를 "chulsoo"로 강제 변경
→ 남의 정보 못 봄!
```

---

### 📍 3단계: 기존 정보 표시

**memberUpdate.jsp**:
```jsp
<%
   MemberVO mvo = (MemberVO) request.getAttribute("mvo");
   
   String nameValue = mvo != null ? mvo.getName() : "";
   String useridValue = mvo != null ? mvo.getUserid() : "";
   String emailValue = mvo != null ? mvo.getEmail() : "";
   String phoneValue = mvo != null ? mvo.getPhone() : "";
%>

<input type="text" name="name" value="<%= nameValue %>" readonly>
<input type="text" value="<%= useridValue %>" readonly disabled>
<input type="text" name="email" value="<%= emailValue %>">
<input type="text" name="phone" value="<%= phoneValue %>">
```

**화면**:
```
┌────────────────────────────┐
│   회원 수정                 │
├────────────────────────────┤
│ 이름: [김철수        ] (변경 불가)
│ 아이디: [chulsoo      ] (변경 불가)
│ 암호: [           ]        │  ← 새 비밀번호
│ 암호 확인: [           ]    │
│ 이메일: [chulsoo@example.com]  ← 수정 가능
│ 전화번호: [010-1234-5678]   ← 수정 가능
│ 등급: (•) 일반회원 ( ) 관리자
│                            │
│    [확인] [취소]           │
└────────────────────────────┘
```

---

### 📍 4단계: 정보 수정

**철수가 수정**:
- 이메일: `chulsoo_new@naver.com` (변경)
- 전화번호: `010-9999-8888` (변경)
- 비밀번호: `1234` (그대로 유지)

---

### 📍 5단계: "확인" 버튼 클릭

**memberUpdate.jsp의 버튼**:
```jsp
<input type="submit" value="확인" onclick="return joinCheck()">
```

**JavaScript 검증**:
- 이름, 아이디, 비밀번호 등 체크
- 모두 OK → 폼 제출

---

### 📍 6단계: UpdateServlet doPost() 실행

**UpdateServlet doPost()**:
```java
protected void doPost(...) {
    // 1. 수정된 데이터 받기
    String userid = request.getParameter("userid");    // "chulsoo"
    String pwd = request.getParameter("pwd");          // "1234"
    String email = request.getParameter("email");      // "chulsoo_new@naver.com"
    String phone = request.getParameter("phone");      // "010-9999-8888"
    String adminStr = request.getParameter("admin");   // "0"
    
    // 2. admin 변환
    int admin = Integer.parseInt(adminStr);
    
    // 3. VO에 담기
    MemberVO mvo = new MemberVO();
    mvo.setUserid(userid);
    mvo.setPwd(pwd);
    mvo.setEmail(email);
    mvo.setPhone(phone);
    mvo.setAdmin(admin);
    
    // 4. DAO를 통해 DB 업데이트
    MemberDAO dao = MemberDAO.getInstance();
    int result = dao.updateMember(mvo);
    
    // 5. 성공하면 세션도 업데이트
    if(result == 1) {
        HttpSession session = request.getSession();
        MemberVO updatedMvo = dao.getMember(userid);
        session.setAttribute("loginUser", updatedMvo);
        
        request.setAttribute("message", "회원정보가 수정되었습니다.");
        url = "/member/main.jsp";
    } else {
        request.setAttribute("message", "수정 실패");
        url = "/member/memberUpdate.jsp";
    }
}
```

---

### 📍 7단계: 데이터베이스 업데이트

**MemberDAO의 updateMember() 메서드**:
```java
public int updateMember(MemberVO mvo) {
    try {
        Connection con = getConnection();
        
        // UPDATE 쿼리 (이름은 수정 안 함)
        String sql = "update member set pwd=?, email=?, phone=?, admin=? where userid=?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        
        // 값 설정
        pstmt.setString(1, mvo.getPwd());      // "1234"
        pstmt.setString(2, mvo.getEmail());    // "chulsoo_new@naver.com"
        pstmt.setString(3, mvo.getPhone());    // "010-9999-8888"
        pstmt.setInt(4, mvo.getAdmin());       // 0
        pstmt.setString(5, mvo.getUserid());   // "chulsoo" (WHERE 조건)
        
        // 쿼리 실행
        int result = pstmt.executeUpdate();
        
        return result;  // 1 (성공)
        
    } catch(Exception e) {
        return -1;  // 실패
    }
}
```

**SQL 쿼리 의미**:
```sql
UPDATE member 
SET email='chulsoo_new@naver.com', 
    phone='010-9999-8888' 
WHERE userid='chulsoo'
```
- "chulsoo" 행의 email과 phone을 새 값으로 변경하라

**데이터베이스 테이블 변화**:
```
변경 전:
┌────────┬────────┬──────────────────┬──────────────┐
│ userid │  pwd   │      email       │    phone     │
├────────┼────────┼──────────────────┼──────────────┤
│chulsoo │  1234  │chulsoo@example.com│010-1234-5678│
└────────┴────────┴──────────────────┴──────────────┘

변경 후:
┌────────┬────────┬────────────────────┬──────────────┐
│ userid │  pwd   │       email        │    phone     │
├────────┼────────┼────────────────────┼──────────────┤
│chulsoo │  1234  │chulsoo_new@naver.com│010-9999-8888│
└────────┴────────┴────────────────────┴──────────────┘
```

---

### 📍 8단계: 세션 업데이트

**왜 세션을 업데이트해야 하나요?**

```java
// 기존 세션:
session {
    loginUser: {
        email: "chulsoo@example.com",  ← 오래된 정보
        phone: "010-1234-5678"         ← 오래된 정보
    }
}

// 업데이트:
MemberVO updatedMvo = dao.getMember(userid);  // DB에서 최신 정보
session.setAttribute("loginUser", updatedMvo);

// 새 세션:
session {
    loginUser: {
        email: "chulsoo_new@naver.com",  ← 최신 정보
        phone: "010-9999-8888"           ← 최신 정보
    }
}
```

**업데이트 안 하면?**
- main.jsp에서 여전히 옛날 이메일/전화번호 표시됨
- 로그아웃하고 다시 로그인해야 최신 정보 보임

---

### 📍 9단계: 메인 페이지로 이동

**main.jsp 화면**:
```
┌─────────────────────────────┐
│   반갑습니다!               │
│   김철수님                  │
├─────────────────────────────┤
│ ID : chulsoo                │
│ Email : chulsoo_new@naver.com  ← 변경됨!
│ Phone : 010-9999-8888       ← 변경됨!
│ Admin : 일반회원            │
├─────────────────────────────┤
│ ✓ 회원정보가 수정되었습니다. │
│                             │
│ [로그아웃] [회원정보변경]   │
└─────────────────────────────┘
```

---

### 🎯 회원정보 수정 과정 요약

```
1. main.jsp에서 "회원정보변경" 버튼 클릭
   ↓
2. UpdateServlet doGet() 실행
   ↓
3. 로그인 체크 (세션 확인)
   ↓
4. DB에서 최신 정보 가져오기
   ↓
5. memberUpdate.jsp에 기존 정보 표시
   ↓
6. 사용자가 정보 수정
   ↓
7. "확인" 버튼 → joinCheck() 검증
   ↓
8. UpdateServlet doPost() 실행
   ↓
9. DAO가 DB 업데이트
   ↓
10. 세션도 업데이트
    ↓
11. main.jsp로 이동
    ↓
12. "수정 완료" 메시지 표시
```

---

### 💡 왜 이름과 아이디는 수정 못 하나요?

**이름을 수정 못 하는 이유**:
- 이름은 중요한 개인정보
- 함부로 바꾸면 안 됨
- 실명 확인이 필요할 수 있음

**아이디를 수정 못 하는 이유**:
- 아이디는 고유 식별자 (Primary Key)
- 아이디를 바꾸면 다른 테이블과의 관계가 깨질 수 있음
- 보안상 문제 발생 가능

**실제 웹사이트도 그래요**:
- 네이버: 아이디 변경 불가
- 인스타그램: 아이디 변경 가능 (하지만 복잡한 과정)
- 은행: 이름/주민번호 변경 불가

---

## 7. 로그아웃 과정

철수가 로그아웃하는 과정입니다!

### 📍 1단계: "로그아웃" 버튼 클릭

**main.jsp의 버튼**:
```jsp
<form method="post" action="<%=request.getContextPath()%>/logout.do">
    <button type="submit">로그아웃</button>
</form>
```

**POST 요청으로 `/logout.do`에 전송**

---

### 📍 2단계: LogoutServlet 실행

**LogoutServlet doPost()**:
```java
protected void doPost(...) {
    // doGet 호출 (GET이든 POST든 같은 처리)
    doGet(request, response);
}

protected void doGet(...) {
    // 1. 세션 가져오기
    HttpSession session = request.getSession();
    
    // 2. 세션 무효화 (모든 데이터 삭제)
    session.invalidate();
    
    // 3. 로그인 페이지로 리다이렉트
    response.sendRedirect(request.getContextPath() + "/login.do");
}
```

**세션 무효화 의미**:
```java
// 무효화 전:
session {
    userid: "chulsoo",
    loginUser: { ... }
}

// 무효화 후:
session = null  // 세션 자체가 사라짐
```

---

### 📍 3단계: 로그인 페이지로 이동

**결과**:
- 로그인 페이지(login.jsp) 표시
- 세션이 없으므로 main.jsp 접속 불가
- 다시 로그인해야 함

**비유**:
- 도서관에서 나올 때 출입증 반납
- 다시 들어가려면 새로 출입증을 받아야 함

---

### 🎯 로그아웃 과정 요약

```
1. "로그아웃" 버튼 클릭
   ↓
2. LogoutServlet 실행
   ↓
3. session.invalidate() → 세션 삭제
   ↓
4. login.do로 리다이렉트
   ↓
5. 로그인 페이지 표시
```

**매우 간단!**
- 가장 짧은 프로세스
- 하지만 중요한 보안 기능

---

### 💡 세션 무효화 vs 세션 속성 삭제

**방법 1: 세션 무효화** (우리가 사용하는 방법)
```java
session.invalidate();
```
- 세션 자체를 완전히 삭제
- 모든 데이터가 사라짐
- 로그아웃에 적합

**방법 2: 특정 속성만 삭제**
```java
session.removeAttribute("userid");
session.removeAttribute("loginUser");
```
- 세션은 유지되지만 특정 데이터만 삭제
- 다른 데이터는 남아있음

**어느 것이 더 좋나요?**
- 로그아웃은 방법 1이 더 안전
- 깔끔하게 모든 정보 삭제

---

## 8. 코드 한 줄씩 이해하기

### 📌 Java 코드 설명

#### 1. request.getParameter()
```java
String userid = request.getParameter("userid");
```

**의미**: 
- 폼에서 전송된 데이터 가져오기
- `name="userid"`인 input의 value

**HTML 예시**:
```html
<input type="text" name="userid" value="chulsoo">
<!-- 서버에서 request.getParameter("userid") → "chulsoo" -->
```

---

#### 2. request.setAttribute() / request.getAttribute()
```java
// 저장
request.setAttribute("message", "로그인 실패");

// 가져오기
String message = (String) request.getAttribute("message");
```

**의미**:
- request 객체에 데이터 임시 저장
- Servlet → JSP로 데이터 전달할 때 사용

**비유**:
- 편지 봉투에 메모 넣기
- JSP는 봉투를 열어서 메모 읽기

---

#### 3. session.setAttribute() / session.getAttribute()
```java
// 저장
session.setAttribute("userid", "chulsoo");

// 가져오기
String userid = (String) session.getAttribute("userid");
```

**의미**:
- 세션에 데이터 저장
- 여러 페이지에서 계속 사용 가능

**request vs session**:
- request: 한 번의 요청-응답 동안만 유지
- session: 로그아웃하거나 타임아웃될 때까지 유지

---

#### 4. RequestDispatcher.forward()
```java
RequestDispatcher dis = request.getRequestDispatcher("/member/main.jsp");
dis.forward(request, response);
```

**의미**:
- 서버 내부에서 다른 페이지로 이동
- **URL이 바뀌지 않음**

**예시**:
```
브라우저 주소창: http://localhost:8080/web-study-09/login.do
실제 보여지는 페이지: main.jsp
```

---

#### 5. response.sendRedirect()
```java
response.sendRedirect(request.getContextPath() + "/login.do");
```

**의미**:
- 브라우저에게 "이 URL로 다시 요청하세요" 지시
- **URL이 바뀜**

**예시**:
```
이전: http://localhost:8080/web-study-09/logout.do
이후: http://localhost:8080/web-study-09/login.do
```

---

#### 6. try-catch-finally
```java
try {
    // 시도할 코드
    Connection con = getConnection();
} catch(Exception e) {
    // 오류 발생 시 실행
    e.printStackTrace();
} finally {
    // 항상 실행 (오류 발생 여부와 관계없이)
    con.close();
}
```

**의미**:
- try: 정상적으로 실행
- catch: 오류 발생하면 여기로
- finally: 마지막에 반드시 실행 (리소스 정리)

**비유**:
- try: 시험 보기
- catch: 시험 망치면 대책 마련
- finally: 시험 끝나면 청소 (무조건)

---

#### 7. PreparedStatement
```java
String sql = "select * from member where userid = ?";
PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setString(1, userid);
ResultSet rs = pstmt.executeQuery();
```

**의미**:
- SQL 쿼리를 안전하게 실행
- `?`에 값을 안전하게 대입

**왜 안전한가요?**
```java
// 위험한 방법 (SQL 인젝션 취약)
String sql = "select * from member where userid = '" + userid + "'";

// 안전한 방법
String sql = "select * from member where userid = ?";
pstmt.setString(1, userid);
```

**SQL 인젝션 공격 예시**:
```
악의적인 입력: chulsoo' OR '1'='1
위험한 쿼리: select * from member where userid = 'chulsoo' OR '1'='1'
→ 모든 회원 정보 노출!

PreparedStatement 사용 시:
→ 'chulsoo\' OR \'1\'=\'1' (안전하게 처리됨)
```

---

### 📌 JSP 코드 설명

#### 1. JSP 선언부
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
```
- 이 페이지가 JSP이고, 한글을 사용한다고 선언

---

#### 2. Scriptlet
```jsp
<%
    String name = "철수";
    int age = 15;
%>
```
- JSP 안에 Java 코드 쓰기
- `<% %>` 사이에 Java 코드

---

#### 3. Expression
```jsp
<%= name %>
<%= age + 5 %>
```
- 변수 값을 화면에 출력
- `<%= %>` 사이에 변수나 수식

---

#### 4. EL (Expression Language)
```jsp
${loginUser.name}
${loginUser.email}
```
- Scriptlet보다 간결하게 데이터 출력
- `${객체.속성}` 형식

**Scriptlet vs EL 비교**:
```jsp
<!-- Scriptlet -->
<%
    MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
    String name = mvo.getName();
%>
<%= name %>

<!-- EL (훨씬 간단!) -->
${loginUser.name}
```

---

#### 5. JSTL (JSP Standard Tag Library)
```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty loginUser}">
    로그인해주세요
</c:if>

<c:forEach var="item" items="${list}">
    ${item}
</c:forEach>
```

**JSTL 태그 종류**:
- `<c:if>`: 조건문
- `<c:choose>`: switch문
- `<c:forEach>`: 반복문
- `<c:set>`: 변수 설정

---

#### 6. request.getContextPath()
```jsp
<a href="<%=request.getContextPath()%>/login.do">로그인</a>
```

**의미**:
- 프로젝트의 경로 가져오기
- 예: `/web-study-09`

**왜 필요한가요?**
```jsp
<!-- 상대 경로 (위험) -->
<a href="login.do">로그인</a>
<!-- /web-study-09/member/login.do로 이동 (오류!) -->

<!-- 절대 경로 (안전) -->
<a href="<%=request.getContextPath()%>/login.do">로그인</a>
<!-- /web-study-09/login.do로 이동 (정확!) -->
```

---

### 📌 JavaScript 코드 설명

#### 1. document.폼이름.필드이름
```javascript
document.frm.userid.value
```
- 폼에서 특정 필드의 값 가져오기

**HTML**:
```html
<form name="frm">
    <input type="text" name="userid">
</form>
```

---

#### 2. alert()
```javascript
alert("아이디를 입력해주세요");
```
- 경고 창 띄우기
- 사용자에게 메시지 전달

---

#### 3. return false / return true
```javascript
function loginCheck() {
    if(조건) {
        return false;  // 폼 제출 중단
    }
    return true;  // 폼 제출 진행
}
```

**버튼에서 사용**:
```html
<input type="submit" onclick="return loginCheck()">
```

---

#### 4. window.open()
```javascript
window.open(url, name, options);
```
- 새 창(팝업) 열기

**예시**:
```javascript
window.open("idCheck.do?userid=chulsoo", "popup", "width=450,height=200");
```

---

#### 5. window.opener
```javascript
window.opener.document.frm.userid.value = "chulsoo";
```
- 팝업에서 부모 창에 접근
- `opener`: 이 팝업을 연 부모 창

---

#### 6. location.href
```javascript
location.href = "/web-study-09/login.do";
```
- 현재 페이지를 다른 페이지로 이동

---

### 🎯 핵심 요약

**Java**:
- `request`: 한 번의 요청-응답
- `session`: 로그인 상태 유지
- `forward`: 서버 내부 이동 (URL 안 바뀜)
- `redirect`: 브라우저 이동 (URL 바뀜)
- `PreparedStatement`: 안전한 SQL

**JSP**:
- `<% %>`: Java 코드
- `<%= %>`: 값 출력
- `${}`: EL로 간단히 출력
- `<c:if>`: JSTL 조건문

**JavaScript**:
- `document.frm`: 폼 접근
- `return false`: 폼 제출 중단
- `window.open`: 팝업 열기
- `window.opener`: 부모 창 접근

---

## 9. 프로그래밍 용어 사전

### A

**admin** (관리자)
- 시스템을 관리하는 사용자
- 일반 사용자보다 많은 권한
- 이 프로젝트에서는 0(일반) 또는 1(관리자)

**attribute** (속성)
- 객체가 가지는 특성
- 예: 회원의 이름, 아이디, 이메일 등

---

### C

**Connection** (연결)
- 데이터베이스와의 연결
- 전화선처럼 연결해야 통신 가능

**Controller** (컨트롤러)
- MVC 패턴의 C
- 요청을 받아 처리하는 부분
- 이 프로젝트에서는 Servlet

---

### D

**DAO (Data Access Object)**
- 데이터베이스 접근 전문 객체
- DB 작업만 담당

**Database** (데이터베이스)
- 데이터를 저장하는 곳
- 엑셀 파일처럼 표 형태

---

### E

**EL (Expression Language)**
- JSP에서 간단히 데이터 표시
- `${변수}` 형식

**Exception** (예외/오류)
- 프로그램 실행 중 발생하는 오류
- try-catch로 처리

---

### F

**forward** (포워딩)
- 서버 내부에서 페이지 이동
- URL 바뀌지 않음

---

### G

**GET** (가져오기)
- 데이터를 가져오는 HTTP 메서드
- 주소창에 보임
- 예: `login.do?userid=chulsoo`

**getter** (겟터)
- 객체의 속성 값을 가져오는 메서드
- 예: `getName()`

---

### H

**HttpSession** (세션)
- 로그인 상태 등을 저장하는 공간
- 임시 출입증 역할

---

### J

**JSP (JavaServer Pages)**
- Java를 사용하는 동적 웹 페이지
- HTML + Java 코드

**JSTL (JSP Standard Tag Library)**
- JSP에서 사용하는 태그 라이브러리
- 조건문, 반복문 등을 태그로

---

### M

**MVC (Model-View-Controller)**
- 프로그램 구조 패턴
- Model: 데이터 (DAO, VO)
- View: 화면 (JSP)
- Controller: 제어 (Servlet)

---

### N

**NPE (NullPointerException)**
- null 객체를 사용하려 할 때 발생
- 가장 흔한 오류

```java
MemberVO mvo = null;
mvo.getName();  // NPE 발생!
```

---

### P

**POST** (제출하기)
- 데이터를 전송하는 HTTP 메서드
- 주소창에 안 보임 (보안)
- 로그인, 회원가입 등에 사용

**PreparedStatement** (준비된 구문)
- 안전한 SQL 실행
- SQL 인젝션 방지

**Primary Key** (기본 키)
- 테이블에서 각 행을 구분하는 고유 값
- 이 프로젝트에서는 userid

---

### R

**redirect** (리다이렉트)
- 브라우저에게 다른 페이지로 가라고 지시
- URL 바뀜

**request** (요청)
- 브라우저가 서버에 보내는 요청
- 데이터 전달 가능

**response** (응답)
- 서버가 브라우저에 보내는 응답
- 페이지, 데이터 등 전달

**ResultSet** (결과 집합)
- SQL 쿼리 실행 결과
- 표 형태의 데이터

---

### S

**Servlet** (서블릿)
- Java로 만든 웹 프로그램
- 요청 처리 담당

**Session** (세션)
- 로그인 상태 유지
- 서버에 저장됨

**setter** (셋터)
- 객체의 속성 값을 설정하는 메서드
- 예: `setName()`

**SQL (Structured Query Language)**
- 데이터베이스 언어
- SELECT, INSERT, UPDATE, DELETE 등

**Singleton** (싱글톤)
- 객체를 하나만 만드는 패턴
- DAO에 사용

---

### T

**try-catch**
- 오류 처리 구문
- try에서 오류 발생하면 catch 실행

---

### V

**VO (Value Object)**
- 데이터를 담는 객체
- 택배 상자 역할

---

### 🎯 핵심 요약

**구조**:
- MVC: Model(데이터), View(화면), Controller(제어)
- DAO: 데이터베이스 전문가
- VO: 데이터 상자
- Servlet: 요청 처리
- JSP: 화면 표시

**데이터**:
- request: 한 번의 요청
- session: 여러 요청에 걸쳐 유지
- attribute: 데이터 저장/전달

**이동**:
- forward: 서버 내부 이동
- redirect: 브라우저에게 이동 지시

---

## 10. 자주 묻는 질문 (Q&A)

### Q1. 왜 이렇게 복잡하게 만들어야 하나요?

**A**: 간단한 프로그램은 한 파일에 다 넣을 수 있어요. 하지만 프로그램이 커지면:
- 어디를 수정해야 할지 찾기 어려움
- 여러 사람이 작업하기 어려움
- 오류 발생 시 찾기 어려움

**MVC 패턴 장점**:
- 역할이 명확하게 나뉨
- 수정이 쉬움 (예: 디자인만 바꾸려면 JSP만 수정)
- 여러 사람이 동시 작업 가능

---

### Q2. Session은 언제 사라지나요?

**A**: 다음 경우에 사라져요:
1. 로그아웃 버튼 클릭 (`session.invalidate()`)
2. 일정 시간 동안 활동 없음 (기본 30분)
3. 브라우저 완전히 종료 (탭만 닫으면 안 사라짐)
4. 서버 재시작

---

### Q3. forward와 redirect 차이가 뭔가요?

**A**: 

**forward (내부 이동)**:
```
사용자: "login.do 페이지 보여주세요"
서버: (내부적으로 main.jsp로 이동)
      "여기 main.jsp 내용이에요"
사용자 주소창: login.do (그대로)
```

**redirect (외부 이동)**:
```
사용자: "logout.do 실행해주세요"
서버: "login.do로 다시 요청하세요"
사용자: (login.do로 새 요청)
사용자 주소창: login.do (바뀜)
```

**언제 뭘 쓰나요?**
- forward: 같은 요청 안에서 이동 (데이터 전달)
- redirect: 완전히 새로운 요청 (로그아웃, 가입 후)

---

### Q4. 비밀번호를 그대로 저장해도 되나요?

**A**: **절대 안 됩니다!**

이 프로젝트는 학습용이라 그대로 저장하지만, 실제로는:

```java
// 회원가입 시
String plainPwd = "1234";
String hashedPwd = BCrypt.hashpw(plainPwd);  // "암호화된값"
// DB에 암호화된 값 저장

// 로그인 시
String inputPwd = "1234";  // 사용자 입력
String dbHashedPwd = "암호화된값";  // DB에서 가져온 값
boolean isMatch = BCrypt.checkpw(inputPwd, dbHashedPwd);  // 비교
```

---

### Q5. SQL 인젝션이 뭔가요?

**A**: 악의적인 SQL 명령을 주입하는 공격:

```java
// 위험한 코드
String sql = "SELECT * FROM member WHERE userid = '" + userid + "'";

// 악의적인 입력
userid = "admin' OR '1'='1"

// 실제 실행되는 SQL
SELECT * FROM member WHERE userid = 'admin' OR '1'='1'
// → 모든 회원 정보 노출!
```

**PreparedStatement로 방지**:
```java
String sql = "SELECT * FROM member WHERE userid = ?";
pstmt.setString(1, "admin' OR '1'='1");
// → 'admin\' OR \'1\'=\'1\'' (안전하게 처리)
```

---

### Q6. 왜 DAO는 싱글톤으로 만드나요?

**A**: 

**싱글톤 아니면**:
```java
// 요청 1
MemberDAO dao1 = new MemberDAO();  // 객체 생성

// 요청 2
MemberDAO dao2 = new MemberDAO();  // 또 생성

// 요청 3
MemberDAO dao3 = new MemberDAO();  // 또또 생성
// → 메모리 낭비!
```

**싱글톤이면**:
```java
// 요청 1
MemberDAO dao = MemberDAO.getInstance();  // 객체 생성

// 요청 2
MemberDAO dao = MemberDAO.getInstance();  // 기존 객체 재사용

// 요청 3
MemberDAO dao = MemberDAO.getInstance();  // 기존 객체 재사용
// → 메모리 절약!
```

---

### Q7. 왜 아이디 중복 체크를 팝업으로 하나요?

**A**: 

**팝업 없이 하면**:
- 중복 체크할 때마다 페이지 새로고침
- 입력한 다른 정보들이 사라짐
- 사용자 불편

**팝업으로 하면**:
- 회원가입 페이지는 그대로 유지
- 입력한 정보 안 사라짐
- 사용자 편리

---

### Q8. GET과 POST 차이가 뭔가요?

**A**:

**GET**:
```
URL: http://localhost/idCheck.do?userid=chulsoo
특징:
- 주소창에 데이터 보임
- 북마크 가능
- 페이지 요청에 적합
```

**POST**:
```
URL: http://localhost/login.do
데이터: (내부에 숨겨져서 전송)
특징:
- 주소창에 안 보임
- 북마크 불가
- 데이터 전송에 적합
```

**언제 뭘 쓰나요?**
- GET: 검색, 페이지 이동
- POST: 로그인, 회원가입, 수정

---

### Q9. 왜 비밀번호는 저장 안 하고 매번 입력하나요?

**A**: 

**보안을 위해서입니다!**

**비밀번호 저장하면**:
- 누군가 컴퓨터를 사용하면 바로 로그인됨
- 계정 도용 위험

**아이디만 저장하면**:
- 로그인 실패 시 편의성 (아이디 다시 입력 안 해도 됨)
- 비밀번호는 매번 입력 (보안)

---

### Q10. 왜 이름은 수정 못 하나요?

**A**: 

**보안과 신뢰성 때문입니다**:
- 이름은 중요한 개인정보
- 실명 확인이 필요할 수 있음
- 함부로 바꾸면 안 됨

**실제 웹사이트도 그래요**:
- 은행: 이름 변경 불가
- 카카오: 이름 변경 시 실명 인증 필요
- 네이버: 이름 변경 제한

---

### 🎯 마무리

이 프로그램을 이해했다면:
1. **웹 프로그래밍의 기본** 이해 ✓
2. **데이터베이스 연동** 이해 ✓
3. **보안의 중요성** 이해 ✓
4. **MVC 패턴** 이해 ✓

다음 단계:
- 게시판 만들기
- 파일 업로드
- 이메일 인증
- 비밀번호 찾기

**계속 공부하세요! 여러분은 이미 웹 개발자의 길을 걷고 있습니다! 🚀**

---

**끝!** 🎉

