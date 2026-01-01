<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 상세 정보 - 관리자</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <jsp:include page="../../common/header.jsp" />

    <div class="container">
        <h2>회원 상세 정보</h2>
        
        <div class="member-detail">
            <div class="view-row">
                <label>아이디:</label>
                <span>${member.id}</span>
            </div>
            
            <div class="view-row">
                <label>이름:</label>
                <span>${member.name}</span>
            </div>
            
            <div class="view-row">
                <label>이메일:</label>
                <span>${member.email}</span>
            </div>
            
            <div class="view-row">
                <label>전화번호:</label>
                <span>${member.phone}</span>
            </div>
            
            <div class="view-row">
                <label>권한:</label>
                <span>${member.role}</span>
            </div>
            
            <div class="view-row">
                <label>가입일:</label>
                <span>
                    <fmt:parseDate value="${member.regdate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedRegDate" type="both"/>
                    <fmt:formatDate value="${parsedRegDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </span>
            </div>
        </div>
        
        <div class="button-group">
            <sec:authentication property="principal.username" var="currentUserId"/>
            <c:if test="${member.id != currentUserId}">
                <a href="${pageContext.request.contextPath}/admin/member/delete?id=${member.id}" 
                   onclick="return confirm('정말 ${member.name}(${member.id}) 회원을 강제 탈퇴시키겠습니까?\n\n이 작업은 되돌릴 수 없습니다.')"
                   class="btn-delete">강제탈퇴</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/board/list">게시판</a>
            <a href="${pageContext.request.contextPath}/admin/member/list">목록</a>
            <a href="${pageContext.request.contextPath}/admin/main">관리자 메인</a>
        </div>
    </div>
</body>
</html>
