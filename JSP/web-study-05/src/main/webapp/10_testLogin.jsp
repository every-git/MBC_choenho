<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
String userId = "test"; //<!--로그인 아이디-->
String userPwd = "test1234"; //<!--로그인 비밀번호-->
String name = "홍길동"; //<!--웰컴 페이지에 표시할 이름-->

String id2 = request.getParameter("userId"); //<!--로그인 아이디 파라미터 받기-->
String pwd2 = request.getParameter("userPwd"); //<!--로그인 비밀번호 파라미터 받기-->

if(userId.equals(id2) && userPwd.equals(pwd2)) { //<!--로그인 아이디와 비밀번호 확인-->
    session.setAttribute("loginUser", name); //<!--로그인 성공 시 세션에 이름 저장-->
    response.sendRedirect("10_main.jsp"); //<!--로그인 성공 시 웰컴 페이지로 이동-->
} else {
    response.sendRedirect("10_loginForm.jsp"); //<!--로그인 실패 시 로그인 페이지로 이동-->
}
%>