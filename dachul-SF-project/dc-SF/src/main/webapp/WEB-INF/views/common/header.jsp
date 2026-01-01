<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <!-- Header -->
    <div class="header">
        <div class="container">
            <h1><a href="${pageContext.request.contextPath}/">대철이제철 게시판</a></h1>
            <div class="user-info">
                <sec:authorize access="isAuthenticated()">
                    <span>
                        <sec:authentication property="principal.name"/>님
                        <sec:authorize access="hasRole('ADMIN')">
                            <span class="badge-admin">관리자</span>
                        </sec:authorize>
                    </span>
                    <a href="${pageContext.request.contextPath}/logout">로그아웃</a>
                </sec:authorize>
                <sec:authorize access="!isAuthenticated()">
                    <a href="${pageContext.request.contextPath}/member/login">로그인</a>
                    <a href="${pageContext.request.contextPath}/member/join">회원가입</a>
                </sec:authorize>
            </div>
        </div>
    </div>
    
    <!-- Navigation -->
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/">홈</a>
            <a href="${pageContext.request.contextPath}/board/list">게시판</a>
            <sec:authorize access="hasRole('ADMIN')">
                <a href="${pageContext.request.contextPath}/admin/main">관리자</a>
            </sec:authorize>
        </div>
    </nav>
