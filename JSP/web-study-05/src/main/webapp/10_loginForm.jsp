<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<!-- 로그인 화면  w3schools 참고해서 코드 가져와서 수정-->
<div class="container mt-3">
  <h2>로그인 화면</h2>
  <form action="10_testLogin.jsp" method="post"> <!-- 로그인 처리 페이지, 회원정보 확인 후 로그인 처리 -->
    <div class="mb-3 mt-3">
      <label for="userId">아이디:</label>
      <input type="text" class="form-control" id="userId" placeholder="Enter ID" name="userId">
    </div>
    <div class="mb-3">
      <label for="userPwd">비밀번호:</label>
      <input type="password" class="form-control" id="userPwd" placeholder="Enter Password" name="userPwd">
    </div>
    <button type="submit" class="btn btn-primary">로그인</button> <!-- 로그인 버튼 -->
    <!-- 다른 색상의 버튼 추가하려면 class="btn btn-primary" 를 class="btn btn-secondary" 로 변경 -->
  </form>
</div>

</body>
</html>
