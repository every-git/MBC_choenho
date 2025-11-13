<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

    <c:choose>
        <c:when test="${param.fruit == 1}">
            <h3>사과를 선택했습니다.</h3>
        </c:when>
        <c:when test="${param.fruit == 2}">
            <h3>메론을 선택했습니다.</h3>
        </c:when>
        <c:when test="${param.fruit == 3}">
            <h3>바나나를 선택했습니다.</h3>
        </c:when>
        <c:otherwise>
            <span style="color: red;">그 외</span>
        </c:otherwise>
    </c:choose>
    
</body>
</html>