# Module Context

JSP View Layer. 사용자 인터페이스 렌더링.

## Role & Responsibilities

- HTML 마크업 생성
- 동적 데이터 표시 (JSTL)
- 폼 처리
- Spring Security 태그 활용
- 정적 리소스 포함 (CSS, JS, Images)

## Dependencies

- JSP 3.1
- JSTL 3.0 (jakarta.tags.core, jakarta.tags.fmt)
- Spring Security Taglib
- Static Resources (CSS, JS)
- Axios (REST API 통신)
- Bootstrap 5.3.0 (UI 컴포넌트)

# Tech Stack & Constraints

## View Resolver Configuration

servlet-context.xml에 설정:
```xml
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/" />
    <property name="suffix" value=".jsp" />
</bean>
```

Controller에서 "member/list" 반환 시 -> /WEB-INF/views/member/list.jsp

## Directory Structure

```
views/
├── common/          # 공통 페이지 (header, footer, nav)
├── member/          # 회원 관련 페이지
├── board/           # 게시판 관련 페이지
├── admin/           # 관리자 페이지
└── error/           # 에러 페이지 (403, 404, 500)
```

# Implementation Patterns

## Basic JSP Structure

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>페이지 제목</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
</head>
<body>
    <%@ include file="../common/header.jsp" %>

    <div class="container">
        <h1>페이지 제목</h1>

        <!-- 내용 -->

    </div>

    <%@ include file="../common/footer.jsp" %>

    <script src="<c:url value='/resources/js/common.js' />"></script>
</body>
</html>
```

## JSTL Core Tags

### Variable & Output

```jsp
<!-- 변수 설정 -->
<c:set var="message" value="Hello World" />

<!-- 출력 -->
<c:out value="${message}" />

<!-- XSS 방지를 위해 escapeXml="true" 기본값 -->
<c:out value="${userInput}" escapeXml="true" />
```

### Conditional

```jsp
<!-- if -->
<c:if test="${not empty list}">
    <p>리스트가 비어있지 않습니다.</p>
</c:if>

<!-- choose/when/otherwise (switch-case와 유사) -->
<c:choose>
    <c:when test="${member.role == 'ADMIN'}">
        <p>관리자입니다.</p>
    </c:when>
    <c:when test="${member.role == 'USER'}">
        <p>일반 사용자입니다.</p>
    </c:when>
    <c:otherwise>
        <p>권한이 없습니다.</p>
    </c:otherwise>
</c:choose>
```

### Iteration

```jsp
<!-- forEach -->
<c:forEach items="${list}" var="item" varStatus="status">
    <div>
        <p>인덱스: ${status.index}, 순번: ${status.count}</p>
        <p>이름: ${item.name}</p>
        <p>설명: ${item.description}</p>
    </div>
</c:forEach>

<!-- 범위 반복 -->
<c:forEach begin="1" end="10" var="i">
    <span>${i}</span>
</c:forEach>
```

### URL

```jsp
<!-- 컨텍스트 경로 자동 포함 -->
<a href="<c:url value='/member/list' />">회원 목록</a>

<!-- 파라미터 포함 -->
<c:url value="/board/read" var="readUrl">
    <c:param name="id" value="${board.id}" />
</c:url>
<a href="${readUrl}">게시글 보기</a>
```

## JSTL Formatting Tags

```jsp
<!-- 날짜 포맷 -->
<fmt:formatDate value="${member.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" />

<!-- 숫자 포맷 -->
<fmt:formatNumber value="${product.price}" pattern="#,###" />

<!-- 통화 포맷 -->
<fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₩" />
```

## Spring Security Tags

```jsp
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!-- 인증 여부 확인 -->
<sec:authorize access="isAuthenticated()">
    <p>로그인된 사용자입니다.</p>
</sec:authorize>

<sec:authorize access="isAnonymous()">
    <a href="/login">로그인</a>
</sec:authorize>

<!-- 권한별 표시 -->
<sec:authorize access="hasRole('ADMIN')">
    <a href="/admin/dashboard">관리자 페이지</a>
