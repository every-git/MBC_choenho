package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBManager {

    // DB 연결 메서드
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/edudb?serverTimezone=Asia/Seoul", 
                "jdbctest",
                "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    // select 수행한 후 자원 반납
    public static void close(Connection con, Statement stmt, ResultSet rs) {
        try {
            if(rs != null) rs.close();
            if(stmt != null) stmt.close();
            if(con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // insert, update, delete 수행한 후 자원 반납
    public static void close(Connection con, Statement stmt) {
        try {
            if(stmt != null) stmt.close();
            if(con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
