<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Todo 상세보기</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
<style>
    .form-container {
        max-width: 700px;
        margin: 0 auto;
    }
    .todo-header {
        border-bottom: 3px solid #0d6efd;
        padding-bottom: 15px;
        margin-bottom: 30px;
    }
</style>
</head>
<body>
    <div class="container mt-4">
        <div class="form-container">
            <div class="todo-header">
                <h2 class="mb-2">📋 Todo 상세보기</h2>
                <p class="text-muted mb-0">
                    <c:if test="${todo.done}">
                        <span class="badge bg-success">완료</span>
                    </c:if>
                    <c:if test="${!todo.done}">
                        <span class="badge bg-warning">진행중</span>
                    </c:if>
                    <span class="ms-2">생성일: <c:out value="${todo.createdDate}" /></span>
                </p>
            </div>
            
            <form action="${pageContext.request.contextPath}/todo/read" method="post" id="form">
              <div class="mb-3">
                <label for="title" class="form-label">제목</label>
                <input type="text" class="form-control form-control-lg" id="title" name="title" 
                       value="<c:out value="${todo.title}" />" required>
              </div>
              <div class="mb-3">
                <label for="description" class="form-label">설명</label>
                <textarea class="form-control" id="description" rows="6" name="description"><c:out value="${todo.description}" /></textarea>
              </div>
              <div class="mb-3 form-check">
                <input type="hidden" name="done" value="false">
                <input type="checkbox" class="form-check-input" id="done" name="done" value="true"
                       <c:if test="${todo.done}">checked</c:if>>
                <label class="form-check-label" for="done">
                    완료 상태
                </label>
              </div>
              
              <input type="hidden" name="id" value="<c:out value='${todo.id}' />" />
              
              <div class="d-grid gap-2 d-md-flex justify-content-md-end mt-4">
                  <button type="button" class="btn btn-primary btn-lg update">수정</button>
                  <button type="button" class="btn btn-danger btn-lg delete">삭제</button>
                  <a href="${pageContext.request.contextPath}/todo/list" class="btn btn-info btn-lg">목록</a>
              </div>
            </form>
        </div>
    </div>

<script type="text/javascript">
  const formObj = document.getElementById("form");
  const contextPath = "${pageContext.request.contextPath}";
  
  document.querySelector(".update").addEventListener("click", ()=>{
    formObj.action = contextPath + "/todo/modify";
    formObj.method = "post";
    formObj.submit();
  });
  
  document.querySelector(".delete").addEventListener("click", ()=>{
    if (confirm("정말 삭제하시겠습니까?")) {
        formObj.action = contextPath + "/todo/delete";
        formObj.method = "post";
        formObj.submit();
    }
  });
</script>

</body>
</html>
