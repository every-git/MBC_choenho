<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 목록 - 관리자</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h2>회원 목록 관리</h2>
        
        <table class="admin-table">
            <thead>
                <tr>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>권한</th>
                    <th>가입일</th>
                    <th>관리</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="member" items="${memberList}">
                    <tr>
                        <td>${member.id}</td>
                        <td>${member.name}</td>
                        <td>${member.email}</td>
                        <td>${member.role}</td>
                        <td>
                            <fmt:parseDate value="${member.regdate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedRegDate" type="both"/>
                            <fmt:formatDate value="${parsedRegDate}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/member/detail?id=${member.id}">상세보기</a>
                            <sec:authorize access="isAuthenticated()">
                                <sec:authentication property="principal.username" var="currentUser"/>
                                <c:if test="${member.id != currentUser}">
                                    | <a href="${pageContext.request.contextPath}/admin/member/delete?id=${member.id}" 
                                         onclick="return confirm('정말 ${member.name}(${member.id}) 회원을 강제 탈퇴시키겠습니까?\n\n이 작업은 되돌릴 수 없습니다.')"
                                         style="color: var(--red);">강제탈퇴</a>
                                </c:if>
                            </sec:authorize>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="links">
            <a href="${pageContext.request.contextPath}/board/list">게시판</a>
            <a href="${pageContext.request.contextPath}/admin/main">관리자 메인</a>
        </div>
    </div>
</body>
</html>
