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
 * 회원정보 수정 처리 서블릿
 * 
 * 역할: 로그인한 회원의 정보 수정 요청을 처리
 * 
 * URL 매핑: /memberUpdate.do
 * 
 * 처리 흐름:
 * 1. GET 요청: 회원정보 수정 페이지 표시 (기존 정보를 폼에 채워서 표시)
 * 2. POST 요청: 수정된 회원정보를 데이터베이스에 저장
 * 
 * 보안 기능:
 * - 로그인하지 않은 사용자는 접근 불가 (로그인 페이지로 리다이렉트)
 * - 세션의 userid와 파라미터의 userid가 다르면 세션의 userid 사용 (타인의 정보 수정 방지)
 */
@WebServlet("/memberUpdate.do")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    /**
     * 기본 생성자
     */
    public UpdateServlet() {
        
    }

	/**
	 * GET 요청 처리: 회원정보 수정 페이지를 표시
	 * 
	 * @param request HTTP 요청 객체
	 * @param response HTTP 응답 객체
	 * @throws ServletException 서블릿 처리 중 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 * 
	 * 처리 과정:
	 * 1. 세션에서 로그인 사용자 정보 확인 (로그인 체크)
	 * 2. userid 파라미터 확인 (없으면 세션의 userid 사용)
	 * 3. 보안 검증 (세션의 userid와 파라미터의 userid 일치 확인)
	 * 4. 데이터베이스에서 최신 회원 정보 조회
	 * 5. 조회한 정보를 request에 저장하고 수정 페이지로 포워딩
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 세션에서 로그인 사용자 정보 가져오기
		HttpSession session = request.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		
		// 로그인하지 않은 경우 로그인 페이지로 리다이렉트
		if(loginUser == null) {
			response.sendRedirect(request.getContextPath() + "/login.do");
			return;  // 여기서 처리 종료
		}
		
		// URL 파라미터에서 userid 가져오기
		String userid = request.getParameter("userid");
		
		// userid 파라미터가 없거나 빈 값이면 세션의 userid 사용
		// 이렇게 하면 URL에 userid를 명시하지 않아도 자신의 정보를 수정할 수 있음
		if(userid == null || userid.trim().equals("")) {
			userid = loginUser.getUserid();
		}
		
		// 보안 검증: 세션의 userid와 파라미터의 userid가 다르면 세션의 userid 사용
		// 이렇게 하면 타인의 정보를 수정하려는 시도를 방지할 수 있음
		if(!userid.equals(loginUser.getUserid())) {
			userid = loginUser.getUserid();
		}

		// 반드시 DAO를 통해 DB에서 최신 정보 가져오기
		// 세션에 저장된 정보는 오래된 정보일 수 있으므로 항상 DB에서 조회
		MemberDAO dao = MemberDAO.getInstance();
		MemberVO mvo = dao.getMember(userid);
		
		// DB에서 정보를 가져오지 못한 경우 에러 처리
		if(mvo == null) {
			request.setAttribute("message", "회원 정보를 불러올 수 없습니다. 다시 시도해주세요.");
			request.getRequestDispatcher("/member/main.jsp").forward(request, response);
			return;  // 여기서 처리 종료
		}
		
		// DB에서 가져온 최신 정보를 request에 설정
		// 이 정보는 JSP에서 EL 표현식이나 scriptlet으로 접근 가능
		request.setAttribute("mvo", mvo);
		
		// 회원정보 수정 페이지로 포워딩
		request.getRequestDispatcher("/member/memberUpdate.jsp")
			.forward(request, response);
	}

	/**
	 * POST 요청 처리: 수정된 회원정보를 데이터베이스에 저장
	 * 
	 * @param request HTTP 요청 객체 (폼 데이터 포함)
	 * @param response HTTP 응답 객체
	 * @throws ServletException 서블릿 처리 중 오류 발생 시
	 * @throws IOException 입출력 오류 발생 시
	 * 
	 * 처리 과정:
	 * 1. 폼에서 전달받은 수정된 데이터 추출
	 * 2. null 체크 및 기본값 설정
	 * 3. MemberVO 객체 생성 및 데이터 설정
	 * 4. MemberDAO를 통해 데이터베이스에 업데이트
	 * 5. 성공 시 세션의 loginUser도 업데이트
	 * 6. 결과에 따라 적절한 페이지로 이동
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 한글 인코딩 설정
		request.setCharacterEncoding("UTF-8");
		
		// 폼에서 전달받은 데이터 추출
		String userid = request.getParameter("userid");      // 아이디 (수정 불가, WHERE 조건에 사용)
		String pwd = request.getParameter("pwd");            // 비밀번호
		String email = request.getParameter("email");        // 이메일
		String phone = request.getParameter("phone");       // 전화번호
		String adminStr = request.getParameter("admin");     // 등급 (문자열로 받음)
		
		// null 체크 및 기본값 설정
		if(userid == null) userid = "";
		if(pwd == null) pwd = "";
		if(email == null) email = "";
		if(phone == null) phone = "";
		if(adminStr == null || adminStr.trim().equals("")) {
			adminStr = "0";  // 기본값: 일반회원
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
		mvo.setUserid(userid);   // WHERE 조건에 사용
		mvo.setPwd(pwd);         // 수정할 비밀번호
		mvo.setEmail(email);     // 수정할 이메일
		mvo.setPhone(phone);     // 수정할 전화번호
		mvo.setAdmin(admin);     // 수정할 등급
		
		// DAO 객체 생성
		MemberDAO dao = MemberDAO.getInstance();
		
		// 데이터베이스에 회원정보 업데이트
		// 반환값: 1(성공), 0(실패), -1(DB 오류)
		int result = dao.updateMember(mvo);
		
		RequestDispatcher dis = null;
		
		if(result == 1) {
			// 회원정보 수정 성공
			
			// 세션 정보도 업데이트 (최신 정보 유지)
			HttpSession session = request.getSession();
			MemberVO updatedMvo = dao.getMember(userid);  // DB에서 최신 정보 다시 조회
			session.setAttribute("loginUser", updatedMvo);  // 세션 업데이트
			
			// 성공 메시지 설정
			request.setAttribute("message", "회원정보가 수정되었습니다.");
			
			// 메인 페이지로 이동
			dis = request.getRequestDispatcher("/member/main.jsp");
		} else {
			// 회원정보 수정 실패
			
			// 기존 정보를 다시 가져와서 폼에 표시
			MemberVO existingMvo = dao.getMember(userid);
			request.setAttribute("mvo", existingMvo);
			
			// 실패 메시지 설정
			request.setAttribute("message", "회원정보 수정에 실패했습니다.");
			
			// 회원정보 수정 페이지로 다시 이동
			dis = request.getRequestDispatcher("/member/memberUpdate.jsp");
		}
		
		// 설정한 경로로 포워딩
		dis.forward(request, response);
	}

}
