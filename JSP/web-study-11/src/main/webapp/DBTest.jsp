<%@ page import="java.sql.Connection" %>
<%@ page import="util.DBManager" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Connection con = DBManager.getConnection();
	
	out.println("con: " + con);
%>>