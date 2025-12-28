<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그아웃</title>

<!-- Bootstrap 5 CDN -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
    body {
        background-color: #f8f9fa;
    }
    .logout-box {
        max-width: 450px;
        margin-top: 150px;
        padding: 40px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0 0 15px rgba(0,0,0,0.1);
    }
    .icon-box {
        width: 80px;
        height: 80px;
        margin: 0 auto 20px;
        background-color: #d1e7dd;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .icon-box svg {
        width: 40px;
        height: 40px;
        color: #198754;
    }
</style>
</head>
<body>

<div class="container">
    <div class="row justify-content-center">
        <div class="logout-box text-center">
            
            <!-- 성공 아이콘 -->
            <div class="icon-box">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
            </div>

            <h3 class="mb-3">로그아웃 완료</h3>
            <p class="text-muted mb-4">
                정상적으로 로그아웃되었습니다.<br>
                이용해 주셔서 감사합니다.
            </p>

            <div class="d-grid gap-2">
                <a href="<c:url value='/' />" class="btn btn-primary">
                    홈으로 이동
                </a>
                <a href="<c:url value='/account/login' />" class="btn btn-outline-secondary">
                    다시 로그인
                </a>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>