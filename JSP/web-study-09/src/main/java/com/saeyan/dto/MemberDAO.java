package com.saeyan.dto;

import java.sql.*;

/**
 * 데이터베이스 접근 객체(Data Access Object)
 * 
 * 역할: Oracle 데이터베이스와의 모든 상호작용을 담당
 * - 싱글톤 패턴으로 구현하여 하나의 인스턴스만 생성
 * - 회원 관련 모든 DB 작업(조회, 삽입, 수정, 중복 체크 등) 수행
 */
public class MemberDAO {

    /**
     * 싱글톤 패턴 구현
     * - 클래스 로딩 시점에 인스턴스를 미리 생성
     * - 프로그램 전체에서 하나의 MemberDAO 인스턴스만 사용
     */
    private static MemberDAO instance = new MemberDAO();

    /**
     * 생성자를 private로 선언하여 외부에서 인스턴스 생성 방지
     * 싱글톤 패턴의 핵심: 외부에서는 getInstance()를 통해서만 인스턴스 획득 가능
     */
    private MemberDAO() {

    }

    /**
     * MemberDAO 인스턴스를 반환하는 정적 메서드
     * @return MemberDAO의 유일한 인스턴스
     */
    public static MemberDAO getInstance() {
        return instance;
    }

    /**
     * Oracle 데이터베이스와의 연결을 생성하는 메서드
     * 
     * @return Connection 객체 (연결 실패 시 null 반환)
     * 
     * 연결 정보:
     * - URL: jdbc:oracle:thin:@localhost:1521:FREE (SID 방식)
     * - 사용자: system
     * - 비밀번호: password
     */
    public Connection getConnection() {
        Connection con = null;
        
        try {
            // Oracle JDBC 드라이버 클래스를 메모리에 로드
            // 드라이버가 없으면 ClassNotFoundException 발생
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	
        	// 데이터베이스 연결 정보 설정
        	String url = "jdbc:oracle:thin:@localhost:1521:FREE";  // SID 방식 연결
        	String user = "system";                                // 데이터베이스 사용자명
        	String password = "password";                           // 데이터베이스 비밀번호
        	
        	// DriverManager를 통해 실제 데이터베이스 연결 시도
        	con = DriverManager.getConnection(url, user, password);
        } catch(ClassNotFoundException e) {
            // Oracle JDBC 드라이버를 찾을 수 없는 경우
        	e.printStackTrace();
        } catch(SQLException e) {
            // 데이터베이스 연결 실패 (서버 미실행, 잘못된 연결 정보 등)
        	e.printStackTrace();
        } catch(Exception e) {
            // 기타 예상치 못한 오류
        	e.printStackTrace();
        }
        
        return con;
    }

