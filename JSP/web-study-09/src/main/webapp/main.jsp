<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
</head>
<body>
    <h1>🎉 로그인 성공!</h1>
    
    <% 
        String userid = (String)session.getAttribute("userid");
        if(userid != null) {
    %>
        <h2>환영합니다! <%=userid %>님</h2>
        <p>로그인에 성공했습니다.</p>
        
        <hr>
        
        <p>
            <a href="<%=request.getContextPath()%>/login.do">로그인 페이지로 돌아가기</a> |
            <a href="<%=request.getContextPath()%>/logout.do">로그아웃</a>
        </p>
    <% 
        } else {
    %>
        <p>세션이 없습니다. <a href="<%=request.getContextPath()%>/login.do">로그인</a> 해주세요.</p>
    <% 
        }
    %>
</body>
</html>
