package com.saeyan.dto;

import java.sql.*;

/*
 * DB연결을 담당하는 클래스
 * 싱글톤으로 객체 하나만 생성해서 공유 
 */
public class MemberDAO {

	//1. 싱글톤 패턴: 객체를 하나만 생성 
    private static MemberDAO instance = new MemberDAO();

    //2. 외부에서 생성자 호출 못하도록 Private
    private MemberDAO() {

    }

    //3. 외부에서 사용할 수 있도록 단일 인스턴스 제
    public static MemberDAO getInstance() {
        return instance;
    }

    //4. DB 연결 메소드(JDBC)
    public Connection getConnection() {
        Connection con = null;
        
        try {
        	//드라이브 로드
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	
        	//연결정보
        	con = DriverManager.getConnection(
        			"jdbc:oracle:thin:@localhost:1521:FREE",
        			"system",
        			"password"
        			);
        			
        	
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        return con;
    }

    //5. 로그인 인증 메소드
    public int userCheck(String userid, String pwd) {
        int result = -1; // -1: DB오류, 0: 비밀번호 불일치, 1: 로그인 성공
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = getConnection();
            String sql = "select pwd from member where userid = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userid);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
                if(rs.getString("pwd").equals(pwd)) {
                    result = 1; // 로그인 성공
                } else {
                    result = 0; // 비밀번호 불일치
                }
            } else {
                result = 0; // 존재하지 않는 사용자
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(con != null) con.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        return result;
    } // end of userCheck

    //6. 아이디로 회원 정보 가져오는 메소드
    public MemberVO getMember(String userid) {
        
        MemberVO mvo = null;
        String sql = "select * from member where userid = ?"; // 아이디로 회원 정보 가져오는 쿼리

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userid);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                mvo.setName(rs.getString("name"));
                mvo.setUserid(rs.getString("userid"));
                mvo.setPwd(rs.getString("pwd"));
                mvo.setEmail(rs.getString("email"));
                mvo.setPhone(rs.getString("phone"));
                mvo.setAdmin(rs.getInt("admin"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(con != null) con.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return mvo;
    } // end of getMember

}
