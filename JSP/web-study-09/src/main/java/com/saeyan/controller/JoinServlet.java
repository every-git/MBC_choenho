package com.saeyan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.saeyan.dto.MemberDAO;
import com.saeyan.dto.MemberVO;

/**
 * 회원가입 처리 서블릿
 * 
 * 역할: 새로운 회원의 가입 요청을 처리하고 데이터베이스에 저장
 * 
 * URL 매핑: /join.do
 * 
 * 처리 흐름:
 * 1. GET 요청: 회원가입 페이지 표시
 * 2. POST 요청: 회원가입 처리
 *    - 아이디 중복 체크 확인
 *    - 입력값 검증
 *    - 데이터베이스에 회원 정보 저장
 *    - 성공 시 로그인 페이지로 이동, 실패 시 회원가입 페이지에 에러 메시지 표시
 */
@WebServlet("/join.do")
public class JoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    /**
     * 기본 생성자
     */
    public JoinServlet() {
        
    }

	/**
	 * GET 요청 처리: 회원가입 페이지를 표시
	 * 
	 * @param request HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @throws ServletException 서블릿 처리 중 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 * 
	 * 처리 내용:
	 * - 회원가입 JSP 페이지로 포워딩
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 회원가입 페이지 JSP로 포워딩
		request.getRequestDispatcher("/member/join.jsp")
			.forward(request, response);
	}

	/**
	 * POST 요청 처리: 회원가입 데이터를 처리하고 데이터베이스에 저장
	 * 
	 * @param request HTTP 요청 객체 (폼 데이터 포함)
	 * @param response HTTP 응답 객체
	 * @throws ServletException 서블릿 처리 중 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 * 
	 * 처리 과정:
	 * 1. 폼에서 전달받은 모든 데이터 추출
	 * 2. 아이디 중복 체크 여부 확인 (reid 파라미터 확인)
	 * 3. 입력값 검증 (이름 길이 등)
	 * 4. MemberVO 객체 생성 및 데이터 설정
	 * 5. MemberDAO를 통해 데이터베이스에 저장
	 * 6. 결과에 따라 적절한 페이지로 이동
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 한글 인코딩 설정
		request.setCharacterEncoding("UTF-8");
		
		// 폼에서 전달받은 데이터 추출
		String name = request.getParameter("name");           // 이름
		String userid = request.getParameter("userid");      // 아이디
		String pwd = request.getParameter("pwd");             // 비밀번호
		String email = request.getParameter("email");        // 이메일
		String phone = request.getParameter("phone");        // 전화번호
		String adminStr = request.getParameter("admin");     // 등급 (문자열로 받음)
		String reid = request.getParameter("reid");          // 중복 체크 확인용 아이디
		
		// 아이디 중복 체크 확인
		// reid는 중복 체크 팝업에서 "사용" 버튼을 클릭했을 때 설정됨
		// reid가 없거나 userid와 다르면 중복 체크를 하지 않은 것으로 간주
		if(reid == null || reid.trim().equals("") || !reid.equals(userid)) {
			request.setAttribute("message", "아이디 중복 체크를 해주세요.");
			request.getRequestDispatcher("/member/join.jsp").forward(request, response);
			return;  // 여기서 처리 종료
		}
		
		// null 체크 및 기본값 설정
		// 폼에서 값이 전달되지 않았을 경우를 대비한 안전장치
		if(name == null) name = "";
		if(userid == null) userid = "";
		if(pwd == null) pwd = "";
		if(email == null) email = "";
		if(phone == null) phone = "";
		if(adminStr == null || adminStr.trim().equals("")) {
			adminStr = "0";  // 기본값: 일반회원
		}
		
		// 이름 길이 검증 (DB 컬럼 크기: 10바이트)
		// 한글은 UTF-8에서 3바이트씩 차지하므로 한글 3글자 정도가 최대
		if(name.length() > 10) {
			request.setAttribute("message", "이름은 10자 이하여야 합니다. (현재: " + name.length() + "자)");
			request.getRequestDispatcher("/member/join.jsp").forward(request, response);
			return;  // 여기서 처리 종료
		}
		
		// 등급 문자열을 정수로 변환
		int admin = 0;
		try {
			admin = Integer.parseInt(adminStr);
		} catch(NumberFormatException e) {
			admin = 0;  // 변환 실패 시 기본값: 일반회원
		}

		// MemberVO 객체 생성 및 데이터 설정
		MemberVO mvo = new MemberVO();
		mvo.setName(name);
		mvo.setUserid(userid);
		mvo.setPwd(pwd);
		mvo.setEmail(email);
		mvo.setPhone(phone);
		mvo.setAdmin(admin);
		
		// DAO 객체 생성
		MemberDAO dao = MemberDAO.getInstance();
		
		// 데이터베이스에 회원 정보 저장
		// 반환값: 1(성공), 0(실패), -1(DB 오류), -2(중복 아이디)
		int result = dao.insertMember(mvo);

		// 결과에 따라 이동할 경로 설정
		String url = "/member/join.jsp";  // 기본값: 회원가입 페이지
		
		if(result == 1) {
			// 회원 가입 성공
			request.setAttribute("message", "회원 가입이 완료되었습니다. 로그인해주세요.");
			url = "/login.do";  // 로그인 페이지로 이동
		} else if(result == -2) {
			// 중복 아이디 오류 (제약 조건 위반)
			request.setAttribute("message", "이미 존재하는 아이디입니다. 다른 아이디를 사용해주세요.");
			request.setAttribute("userid", userid);  // 입력했던 아이디 유지
		} else if(result == 0) {
			// 회원 가입 실패 (알 수 없는 이유)
			request.setAttribute("message", "회원 가입에 실패했습니다. 다시 시도해주세요.");
		} else {
			// 데이터베이스 오류
			request.setAttribute("message", "데이터베이스 오류가 발생했습니다. 관리자에게 문의해주세요.");
		}

		// 설정한 경로로 포워딩
		request.getRequestDispatcher(url)
			.forward(request, response);
	}

}
