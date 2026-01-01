<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시글 수정 - 대철이제철 게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h2>게시글 수정</h2>
        
        <form action="${pageContext.request.contextPath}/board/update" method="post">
            <input type="hidden" name="seq" value="${board.seq}">
            
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" value="${board.title}" required>
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" rows="15" required>${board.content}</textarea>
            </div>
            
            <div class="form-group">
                <button type="submit">수정</button>
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
