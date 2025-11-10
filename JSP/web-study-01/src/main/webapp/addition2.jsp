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
		// 1. 서블릿이 "n1", "n2", "sum"이라는 이름으로 보낸 값을 받는다.
        // (null 체크를 포함하는 것이 더 안전합니다)
		int num1 = (Integer)request.getAttribute("n1");
		int num2 = (Integer)request.getAttribute("n2");
		int add = (Integer)request.getAttribute("sum");	
	%>
	
	<%= num1 %> + <%= num2 %> = <%= add %>
</body>
</html>