package unit01;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/add")
public class AdditionServlet01 extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int num1 = 20;
		int num2 = 10;
		int add = num1 + num2;
		
		// 1. request 객체에 데이터를 담는다.
		request.setAttribute("n1", num1);
		request.setAttribute("n2", num2);
		request.setAttribute("sum", add);
				
		// 2. JSP로 포워드한다.
		RequestDispatcher dis = request.getRequestDispatcher("addition2.jsp");
		dis.forward(request, response);
		
	}

}
