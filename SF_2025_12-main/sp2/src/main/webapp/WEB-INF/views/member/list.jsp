<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<body>
<div class="container mt-3">
    <div class="d-flex justify-content-end mb-3">
        <a href="${pageContext.request.contextPath}/member/register" class="btn btn-primary">등록</a>
    </div>
    <table class="table">
	<thead>
		<tr>
            <th scope="col">Mno</th>
            <th scope="col">Name</th>
            <th scope="col">Email</th>
            <th scope="col">RegDate</th> 
        </tr>
    </thead>
    <tbody>
        <c:forEach var="member" items="${memberList}">
            <tr>
                <td><c:out value="${member.mno}" /></td>
                <td>
                    <a href="${pageContext.request.contextPath}/member/read/${member.mno}">
                        <c:out value="${member.name}" />
                    </a>
                </td>
                <td><c:out value="${member.email}" /></td>
                <td>
                    <%
                        org.zerock.dto.MemberDTO m = (org.zerock.dto.MemberDTO)pageContext.getAttribute("member");
                        if (m != null && m.getRegDate() != null) {
                            out.print(m.getRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                        }
                    %>
                </td>
            </tr>
        </c:forEach>
    </tbody>
    </table>
</div>
</body>
</html>