package com.saeyan.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.saeyan.dto.MemberDAO;
import com.saeyan.dto.MemberVO;

/**
 * 로그인 처리 서블릿
 * 
 * 역할: 사용자의 로그인 요청을 처리하고 결과에 따라 적절한 페이지로 이동
 * 
 * URL 매핑: /login.do
 * 
 * 처리 흐름:
 * 1. GET 요청: 로그인 페이지 표시
 * 2. POST 요청: 로그인 인증 처리
 *    - 성공: 세션에 회원 정보 저장 후 메인 페이지로 이동
 *    - 실패: 에러 메시지와 함께 로그인 페이지로 다시 이동
 */
@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    /**
     * 기본 생성자
     */
    public LoginServlet() {
        
    }

	/**
	 * GET 요청 처리: 로그인 페이지를 표시
	 * 
	 * @param request HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @throws ServletException 서블릿 처리 중 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 * 
	 * 처리 내용:
	 * - 이전에 입력했던 아이디와 메시지 제거 (깨끗한 로그인 페이지 제공)
	 * - 로그인 JSP 페이지로 포워딩
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 처음 로그인 페이지 접속 시 이전 입력값 제거
		// 이렇게 하면 로그인 실패 후 다시 로그인 페이지로 돌아왔을 때
		// 이전에 입력했던 아이디가 자동으로 채워지지 않음
		request.removeAttribute("userid");
		request.removeAttribute("message");
		
		// 로그인 페이지 JSP로 포워딩
		request.getRequestDispatcher("/member/login.jsp")
			.forward(request, response);
	}

	/**
	 * POST 요청 처리: 로그인 인증 수행
	 * 
	 * @param request HTTP 요청 객체 (폼 데이터 포함)
	 * @param response HTTP 응답 객체
	 * @throws ServletException 서블릿 처리 중 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 * 
	 * 처리 과정:
	 * 1. 폼에서 전달받은 아이디와 비밀번호 추출
	 * 2. MemberDAO를 통해 데이터베이스에서 인증 확인
	 * 3. 인증 결과에 따라 처리:
	 *    - 성공(1): 세션에 회원 정보 저장 후 메인 페이지로 이동
	 *    - 실패(0): 에러 메시지와 함께 로그인 페이지로 다시 이동
	 *    - DB 오류(-1): 에러 메시지와 함께 로그인 페이지로 다시 이동
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 한글 인코딩 설정 (폼 데이터가 한글로 올 수 있으므로)
		request.setCharacterEncoding("UTF-8");
		
		// 폼(login.jsp)에서 전달받은 데이터 추출
		String userid = request.getParameter("userid");  // 아이디 파라미터
		String pwd = request.getParameter("pwd");        // 비밀번호 파라미터
		String url = "/member/login.jsp";                // 기본 이동 경로 (로그인 페이지)
		
		// DAO 객체 생성 및 로그인 검증
		// 싱글톤 패턴으로 구현되어 있어 getInstance()로 인스턴스 획득
		MemberDAO dao = MemberDAO.getInstance();
		
		// userCheck 메서드를 통해 아이디와 비밀번호 검증
		// 반환값: 1(성공), 0(실패), -1(DB 오류)
		int result = dao.userCheck(userid, pwd);
		
		if(result == 1) {
			// 로그인 성공 처리
			
			// 세션 객체 가져오기 (없으면 새로 생성)
			HttpSession session = request.getSession();
			
			// 세션에 아이디 저장 (간단한 로그인 확인용)
			session.setAttribute("userid", userid);
			
			// 전체 회원 정보를 데이터베이스에서 조회
			MemberVO mvo = dao.getMember(userid);
			
			// 세션에 완전한 회원 정보 저장
			// 이렇게 하면 다른 페이지에서도 회원 정보를 쉽게 사용할 수 있음
			session.setAttribute("loginUser", mvo);
			
			// 메인 페이지로 이동할 경로 설정
			url = "/member/main.jsp";

		} else if(result == 0) {
			// 로그인 실패 처리 (아이디 또는 비밀번호 불일치)
			
			// 에러 메시지를 request에 저장 (JSP에서 표시하기 위해)
			request.setAttribute("message", "아이디 또는 비밀번호가 맞지 않습니다.");
			
			// 입력했던 아이디를 다시 표시하기 위해 request에 저장
			// 이렇게 하면 사용자가 다시 입력할 필요 없음
			request.setAttribute("userid", userid);
		} else {
			// 데이터베이스 오류 처리
			
			// DB 연결 오류 메시지 저장
			request.setAttribute("message", "데이터베이스 연결 오류가 발생했습니다.");
			
			// 입력했던 아이디 유지
			request.setAttribute("userid", userid);
		}

		// 설정한 경로로 포워딩
		// 포워딩: 서버 내부에서 다른 페이지로 요청을 전달하는 방식
		RequestDispatcher dis = request.getRequestDispatcher(url);
		dis.forward(request, response);
	}

}
