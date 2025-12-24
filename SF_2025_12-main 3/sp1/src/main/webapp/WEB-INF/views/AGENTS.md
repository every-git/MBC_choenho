# View Layer Context (JSP)

## Module Context

View 계층은 JSP를 사용하여 사용자 인터페이스를 렌더링합니다. Spring MVC의 ViewResolver가 JSP를 찾아 렌더링합니다.

**의존성 관계:**
- JSP -> Controller (Model 데이터 수신)
- JSP -> JSTL, EL (표현식)
- JSP -> Bootstrap 5.3.3 (스타일링)
- JSP -> Axios (REST API 호출)

## Tech Stack & Constraints

- Jakarta Servlet JSP 3.1.1
- JSTL 3.0.0
- Bootstrap 5.3.3
- Axios (CDN)
- JavaScript (ES6+)

**제약사항:**
- JSP는 `/WEB-INF/views/` 디렉토리에만 위치
- 스크립트릿(`<% %>`) 사용 지양, JSTL과 EL 사용
- `isELIgnored="false"` 명시 필수

## Implementation Patterns

**JSP 기본 구조:**
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="container">
  <c:forEach var="item" items="${list}">
    <div><c:out value="${item.name}" /></div>
  </c:forEach>
</div>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
```

**REST API 호출 패턴 (Axios):**
```javascript
axios.post("/replies", JSON.stringify(data), {
  headers: { 'Content-Type': 'application/json' }
}).then(res => {
  console.log("성공:", res.data);
}).catch(err => {
  console.error("실패:", err);
});
```

**파일 네이밍:**
- 페이지: `{entity}/{action}.jsp` (예: `board/list.jsp`, `board/read.jsp`)
- 공통: `includes/{name}.jsp` (예: `includes/header.jsp`)

## Testing Strategy

JSP는 브라우저에서 직접 테스트:
- 서버 실행 후 실제 페이지 접속하여 확인
- 브라우저 개발자 도구로 JavaScript 오류 확인
- 네트워크 탭에서 API 호출 확인

## Local Golden Rules

**Do's:**
- EL 표현식 사용: `${variable}`
- JSTL `<c:out>` 사용하여 XSS 방지
- JavaScript는 `<script>` 태그 내부에 작성
- Axios로 REST API 호출 시 JSON.stringify() 사용
- Bootstrap 클래스 활용하여 스타일링

**Don'ts:**
- 스크립트릿(`<% %>`) 사용 지양
- 인라인 스타일 대신 Bootstrap 클래스 사용
- 하드코딩된 URL 대신 `<c:url>` 태그 사용
- JavaScript에서 전역 변수 남발하지 말 것
- `isELIgnored="false"` 누락 금지

