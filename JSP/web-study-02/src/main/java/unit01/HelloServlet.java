package unit01;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=utf-8");
		
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		
		out.println("<h1>안녕하세요!!!<h1>"); //한글은 깨짐. 단,charset=utf-8 넣으면 됨. 
		out.println("<h1>Hello World!!!!<h1>");
		out.println(name + "님, 안녕하세요!!");
		out.println("나이는 " + age + "세 입니다!!");
		
		out.println("</body>");
		out.println("</html>");
		
		out.close();
	}

}