    /**
     * 로그인 인증을 수행하는 메서드
     * 
     * @param userid 사용자가 입력한 아이디
     * @param pwd 사용자가 입력한 비밀번호
     * @return 인증 결과
     *         - 1: 로그인 성공 (아이디와 비밀번호 일치)
     *         - 0: 로그인 실패 (아이디 없음 또는 비밀번호 불일치)
     *         - -1: 데이터베이스 오류
     * 
     * 처리 과정:
     * 1. 데이터베이스 연결
     * 2. 아이디로 회원 정보 조회
     * 3. 조회된 비밀번호와 입력한 비밀번호 비교
     * 4. 결과 반환
     */
    public int userCheck(String userid, String pwd) {
        int result = -1;  // 기본값: DB 오류
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // 데이터베이스 연결
            con = getConnection();
            if(con == null) {
                return -1;  // 연결 실패 시 -1 반환
            }
            
            // PreparedStatement를 사용하여 SQL Injection 방지
            // ?는 나중에 setString()으로 값을 설정
            String sql = "select pwd from member where userid = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userid);  // 첫 번째 ?에 userid 값 설정
            
            // SELECT 쿼리 실행 및 결과 받기
            rs = pstmt.executeQuery();
            
            // 결과가 있는지 확인 (rs.next()는 다음 행이 있으면 true 반환)
            if(rs.next()) {
                // 데이터베이스에서 조회한 비밀번호
                String dbPwd = rs.getString("pwd");
                
                // 입력한 비밀번호와 DB의 비밀번호 비교
                if(dbPwd != null && dbPwd.equals(pwd)) {
                    result = 1;  // 로그인 성공
                } else {
                    result = 0;  // 비밀번호 불일치
                }
            } else {
                // 조회 결과가 없음 = 존재하지 않는 아이디
                result = 0;
            }
        } catch(SQLException e) {
            // SQL 실행 중 오류 발생
            e.printStackTrace();
            result = -1;
        } catch(Exception e) {
            // 기타 예상치 못한 오류
            e.printStackTrace();
            result = -1;
        } finally {
            // 리소스 해제 (반드시 실행됨)
            // 연결을 닫지 않으면 데이터베이스 연결이 고갈될 수 있음
            try {
                if(rs != null) rs.close();        // ResultSet 닫기
                if(pstmt != null) pstmt.close();  // PreparedStatement 닫기
                if(con != null) con.close();      // Connection 닫기
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }

    /**
     * 아이디로 회원 정보를 조회하는 메서드
     * 
     * @param userid 조회할 회원의 아이디
     * @return MemberVO 객체 (회원 정보가 없으면 null)
     * 
     * 사용 예시:
     * - 로그인 성공 후 전체 회원 정보를 세션에 저장할 때
     * - 회원정보 수정 페이지에서 기존 정보를 불러올 때
     */
    public MemberVO getMember(String userid) {
        MemberVO mvo = null;  // 반환할 객체 (조회 실패 시 null)
        String sql = "select * from member where userid = ?";  // 모든 컬럼 조회

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 데이터베이스 연결
            con = getConnection();
            if(con == null) {
                return null;  // 연결 실패 시 null 반환
            }
            
            // SQL 쿼리 준비
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userid);  // 첫 번째 ?에 userid 값 설정

            // 쿼리 실행
            rs = pstmt.executeQuery();
            
            // 결과가 있으면 MemberVO 객체에 데이터 저장
            if(rs.next()) {
                mvo = new MemberVO();
                // ResultSet에서 데이터를 가져와서 MemberVO 객체에 설정
                mvo.setName(rs.getString("name"));        // 이름
                mvo.setUserid(rs.getString("userid"));    // 아이디
                mvo.setPwd(rs.getString("pwd"));           // 비밀번호
                mvo.setEmail(rs.getString("email"));      // 이메일
                mvo.setPhone(rs.getString("phone"));     // 전화번호
                mvo.setAdmin(rs.getInt("admin"));         // 등급
            }
        } catch(Exception e) {
            // 예외 발생 시 스택 트레이스 출력
            e.printStackTrace();
        } finally {
            // 리소스 해제
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(con != null) con.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return mvo;
    }

