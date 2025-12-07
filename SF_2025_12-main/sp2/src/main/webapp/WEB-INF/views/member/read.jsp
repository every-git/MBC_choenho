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
        <h2>Member Read</h2>
        <form action="${pageContext.request.contextPath}/member/read" method="post" id="form">
          <div class="mb-3 mt-3">
            <label for="name">Name:</label>
            <input type="text" class="form-control" id="name" name="name" 
            value="<c:out value="${member.name}" />">
          </div>
          <div class="mb-3">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" name="email" 
            value="<c:out value="${member.email}" />">
          </div>
          <div class="mb-3">
            <label for="pwd">Password:</label>
            <input type="password" class="form-control" id="pwd" name="password" 
            value="<c:out value="${member.password}" />">
          </div>
          
          <input type="hidden" name="mno" value="<c:out value='${member.mno}' />" />
          <button type="button" class="btn btn-primary update">수정</button>
          <button type="button" class="btn btn-danger delete">삭제</button>
          <button type="button" class="btn btn-info list">목록</button>
        </form>
      </div>

<script type="text/javascript">
  const formObj = document.getElementById("form");
  const contextPath = "${pageContext.request.contextPath}";
  
  document.querySelector(".update").addEventListener("click", ()=>{
    formObj.action = contextPath + "/member/modify";
    formObj.method = "post";
    formObj.submit();
  });
  document.querySelector(".delete").addEventListener("click", ()=>{
    formObj.action = contextPath + "/member/delete";
    formObj.method = "post";
    formObj.submit();
  });
  document.querySelector(".list").addEventListener("click", ()=>{
    window.location.href = contextPath + "/member/list";
  });
</script>

</body>
</html>