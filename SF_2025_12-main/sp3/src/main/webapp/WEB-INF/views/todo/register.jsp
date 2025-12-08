<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Todo 등록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
<style>
    .form-container {
        max-width: 600px;
        margin: 0 auto;
    }
</style>
</head>
<body>
    <div class="container mt-4">
        <div class="form-container">
            <h2 class="mb-4">✨ 새 Todo 등록</h2>
            <form action="${pageContext.request.contextPath}/todo/register" method="post">
              <div class="mb-3">
                <label for="title" class="form-label">제목 <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="title" 
                       placeholder="Todo 제목을 입력하세요" name="title" required>
              </div>
              <div class="mb-3">
                <label for="description" class="form-label">설명</label>
                <textarea class="form-control" id="description" rows="5" 
                          placeholder="Todo에 대한 상세 설명을 입력하세요" name="description"></textarea>
              </div>
              <div class="mb-3 form-check">
                <input type="hidden" name="done" value="false">
                <input type="checkbox" class="form-check-input" id="done" name="done" value="true">
                <label class="form-check-label" for="done">
                    완료 상태로 등록
                </label>
              </div>
              
              <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                  <button type="submit" class="btn btn-primary btn-lg me-md-2">등록</button>
                  <a href="${pageContext.request.contextPath}/todo/list" class="btn btn-secondary btn-lg">취소</a>
              </div>
            </form>
        </div>
    </div>
</body>
</html>