</sec:authorize>

<sec:authorize access="hasAnyRole('USER', 'ADMIN')">
    <a href="/member/mypage">마이페이지</a>
</sec:authorize>

<!-- 인증 정보 출력 -->
<sec:authentication property="principal.username" var="username" />
<p>환영합니다, ${username}님!</p>

<!-- CSRF 토큰 -->
<form method="post" action="/member/register">
    <sec:csrfInput/>
    <!-- 또는 -->
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    ...
</form>
```

## Form Handling

```jsp
<!-- 등록 폼 -->
<form method="post" action="<c:url value='/member/register' />">
    <sec:csrfInput/>

    <div>
        <label for="username">사용자명:</label>
        <input type="text" id="username" name="username" required>
    </div>

    <div>
        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required>
    </div>

    <div>
        <label for="email">이메일:</label>
        <input type="email" id="email" name="email" required>
    </div>

    <button type="submit">등록</button>
</form>

<!-- 수정 폼 -->
<form method="post" action="<c:url value='/member/modify' />">
    <sec:csrfInput/>

    <input type="hidden" name="id" value="${member.id}">

    <div>
        <label for="username">사용자명:</label>
        <input type="text" id="username" name="username" value="${member.username}" required>
    </div>

    <button type="submit">수정</button>
</form>

<!-- 삭제 폼 -->
<form method="post" action="<c:url value='/member/remove' />">
    <sec:csrfInput/>
    <input type="hidden" name="id" value="${member.id}">
    <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
</form>
```

## Flash Message Display

```jsp
<!-- RedirectAttributes로 전달된 일회성 메시지 표시 -->
<c:if test="${not empty message}">
    <div class="alert alert-success">
        ${message}
    </div>
</c:if>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">
        ${errorMessage}
    </div>
</c:if>
```

## List Display with Pagination

```jsp
<!-- 게시글 목록 -->
<div class="board-list">
    <c:forEach var="board" items="${dto.boardDTOList}">
        <div class="board-item">
            <div class="board-title">
                <a href="<c:url value='/board/view?seq=${board.seq}' />">
                    ${board.title}
                </a>
            </div>
            <div class="board-meta">
                <span class="board-writer">${board.writer}</span>
                <span class="board-separator">|</span>
                <span class="board-date">
                    <fmt:formatDate value="${board.regdate}" pattern="yyyy.MM.dd HH:mm"/>
                </span>
                <span class="board-separator">|</span>
                <span class="board-hit">조회 ${board.hit}</span>
            </div>
        </div>
    </c:forEach>
    
    <c:if test="${empty dto.boardDTOList}">
        <div class="board-empty">등록된 게시글이 없습니다.</div>
    </c:if>
</div>

<!-- 페이징 처리 -->
<div class="pagination-wrapper">
    <ul class="pagination justify-content-center">
        <c:if test="${dto.prev}">
            <li class="page-item">
                <a class="page-link" href="${dto.start - 1}" tabindex="-1">이전</a>
            </li>
        </c:if>
        
        <c:forEach var="num" items="${dto.pageNums}">
            <li class="page-item ${dto.page == num ? 'active' : ''}">
                <a class="page-link" href="${num}">${num}</a>
            </li>
        </c:forEach>
        
        <c:if test="${dto.next}">
            <li class="page-item">
                <a class="page-link" href="${dto.end + 1}">다음</a>
            </li>
        </c:if>
    </ul>
</div>

<script type="text/javascript">
    const pagingDiv = document.querySelector(".pagination");
    if(pagingDiv) {
        pagingDiv.addEventListener("click", (e) => {
            e.preventDefault();
            e.stopPropagation();
            
            const target = e.target;
            const targetPage = target.getAttribute("href");
            const size = ${dto.size} || 10;
            
            if(targetPage) {
                const params = new URLSearchParams({
                    page: targetPage,
                    size: size
                });
                
                self.location = '<c:url value="/board/list" />?' + params.toString();
            }
        }, false);
    }
