package com.saeyan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 로그아웃 처리 서블릿
 * 
 * 역할: 사용자의 로그아웃 요청을 처리하고 세션을 무효화
 * 
 * URL 매핑: /logout.do
 * 
 * 처리 흐름:
 * 1. 세션 무효화 (모든 세션 데이터 삭제)
 * 2. 로그인 페이지로 리다이렉트
 * 
 * 보안 기능:
 * - 세션을 완전히 무효화하여 로그인 정보가 남지 않도록 함
 */
@WebServlet("/logout.do")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * GET 요청 처리: 로그아웃 수행
     * 
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @throws ServletException 서블릿 처리 중 오류 발생 시
     * @throws IOException 입출력 오류 발생 시
     * 
     * 처리 과정:
     * 1. 현재 세션 가져오기
     * 2. 세션 무효화 (모든 세션 데이터 삭제)
     * 3. 로그인 페이지로 리다이렉트
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 현재 세션 가져오기
        HttpSession session = request.getSession();
        
        // 세션 무효화
        // 이 메서드를 호출하면 세션에 저장된 모든 데이터(userid, loginUser 등)가 삭제됨
        // 이후에는 세션을 사용할 수 없게 됨
        session.invalidate();
        
        // 로그인 페이지로 리다이렉트 (절대 경로 사용)
        // 리다이렉트: 브라우저에게 다른 URL로 이동하라고 지시하는 방식
        // 포워딩과 달리 브라우저의 URL이 변경됨
        response.sendRedirect(request.getContextPath() + "/login.do");
    }

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
