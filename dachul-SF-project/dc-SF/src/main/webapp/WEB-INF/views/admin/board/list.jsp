<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>게시판 관리 - 관리자</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h2>게시판 관리</h2>
        <p class="subtitle">전체 게시글 목록</p>
        
        <table class="admin-table">
            <thead>
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>조회수</th>
                    <th>관리</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="board" items="${boardList}">
                    <tr>
                        <td>${board.seq}</td>
                        <td>${board.title}</td>
                        <td>${board.writer}</td>
                        <td>
                            <fmt:parseDate value="${board.regdate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedRegDate" type="both"/>
                            <fmt:formatDate value="${parsedRegDate}" pattern="yyyy-MM-dd"/>
                        </td>
                        <td>${board.hit}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/board/delete?seq=${board.seq}" 
                               onclick="return confirm('삭제하시겠습니까?')">삭제</a>
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
