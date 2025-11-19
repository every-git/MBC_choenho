<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// 메인 페이지 접속 시 게시글 목록 페이지로 리다이렉트
	// command 값: "board_list"
	response.sendRedirect("BoardServlet?command=board_list");
%>