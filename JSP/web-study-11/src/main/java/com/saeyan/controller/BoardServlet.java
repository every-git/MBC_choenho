package com.saeyan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.controller.action.Action;

/**
 * 게시판 요청을 처리하는 서블릿
 * 
 * 역할: 모든 게시판 관련 요청을 받아서 적절한 Action으로 위임
 * 
 * 흐름:
 * 1. 브라우저에서 요청 받기 (예: BoardServlet?command=board_list)
 * 2. command 파라미터 추출
 * 3. ActionFactory를 통해 적절한 Action 인스턴스 생성
 * 4. Action의 execute() 메서드 호출하여 실제 비즈니스 로직 실행
 */
@WebServlet("/BoardServlet")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardServlet() {
        
    }
    
	/**
	 * GET 요청 처리 메서드
	 * 
	 * 요청 예시: http://localhost:8080/web-study-11/BoardServlet?command=board_list
	 * 
	 * 처리 과정:
	 * 1. URL에서 command 파라미터 추출 (예: "board_list")
	 * 2. ActionFactory를 통해 command에 맞는 Action 인스턴스 생성
	 * 3. 생성된 Action의 execute() 메서드 호출
	 * 4. Action이 JSP로 forward하여 화면 출력
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1단계: URL에서 command 파라미터 추출
		// 예: BoardServlet?command=board_list → "board_list" 추출
		String command = request.getParameter("command");

		// 2단계: ActionFactory 인스턴스 가져오기 (싱글톤 패턴)
		// ActionFactory는 command 값에 따라 적절한 Action을 생성하는 팩토리 클래스
		ActionFactory af = ActionFactory.getInstance();
		
		// 3단계: command 값에 맞는 Action 인스턴스 생성
		// 예: command="board_list" → BoardListAction 인스턴스 반환
		//     command가 일치하지 않으면 null 반환
		Action action = af.getAction(command);
		
		// 4단계: Action이 null이 아니면 execute() 메서드 호출
		// execute() 메서드에서:
		//   - 데이터베이스에서 데이터 조회
		//   - request에 데이터 저장
		//   - JSP로 forward하여 화면 출력
		if(action != null) {
			action.execute(request, response);
		}
		// action이 null이면 아무 작업도 하지 않음 (빈 응답)
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
