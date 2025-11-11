package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // ★ 이 어노테이션을 사용합니다
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// ★ 이클립스의 'URL mapping' 설정과 동일합니다
@WebServlet("/hello") 
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // UTF-8 인코딩 설정
        resp.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h2>Hello from Servlet!</h2>");
        out.println("</body></html>");
    }
}