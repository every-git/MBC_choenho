<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <title>Login</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

<div class="container mt-5">
  <div class="row justify-content-center">
    <div class="col-md-4">
      <h2 class="text-center mb-4">로그인</h2>
      <c:if test="${param.error != null}">
        <div class="alert alert-danger" role="alert">
          로그인에 실패했습니다. 사용자명과 비밀번호를 확인해주세요.
        </div>
      </c:if>
      <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="mb-3">
          <label for="username" class="form-label">사용자명:</label>
          <input type="text" class="form-control" id="username" name="username" placeholder="사용자명을 입력하세요" required>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">비밀번호:</label>
          <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요" required>
        </div>
        <button type="submit" class="btn btn-primary w-100">로그인</button>
      </form>
      <div class="mt-3 text-center">
        <small class="text-muted">테스트 계정: 아무 사용자명 / 비밀번호: 1111</small>
      </div>
    </div>
  </div>
</div>

</body>
</html>

