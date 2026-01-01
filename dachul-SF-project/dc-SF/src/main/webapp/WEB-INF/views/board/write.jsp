<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 작성 - 대철이제철 게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h2>게시글 작성</h2>
        
        <form action="${pageContext.request.contextPath}/board/write" method="post">
            <sec:authorize access="isAuthenticated()">
                <sec:authentication property="principal.username" var="currentUser"/>
                <input type="hidden" name="writer" value="${currentUser}">
            </sec:authorize>
            
            <div class="form-group">
                <label>작성자</label>
                <span class="read-only"><sec:authentication property="principal.name"/></span>
            </div>
            
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" placeholder="제목을 입력하세요" required autofocus>
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" rows="15" placeholder="내용을 입력하세요" required></textarea>
            </div>
            
            <div class="form-group">
                <button type="submit">등록</button>
                <button type="button" onclick="history.back()">취소</button>
            </div>
        </form>
        
        <div class="links">
            <a href="${pageContext.request.contextPath}/board/list">목록으로</a>
            <sec:authorize access="hasRole('ADMIN')">
                <a href="${pageContext.request.contextPath}/admin/main">관리자</a>
            </sec:authorize>
        </div>
    </div>
</body>
</html>
