package unit05;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; //톰캣 10버전 이후에는 javax 를 jakarta 로 변경

@WebServlet("/ParamServlet")
public class ParamServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        resp.setContentType("text/html;charset=UTF-8");

        String id = req.getParameter("id");
        int age = Integer.parseInt(req.getParameter("age"));

        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h2>ParamServlet</h2>");
        out.println("당신이 입력한 정보입니다.<br>");
        out.println("아이디: " + id + "<br>");
        out.println("나이: " + age + "<br>");
        out.println("<a href='javascript:history.back()'>다시</a>");
        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       
        resp.setContentType("text/html;charset=UTF-8");

        String id = req.getParameter("id");
        int age = Integer.parseInt(req.getParameter("age"));

        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h2>ParamServlet</h2>");
        out.println("당신이 입력한 정보입니다.<br>");
        out.println("아이디: " + id + "<br>");
        out.println("나이: " + age + "<br>");
        out.println("<a href='javascript:history.back()'>다시</a>");
        out.println("</body></html>");
    }
}
