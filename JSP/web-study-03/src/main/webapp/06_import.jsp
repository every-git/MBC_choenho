<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.Calendar, java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
    Calendar date = Calendar.getInstance();

    SimpleDateFormat today = new SimpleDateFormat("yyyy년 MM월 dd일");
    SimpleDateFormat now = new SimpleDateFormat("hh시 mm분 ss초");
%>
오늘은 <%= today.format(date.getTime()) %> 입니다.<br>
지금 시각은 <b><%= now.format(date.getTime()) %> 입니다.</b>    
</body>
</html>