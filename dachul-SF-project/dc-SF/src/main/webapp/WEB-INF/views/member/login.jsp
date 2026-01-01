<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 - 대철이제철 게시판</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h2>로그인</h2>
        
        <c:if test="${not empty errorMsg}">
            <div class="alert alert-danger">
                ${errorMsg}
            </div>
        </c:if>
        
        <c:if test="${not empty logoutMsg}">
            <div class="alert alert-success">
                ${logoutMsg}
            </div>
        </c:if>
        
        <c:if test="${not empty msg}">
            <div class="alert alert-success">
                ${msg}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="id">아이디</label>
                <input type="text" id="id" name="id" placeholder="아이디를 입력하세요" required autofocus>
            </div>
            
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" placeholder="비밀번호를 입력하세요" required>
            </div>
            
            <div class="form-group" style="display: flex; align-items: center; gap: 8px;">
                <input type="checkbox" id="remember-me" name="remember-me">
                <label for="remember-me" style="margin-bottom: 0; font-weight: normal;">로그인 상태 유지</label>
            </div>
            
            <div class="form-group">
                <button type="submit">로그인</button>
            </div>
        </form>
        
        <div class="links">
            <a href="${pageContext.request.contextPath}/member/join">회원가입</a>
            <a href="${pageContext.request.contextPath}/">메인으로</a>
        </div>
    </div>
    
    <script>
    <%
        String message = (String)session.getAttribute("message");
        if(message != null) {
    %>
            alert("<%= message %>");
    <%
            session.removeAttribute("message");
        }
    %>
    </script>
</body>
</html>