    /**
     * 아이디 중복 여부를 확인하는 메서드
     * 
     * @param userid 중복 확인할 아이디
     * @return 중복 확인 결과
     *         - 1: 중복된 아이디 (이미 존재함)
     *         - 0: 사용 가능한 아이디 (존재하지 않음)
     *         - -1: 데이터베이스 오류
     * 
     * 사용 예시:
     * - 회원가입 시 아이디 중복 체크 팝업에서 사용
     */
    public int confirmID(String userid) {
        int result = -1;  // 기본값: DB 오류
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // 데이터베이스 연결
            con = getConnection();
            
            // 해당 아이디가 존재하는지 확인하는 쿼리
            String sql = "select userid from member where userid = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userid);
            
            // 쿼리 실행
            rs = pstmt.executeQuery();
            
            // 결과 확인
            if(rs.next()) {
                // 결과가 있으면 = 해당 아이디가 이미 존재함
                result = 1;  // 중복
            } else {
                // 결과가 없으면 = 해당 아이디가 존재하지 않음
                result = 0;  // 사용 가능
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(con != null) con.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }

    /**
     * 새로운 회원 정보를 데이터베이스에 삽입하는 메서드
     * 
     * @param mvo 저장할 회원 정보가 담긴 MemberVO 객체
     * @return 삽입 결과
     *         - 1: 회원가입 성공
     *         - 0: 회원가입 실패
     *         - -1: 데이터베이스 오류
     *         - -2: 중복된 아이디 (제약 조건 위반)
     * 
     * 처리 과정:
     * 1. MemberVO 객체에서 각 필드 값을 가져옴
     * 2. INSERT 쿼리 실행
     * 3. 결과 반환
     */
	public int insertMember(MemberVO mvo) {
		int result = -1;  // 기본값: DB 오류

		// INSERT 쿼리: member 테이블에 6개 컬럼 값 삽입
		// 순서: name, userid, pwd, email, phone, admin
		String sql = "insert into member values (?, ?, ?, ?, ?, ?)";
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// 데이터베이스 연결
			con = this.getConnection();
			if(con == null) {
				return -1;  // 연결 실패
			}
			
			// SQL 쿼리 준비
			pstmt = con.prepareStatement(sql);
			
			// 각 ?에 실제 값 설정 (순서 중요!)
			pstmt.setString(1, mvo.getName());      // 첫 번째 ?: 이름
			pstmt.setString(2, mvo.getUserid());    // 두 번째 ?: 아이디
			pstmt.setString(3, mvo.getPwd());       // 세 번째 ?: 비밀번호
			pstmt.setString(4, mvo.getEmail());     // 네 번째 ?: 이메일
			pstmt.setString(5, mvo.getPhone());     // 다섯 번째 ?: 전화번호
			pstmt.setInt(6, mvo.getAdmin());        // 여섯 번째 ?: 등급
			
			// INSERT 쿼리 실행
			// executeUpdate()는 영향을 받은 행의 수를 반환 (성공 시 1)
			result = pstmt.executeUpdate();
			
		} catch(SQLIntegrityConstraintViolationException e) {
			// 제약 조건 위반 오류 (주로 중복된 아이디)
			e.printStackTrace();
			if(e.getMessage() != null && e.getMessage().contains("USERID")) {
				result = -2;  // 중복된 아이디
			} else {
				result = -1;  // 기타 제약 조건 위반
			}
		} catch(Exception e) {
			// 기타 예외 (데이터 크기 초과 등)
			e.printStackTrace();
			result = -1;
		} finally {
			// 리소스 해제
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 회원 정보를 수정하는 메서드
	 * 
	 * @param mvo 수정할 회원 정보가 담긴 MemberVO 객체
	 * @return 수정 결과
	 *         - 1: 수정 성공
	 *         - 0: 수정 실패 (해당 아이디가 없거나 오류 발생)
	 *         - -1: 데이터베이스 오류
	 * 
	 * 주의사항:
	 * - 이름(name)은 수정하지 않음 (readonly 필드)
	 * - 아이디(userid)는 수정 불가 (WHERE 조건으로 사용)
	 * - 비밀번호, 이메일, 전화번호, 등급만 수정 가능
	 */
	public int updateMember(MemberVO mvo) {
		int result = -1;  // 기본값: DB 오류
		
		// UPDATE 쿼리: userid를 기준으로 나머지 정보 수정
		String sql = "update member set pwd=?, email=?, phone=?, admin=? where userid=?";
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			// 데이터베이스 연결
			con = getConnection();
			
			// SQL 쿼리 준비
			pstmt = con.prepareStatement(sql);
			
			// 각 ?에 실제 값 설정
			pstmt.setString(1, mvo.getPwd());       // 첫 번째 ?: 비밀번호
			pstmt.setString(2, mvo.getEmail());     // 두 번째 ?: 이메일
			pstmt.setString(3, mvo.getPhone());     // 세 번째 ?: 전화번호
			pstmt.setInt(4, mvo.getAdmin());         // 네 번째 ?: 등급
			pstmt.setString(5, mvo.getUserid());     // 다섯 번째 ?: WHERE 조건의 아이디
			
			// UPDATE 쿼리 실행
			// executeUpdate()는 영향을 받은 행의 수를 반환 (성공 시 1)
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			// 리소스 해제
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
