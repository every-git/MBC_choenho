<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Todo 목록</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
<style>
    .todo-card {
        transition: transform 0.2s;
    }
    .todo-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 8px rgba(0,0,0,0.1);
    }
    .done-badge {
        font-size: 0.9em;
    }
</style>
</head>
<body>
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="display-5">📝 Todo 게시판</h1>
        <a href="${pageContext.request.contextPath}/todo/register" class="btn btn-primary btn-lg">
            <i class="bi bi-plus-circle"></i> 새 Todo 등록
        </a>
    </div>
    
    <c:if test="${empty todoList}">
        <div class="alert alert-info text-center">
            <h4>등록된 Todo가 없습니다.</h4>
            <p>새로운 Todo를 등록해보세요!</p>
        </div>
    </c:if>
    
    <c:if test="${not empty todoList}">
        <div class="row">
            <c:forEach var="todo" items="${todoList}">
                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card todo-card h-100 ${todo.done ? 'border-success' : 'border-warning'}">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <h5 class="card-title">
                                    <a href="${pageContext.request.contextPath}/todo/read/${todo.id}" 
                                       class="text-decoration-none text-dark">
                                        <c:out value="${todo.title}" />
                                    </a>
                                </h5>
                                <span class="badge ${todo.done ? 'bg-success' : 'bg-warning'} done-badge">
                                    <c:out value="${todo.doneStatus}" />
                                </span>
                            </div>
                            <p class="card-text text-muted">
                                <c:out value="${todo.description}" />
                            </p>
                            <div class="d-flex justify-content-between align-items-center mt-3">
                                <small class="text-muted">
                                    📅 <c:out value="${todo.createdDate}" />
                                </small>
                                <a href="${pageContext.request.contextPath}/todo/read/${todo.id}" 
                                   class="btn btn-sm btn-outline-primary">상세보기</a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>
</body>
</html>
