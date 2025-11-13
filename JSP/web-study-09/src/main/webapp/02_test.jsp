<%@page import="java.sql.Connection"%>
<%@page import="com.saeyan.dto.MemberDAO"%>

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

	MemberDAO memDao = MemberDAO.getInstance();

	Connection con = memDao.getConnection();

%>
</body>
</html>