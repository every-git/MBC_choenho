<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
    if(session.getAttribute("loginUser") == null) { //<!--- 세션에 이름이 없으면 로그인 페이지로 이동 -->
        response.sendRedirect("10_loginForm.jsp");
    } else {
        String name = (String)session.getAttribute("loginUser"); //<!-- 세션에 이름이 있으면 이름 출력 -->
        out.println("<h3> " + name + "님 환영합니다. </h3>"); 
%>
        <form method="post" action="10_logout.jsp"> <!-- 로그아웃 처리 페이지 -->
            <input type="submit" value="로그아웃"> <!--로그아웃 버튼-->
        </form>
<% } %>

</body>
</html>