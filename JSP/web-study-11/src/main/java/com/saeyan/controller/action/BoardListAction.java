package com.saeyan.controller.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

/**
 * 게시글 목록 조회를 처리하는 Action 클래스
 * 
 * 역할: 게시글 목록을 데이터베이스에서 조회하고, JSP로 전달하여 화면에 표시
 * 
 * 처리 흐름:
 * 1. BoardDAO를 통해 데이터베이스에서 게시글 목록 조회
 * 2. 조회된 데이터를 request 객체에 저장 (속성명: "boardList")
 * 3. boardList.jsp로 forward하여 화면 출력
 * 
 * 사용 시나리오:
 * - URL: BoardServlet?command=board_list
 * - ActionFactory에서 command="board_list"일 때 이 클래스의 인스턴스 생성
 */
public class BoardListAction implements Action {

	/**
	 * 게시글 목록 조회 및 화면 출력을 수행하는 메서드
	 * 
	 * 처리 과정:
	 * 1. 데이터베이스에서 게시글 목록 조회 (BoardDAO 사용)
	 * 2. 조회된 List<BoardVO>를 request에 저장
	 * 3. boardList.jsp로 forward하여 화면 출력
	 * 
	 * @param request HTTP 요청 객체 (데이터 저장 및 전달용)
	 * @param response HTTP 응답 객체 (forward용)
	 */
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1단계: 이동할 JSP 페이지 경로 지정
		// forward할 때 사용할 JSP 파일의 경로
		String url = "/board/boardList.jsp";

		// 2단계: BoardDAO 인스턴스 가져오기 (싱글톤 패턴)
		// BoardDAO는 데이터베이스 접근을 담당하는 클래스
		BoardDAO bdao = BoardDAO.getInstance();
		
		// 3단계: 데이터베이스에서 게시글 목록 조회
		// selectAllBoards() 메서드가 데이터베이스에 연결하여
		// 모든 게시글을 조회하고 List<BoardVO> 형태로 반환
		List<BoardVO> list = bdao.selectAllBoards();

		// 4단계: 조회된 게시글 목록을 request 객체에 저장
		// 속성명: "boardList"
		// JSP에서 ${boardList}로 접근 가능
		request.setAttribute("boardList", list);

		// 5단계: boardList.jsp로 forward
		// forward는 서버 내부에서 페이지를 이동하는 방식
		// request와 response 객체가 그대로 전달되므로
		// 위에서 저장한 "boardList" 속성을 JSP에서 사용할 수 있음
		request.getRequestDispatcher(url)
            .forward(request, response);
	}

}
