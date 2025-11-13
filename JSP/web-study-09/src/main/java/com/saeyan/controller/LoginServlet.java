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


@WebServlet("/login.do")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public LoginServlet() {
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.getRequestDispatcher("member/login.jsp")
			.forward(request, response);
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		// 폼에서 전달받은 데이터
		String userid = request.getParameter("userid");
		String pwd = request.getParameter("pwd");
		
		// DAO 객체 생성 및 로그인 검증
		MemberDAO dao = MemberDAO.getInstance();
		int result = dao.userCheck(userid, pwd);
		
		if(result == 1) {
			// 로그인 성공
			System.out.println("로그인 성공! 직접 HTML 출력");
			HttpSession session = request.getSession();
			session.setAttribute("userid", userid);
			
			// main.jsp가 서버에 배포되지 않았으므로 직접 HTML 출력
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().println("<!DOCTYPE html>");
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");
			response.getWriter().println("<meta charset='UTF-8'>");
			response.getWriter().println("<title>로그인 성공</title>");
			response.getWriter().println("</head>");
			response.getWriter().println("<body>");
			response.getWriter().println("<h1>🎉 로그인 성공!</h1>");
			response.getWriter().println("<h2>환영합니다! " + userid + "님</h2>");
			response.getWriter().println("<p>로그인에 성공했습니다.</p>");
			response.getWriter().println("<hr>");
			response.getWriter().println("<p>");
			response.getWriter().println("<a href='" + request.getContextPath() + "/login.do'>로그인 페이지로 돌아가기</a> | ");
			response.getWriter().println("<a href='" + request.getContextPath() + "/logout.do'>로그아웃</a>");
			response.getWriter().println("</p>");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");
		} else if(result == 0) {
			// 로그인 실패 (아이디 또는 비밀번호 불일치)
			System.out.println("로그인 실패: 아이디 또는 비밀번호 불일치");
			request.setAttribute("message", "아이디 또는 비밀번호가 맞지 않습니다.");
			request.getRequestDispatcher("member/login.jsp").forward(request, response);
		} else {
			// DB 오류
			System.out.println("DB 연결 오류");
			request.setAttribute("message", "데이터베이스 연결 오류가 발생했습니다.");
			request.getRequestDispatcher("member/login.jsp").forward(request, response);
		}
	}

}
