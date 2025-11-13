<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
    //선언문
    Connection con = null; // 연결
    //Statement stmt = null; // 문장
    PreparedStatement pstmt = null;
    ResultSet rs = null; // 결과 집합

    String url = "jdbc:oracle:thin:@localhost:1521:FREE"; // 데이터베이스 연결 정보
    String uid = "system";
    String upw = "password";
    String sql = "select * from member"; // 모든 회원 정보를 조회하는 쿼리
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>회원 목록</h1>

    <table width="800" border="1">
        <tr>
            <th>이름</th>
            <th>아이디</th>
            <th>암호</th>
            <th>이메일</th>
            <th>전화번호</th>
            <th>권한(1:관리자, 0:일반회원)</th>
        </tr>
        <%
            try {   
            Class.forName("oracle.jdbc.driver.OracleDriver"); // 드라이버 로드
            con = DriverManager.getConnection(url, uid, upw); // 오라클 연결
            pstmt = con.prepareStatement(sql);
            
            //stmt =conn.createStatement(); //sql 문장 오라클 전송
            //rs = stmt.executeQuery(sql); //sql 문장 실행
			rs = pstmt.executeQuery();
            
            while(rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("userid") + "</td>");
                out.println("<td>" + rs.getString("pwd") + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("phone") + "</td>");
                out.println("<td>" + rs.getInt("admin") + "</td>"); // 정수를 가져옴
                out.println("</tr>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            pstmt.close();
            con.close();
        }
        %>





    </table>
</body>
</html>