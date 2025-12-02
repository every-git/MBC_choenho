package com.saeyan.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.saeyan.dto.BoardVO;
import com.saeyan.util.DBManager;

//DB연동 파트
@Repository
public class BoardRepository {
	
	public List<BoardVO> selectAllBoards() {

		// 데이터베이스 관련 객체들 선언
		Connection con = null;           // 데이터베이스 연결 객체
		PreparedStatement pstmt = null;  // SQL 실행 객체
		ResultSet rs = null;             // 조회 결과를 담는 객체
		
		// 실행할 SQL 쿼리
		// num 컬럼 기준 내림차순 정렬 (최신 게시글이 위에 오도록)
		String sql = "select * from board order by num desc";
		
		// 반환할 게시글 목록을 담을 리스트
		List<BoardVO> list = new ArrayList<BoardVO>();
		
		try {
			
			// 1단계: 데이터베이스 연결
			// DBManager를 통해 MySQL 데이터베이스에 연결
			con = DBManager.getConnection();
			
			// 2단계: SQL 쿼리를 실행할 PreparedStatement 생성
			// PreparedStatement는 SQL 인젝션 공격을 방지하고 성능을 향상시킴
			pstmt = con.prepareStatement(sql);
			
			// 3단계: SQL 쿼리 실행 및 결과 받기
			// SELECT 쿼리이므로 executeQuery() 사용
			// ResultSet에 조회된 데이터가 저장됨
			rs = pstmt.executeQuery();
			
			// 4단계: ResultSet에서 데이터 추출
			// rs.next()는 다음 행이 있으면 true, 없으면 false 반환
			// while 루프로 모든 행을 순회하면서 데이터 추출
			while(rs.next()) {

				// 각 행의 데이터를 담을 BoardVO 객체 생성
                BoardVO vo = new BoardVO();

				// ResultSet에서 컬럼별로 데이터 추출
                int num = rs.getInt("num");              // 게시글 번호
                String name = rs.getString("name");      // 작성자 이름

				// 추출한 데이터를 BoardVO 객체에 설정
				// 각 필드에 setter 메서드를 통해 값 저장
                vo.setNum(num);                                    // 게시글 번호 설정
                vo.setName(name);                                  // 작성자 이름 설정
                vo.setEmail(rs.getString("email"));               // 이메일 설정
                vo.setTitle(rs.getString("title"));               // 제목 설정
                vo.setContent(rs.getString("content"));           // 내용 설정
                vo.setReadcount(rs.getInt("readcount"));          // 조회수 설정
                vo.setWritedate(rs.getTimestamp("writedate"));    // 작성일 설정
                
				// 완성된 BoardVO 객체를 리스트에 추가
				// 모든 행을 처리하면 List<BoardVO> 형태의 게시글 목록이 완성됨
                list.add(vo);
			}
			
		} catch(Exception e) {
			// 예외 발생 시 에러 메시지 출력
			// 실제 운영 환경에서는 로깅 프레임워크 사용 권장
			e.printStackTrace();
		} finally {
			// 5단계: 사용한 자원 반납 (반드시 실행됨)
			// Connection, PreparedStatement, ResultSet은 사용 후 반드시 닫아야 함
			// finally 블록은 예외 발생 여부와 관계없이 항상 실행됨
			DBManager.close(con, pstmt, rs);
		}
		
		// 조회된 게시글 목록 반환
		// 데이터가 없으면 빈 리스트 반환
		return list;
	} //end selectAllBoards

	public void insertBoard(BoardVO vo) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String sql = "insert into board (name, pass, email, title, content) "
				+ "values (?, ?, ?, ?, ?)";
		
		try {
			con = DBManager.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPass());
			pstmt.setString(3, vo.getEmail());
			pstmt.setString(4, vo.getTitle());
			pstmt.setString(5, vo.getContent());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt);
		}
	} //end insertBoard

	public BoardVO selectOneByNum(int num) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from board where num = ?";

		BoardVO vo = new BoardVO();
		
		try {
			con = DBManager.getConnection(); // 데이터베이스 연결
			pstmt = con.prepareStatement(sql); // SQL 쿼리 실행
			pstmt.setInt(1, num); // 물음표에 num 값 설정
			rs = pstmt.executeQuery(); // SQL 쿼리 실행 결과 받기
			
			if(rs.next()) { // 결과가 있으면 데이터 추출
				vo.setNum(rs.getInt("num"));
				vo.setName(rs.getString("name"));
				vo.setPass(rs.getString("pass")); // 비밀번호 필드 추가
				vo.setEmail(rs.getString("email"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setReadcount(rs.getInt("readcount"));
				vo.setWritedate(rs.getTimestamp("writedate"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		return vo;
	} //end selectOneByNum

	public void updateReadCount(int num) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		String sql = "update board set readcount = readcount + 1 where num = ?";

		try {
			con = DBManager.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt);
		}
	} //end updateReadCount

	public void deleteBoard(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;

		String sql = "delete from board where num = ?";

		try {
			con = DBManager.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt);
		}
		
	} //end deleteBoard

	public void updateBoard(BoardVO vo) {
		
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String sql = "update board set name=?, pass=?, email=?, title=?, content=? where num=? ";
		
		try {
			con = DBManager.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPass());
			pstmt.setString(3, vo.getEmail());
			pstmt.setString(4, vo.getTitle());
			pstmt.setString(5, vo.getContent());
			pstmt.setInt(6, vo.getNum());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt);
		}
	}

}