</script>
```

## REST API Integration (Axios)

```jsp
<!-- Axios 라이브러리 포함 -->
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<script type="text/javascript">
    // 변수 선언 (최상단)
    const contextPath = '<c:out value="${pageContext.request.contextPath}"/>';
    const bno = parseInt(<c:out value="${board.seq}"/>);
    
    // REST API 호출 예제: 댓글 목록 조회
    function getReplies(pageNum, goLast) {
        axios.get(contextPath + '/replies/' + bno + '/list', {
            params: {
                page: pageNum || currentPage,
                size: currentSize
            }
        })
        .then(res => {
            const data = res.data;
            printReplies(data);
        })
        .catch(err => {
            console.error("댓글 목록 조회 실패:", err);
            alert("댓글을 불러올 수 없습니다.");
        });
    }
    
    // REST API 호출 예제: 댓글 등록
    function addReply(replyData) {
        axios.post(contextPath + '/replies', JSON.stringify(replyData), {
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(res => {
            alert("댓글이 등록되었습니다.");
            getReplies(1, true);
        })
        .catch(err => {
            console.error("댓글 등록 실패:", err);
            const errorMsg = err.response?.data || "댓글 등록에 실패했습니다.";
            alert("오류: " + errorMsg);
        });
    }
</script>
```

# Local Golden Rules

## Security Best Practices

### Do's
- 사용자 입력은 <c:out escapeXml="true"> 사용
- CSRF 토큰 항상 포함
- Spring Security 태그로 권한 제어
- XSS 방지

### Don'ts
- ${userInput} 직접 출력 금지 (XSS 위험)
- JavaScript에 서버 데이터 직접 삽입 시 주의
- 민감 정보 (비밀번호 등) 표시 금지
- 이모티콘 사용 금지 (UI 일관성 유지)
- JavaScript 변수 선언 순서 주의 (사용 전에 선언)

```jsp
<!-- BAD - XSS 위험 -->
<p>${userInput}</p>

<!-- GOOD -->
<p><c:out value="${userInput}" /></p>
```

## Static Resources

### Do's
- <c:url value='/resources/...'> 사용
- 컨텍스트 경로 자동 포함
- 버전 관리 (캐시 제어)

### Don'ts
- 절대 경로 하드코딩 금지
- /resources/... 직접 사용 지양

```jsp
<!-- GOOD -->
<link rel="stylesheet" href="<c:url value='/resources/css/style.css' />">
<script src="<c:url value='/resources/js/common.js' />"></script>

<!-- BAD -->
<link rel="stylesheet" href="/resources/css/style.css">
```

## Performance

### Do's
- include 지시자로 공통 파일 재사용
- JSTL 적극 활용
- 불필요한 스크립틀릿 제거
- REST API 호출 시 Axios 사용
- JavaScript 변수는 스크립트 최상단에 선언
- EL 표현식은 <c:out> 태그로 안전하게 출력

### Don'ts
- 스크립틀릿 (<% %>) 사용 금지
- JSP에서 비즈니스 로직 작성 금지
- 무거운 연산 금지

```jsp
<!-- BAD - 스크립틀릿 사용 -->
<% String username = (String) request.getAttribute("username"); %>
<p><%= username %></p>

<!-- GOOD - EL 사용 -->
<p>${username}</p>
```

## Accessibility & SEO

### Do's
- 시맨틱 HTML 사용
- alt 속성 추가
- label 태그 사용
- 적절한 heading 구조

### Don'ts
- div 남용 금지
- 인라인 스타일 지양

# Testing Strategy

## Manual Testing

1. 브라우저에서 페이지 접속
2. 폼 입력 및 제출
3. 권한별 접근 확인
4. 에러 메시지 표시 확인
5. 다양한 브라우저 테스트

## Validation

- HTML 유효성 검사 (W3C Validator)
- 접근성 검사 (WAVE, Lighthouse)
- 반응형 디자인 확인

# Related Contexts

- Controller Layer: ../../../java/org/zerock/controller/AGENTS.md
- Spring Security: ../../../java/org/zerock/security/AGENTS.md
- Static Resources: ../../resources/
