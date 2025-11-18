package com.saeyan.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.ProductDAO;
import com.saeyan.dto.ProductVO;


@WebServlet("/productDelete.do")
public class ProductDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProductDeleteServlet() {
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1. code 값 가져오기. 스트링으로 로 가져오기.
		String code = request.getParameter("code");

		//2. ProductDAO 통해서 code값 삭제
		ProductDAO pdao = ProductDAO.getInstance();
		ProductVO vo = pdao.selectProductByCode(code);
		//3. request.setAttribute()저장
		request.setAttribute("product", vo);

		//4. forward(product/productDelete.jsp)페이지로 이동
		request.getRequestDispatcher("/product/productDelete.jsp")
		.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1. code 값 가져오기. 스트링으로 로 가져오기.
		Integer code = Integer.parseInt(request.getParameter("code"));
		//2. ProductDAO 통해서 code값 삭제
		ProductDAO pdao = ProductDAO.getInstance();
		pdao.deleteProduct(code);
		//3. redirect로 화면 전환
		response.sendRedirect(request.getContextPath() + "/productList.do");
	}

}
