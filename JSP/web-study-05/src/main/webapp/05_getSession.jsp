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
    Object id = session.getAttribute("id");
    Object pwd = session.getAttribute("pwd");
    Object age = session.getAttribute("age");
    %>
    <h3> 세션 정보 확인 </h3>
    <h4> id: <%= id %> </h4>
    <h4> pwd: <%= pwd %> </h4>
    <h4> age: <%= age %> </h4>
    
</body>
</html>