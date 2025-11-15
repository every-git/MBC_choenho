package com.saeyan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dto.MemberDAO;

/**
 * 아이디 중복 체크 서블릿
 * 
 * 역할: 회원가입 시 입력한 아이디가 이미 사용 중인지 확인
 * 
 * URL 매핑: /idCheck.do
 * 
 * 처리 흐름:
 * 1. userid 파라미터 받기
 * 2. 파라미터 유효성 검증
 * 3. MemberDAO를 통해 데이터베이스에서 중복 확인
 * 4. 결과를 request에 저장하고 결과 페이지로 포워딩
 * 
 * 사용 예시:
 * - 회원가입 페이지에서 "중복 체크" 버튼 클릭 시 팝업으로 호출됨
 * - 결과: 1(중복), 0(사용 가능), -1(DB 오류)
 */
@WebServlet("/idCheck.do")
public class IdCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * 기본 생성자
     */
    public IdCheckServlet() {
        
    }

	/**
	 * GET 요청 처리: 아이디 중복 체크 수행
	 * 
	 * @param request HTTP 요청 객체 (userid 파라미터 포함)
	 * @param response HTTP 응답 객체
	 * @throws ServletException 서블릿 처리 중 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 * 
	 * 처리 과정:
	 * 1. 요청 인코딩 설정 (한글 처리)
	 * 2. userid 파라미터 추출
	 * 3. 파라미터 유효성 검증 (null 또는 빈 문자열 체크)
	 * 4. MemberDAO를 통해 중복 확인
	 * 5. 결과를 request에 저장하고 결과 페이지로 포워딩
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 한글 인코딩 설정
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		// URL 파라미터에서 userid 추출
		// 예: /idCheck.do?userid=test123
		String userid = request.getParameter("userid");
		
		// userid가 null이거나 비어있으면 에러 처리
		// 중복 체크를 하려면 아이디가 필요하므로
		if(userid == null || userid.trim().equals("")) {
			request.setAttribute("message", "아이디를 입력해주세요.");
			request.getRequestDispatcher("/member/idCheck.jsp")
				.forward(request, response);
			return;  // 여기서 처리 종료
		}

		// 싱글톤 패턴으로 DAO 객체 생성
		MemberDAO dao = MemberDAO.getInstance();

		// 아이디 중복 체크 수행
		// 반환값: 1(중복), 0(사용 가능), -1(DB 오류)
		int result = dao.confirmID(userid);

		// 결과를 request에 저장
		// JSP에서 이 값들을 사용하여 결과를 표시함
		request.setAttribute("userid", userid);    // 입력한 아이디
		request.setAttribute("result", result);    // 중복 체크 결과

		// 결과 페이지로 포워딩
		// idCheck.jsp에서 result 값에 따라 적절한 메시지를 표시함
		request.getRequestDispatcher("/member/idCheck.jsp")
			.forward(request, response);
	}

	/**
	 * POST 요청 처리: 현재는 사용하지 않음
	 * 
	 * @param request HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @throws ServletException 서블릿 처리 중 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 현재는 POST 요청을 처리하지 않음
		// 필요시 doGet과 동일한 로직을 구현할 수 있음
	}

}
