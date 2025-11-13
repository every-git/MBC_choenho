<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.sql.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
    <body>
        <%!
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    String url = "jdbc:oracle:thin:@localhost:1521:FREE";
    String uid = "system";
    String upw = "password";
    String sql = "insert into member values (?, ?, ?, ?, ?, ?)";
%>

<%
    request.setCharacterEncoding("UTF-8");

    // 02_addMemberForm.jsp 에서 입력한 값을 받아옴
    String name = request.getParameter("name");
    String userid = request.getParameter("userid");
    String pwd = request.getParameter("pwd");
    String email = request.getParameter("email");
    String phone = request.getParameter("phone");
    int admin = Integer.parseInt(request.getParameter("admin"));

    try {
    	//1. 드라이브 로딩
        Class.forName("oracle.jdbc.driver.OracleDriver");
        
    	//2. 데이터베이스 연결
    	con = DriverManager.getConnection(url, uid, upw);
        //out.println(con);
        
    	//3. SQL 구문을 실행할 PrepareStatement 객체 생성.
        pstmt = con.prepareStatement(sql);
        
        //4. 폼에서 입력한것을 values(?)맵핑
        pstmt.setString(1, name);
        pstmt.setString(2, userid);
        pstmt.setString(3, pwd);
        pstmt.setString(4, email);
        pstmt.setString(5, phone);
        pstmt.setInt(6, admin);
        
        //5. 실행 : 성공하면 1, 실패하면 0
        int result = pstmt.executeUpdate();
        out.println(result);
        
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
		if(pstmt != null) pstmt.close();
		if(con != null) con.close();
    }
%>

    <h3>회원 가입 성공</h3>
    <a href="01_allMember.jsp">회원 목록 보기</a>
    </body>
</html>
