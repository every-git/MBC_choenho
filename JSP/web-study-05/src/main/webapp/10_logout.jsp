<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그아웃</title>
</head>
<body>

<%
	session.removeAttribute("loginUser"); //<!--세션에서 이름 제거-->
	session.invalidate(); //<!-- 세션 초기화 -->
%>

<script type="text/javascript">
	alert("로그아웃 되었습니다.");
	location.href = "10_loginForm.jsp"; // 로그아웃 후 로그인 페이지로 이동
</script>

</body>
</html>