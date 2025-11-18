package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * 개선된 데이터베이스 연결 관리 클래스
 * - db.properties 파일에서 설정 읽기
 * - 환경변수 지원
 * - 로깅 개선
 * 
 * 사용법:
 * 1. 기존 DBManager.java를 백업
 * 2. 이 파일의 이름을 DBManager.java로 변경
 * 3. db.properties 파일 생성 필요
 */
public class DBManager_improved {

    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;
    
    // 정적 초기화 블록: 클래스 로드 시 한 번만 실행
    static {
        loadDatabaseConfig();
    }
    
    /**
     * 데이터베이스 설정 로드
     * 우선순위: 환경변수 > db.properties > 기본값
     */
    private static void loadDatabaseConfig() {
        Properties props = new Properties();
        InputStream inputStream = null;
        
        try {
            // 1. db.properties 파일 읽기 시도
            inputStream = DBManager_improved.class.getClassLoader()
                .getResourceAsStream("db.properties");
            
            if (inputStream != null) {
                props.load(inputStream);
                System.out.println("[DBManager] db.properties 파일 로드 성공");
            } else {
                System.out.println("[DBManager] db.properties 파일을 찾을 수 없습니다. 기본값을 사용합니다.");
            }
            
            // 2. 설정 값 로드 (환경변수 우선, 없으면 properties, 없으면 기본값)
            DB_DRIVER = getConfigValue("db.driver", props, "com.mysql.cj.jdbc.Driver");
            DB_URL = getConfigValue("db.url", props, 
                "jdbc:mysql://localhost:3306/edudb?serverTimezone=Asia/Seoul");
            DB_USERNAME = getConfigValue("db.username", props, "jdbctest");
            DB_PASSWORD = getConfigValue("db.password", props, "1234");
            
            // 3. 드라이버 로드
            Class.forName(DB_DRIVER);
            System.out.println("[DBManager] JDBC 드라이버 로드 성공: " + DB_DRIVER);
            System.out.println("[DBManager] DB URL: " + maskPassword(DB_URL));
            System.out.println("[DBManager] DB User: " + DB_USERNAME);
            
        } catch (ClassNotFoundException e) {
            System.err.println("[DBManager ERROR] JDBC 드라이버를 찾을 수 없습니다: " + DB_DRIVER);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("[DBManager ERROR] db.properties 파일 읽기 실패");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[DBManager ERROR] 데이터베이스 설정 로드 실패");
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 설정 값 가져오기
     * 우선순위: 시스템 환경변수 > Properties 파일 > 기본값
     */
    private static String getConfigValue(String key, Properties props, String defaultValue) {
        // 1. 환경변수에서 찾기 (대문자로 변환, 점을 언더스코어로)
        String envKey = key.toUpperCase().replace('.', '_');
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) {
            System.out.println("[DBManager] 환경변수에서 " + key + " 로드");
            return envValue;
        }
        
        // 2. 시스템 프로퍼티에서 찾기
        String sysValue = System.getProperty(key);
        if (sysValue != null && !sysValue.isEmpty()) {
            System.out.println("[DBManager] 시스템 프로퍼티에서 " + key + " 로드");
            return sysValue;
        }
        
        // 3. Properties 파일에서 찾기
        String propValue = props.getProperty(key);
        if (propValue != null && !propValue.isEmpty()) {
            return propValue;
        }
        
        // 4. 기본값 반환
        System.out.println("[DBManager] " + key + "에 기본값 사용");
        return defaultValue;
    }
    
    /**
     * URL에서 비밀번호 마스킹 (로그용)
     */
    private static String maskPassword(String url) {
        if (url == null) return null;
        // password= 부분을 찾아서 마스킹
        return url.replaceAll("password=[^&;]+", "password=****");
    }

    /**
     * DB 연결 생성 메서드
     * @return Connection 객체 (실패시 null)
     */
    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            // System.out.println("[DBManager] DB 연결 성공");
        } catch (Exception e) {
            System.err.println("[DBManager ERROR] DB 연결 실패");
            System.err.println("URL: " + maskPassword(DB_URL));
            System.err.println("Username: " + DB_USERNAME);
            e.printStackTrace();
        }
        return con;
    }

    /**
     * SELECT 수행 후 자원 반납
     * @param con Connection 객체
     * @param stmt Statement 객체
     * @param rs ResultSet 객체
     */
    public static void close(Connection con, Statement stmt, ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
                // System.out.println("[DBManager] ResultSet 닫기 성공");
            }
            if(stmt != null) {
                stmt.close();
                // System.out.println("[DBManager] Statement 닫기 성공");
            }
            if(con != null) {
                con.close();
                // System.out.println("[DBManager] Connection 닫기 성공");
            }
        } catch (Exception e) {
            System.err.println("[DBManager ERROR] 자원 반납 실패");
            e.printStackTrace();
        }
    }

    /**
     * INSERT, UPDATE, DELETE 수행 후 자원 반납
     * @param con Connection 객체
     * @param stmt Statement 객체
     */
    public static void close(Connection con, Statement stmt) {
        try {
            if(stmt != null) {
                stmt.close();
                // System.out.println("[DBManager] Statement 닫기 성공");
            }
            if(con != null) {
                con.close();
                // System.out.println("[DBManager] Connection 닫기 성공");
            }
        } catch (Exception e) {
            System.err.println("[DBManager ERROR] 자원 반납 실패");
            e.printStackTrace();
        }
    }
    
    /**
     * DB 연결 테스트 메서드
     * @return 연결 성공 여부
     */
    public static boolean testConnection() {
        Connection con = null;
        try {
            con = getConnection();
            if (con != null && !con.isClosed()) {
                System.out.println("[DBManager] DB 연결 테스트 성공!");
                return true;
            }
        } catch (Exception e) {
            System.err.println("[DBManager ERROR] DB 연결 테스트 실패");
            e.printStackTrace();
        } finally {
            close(con, null);
        }
        return false;
    }
    
    /**
     * 현재 설정 정보 출력 (디버깅용)
     */
    public static void printConfig() {
        System.out.println("========================================");
        System.out.println("현재 데이터베이스 설정:");
        System.out.println("Driver: " + DB_DRIVER);
        System.out.println("URL: " + maskPassword(DB_URL));
        System.out.println("Username: " + DB_USERNAME);
        System.out.println("Password: ****");
        System.out.println("========================================");
    }
    
    /**
     * 테스트용 메인 메서드
     */
    public static void main(String[] args) {
        System.out.println("=== DBManager 테스트 시작 ===\n");
        
        // 1. 설정 정보 출력
        printConfig();
        
        // 2. 연결 테스트
        System.out.println("\n=== 연결 테스트 ===");
        boolean success = testConnection();
        
        if (success) {
            System.out.println("\n✅ 모든 테스트 통과!");
        } else {
            System.out.println("\n❌ 연결 테스트 실패");
        }
        
        System.out.println("\n=== DBManager 테스트 종료 ===");
    }
}

