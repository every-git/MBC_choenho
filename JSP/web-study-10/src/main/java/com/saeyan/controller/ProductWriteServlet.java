package com.saeyan.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;


@WebServlet("/productWrite.do")
public class ProductWriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public ProductWriteServlet() {
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/product/productWrite.jsp")
			.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		ServletContext context = getServletContext();
		System.out.println("context: " + context);
		
		String path = context.getRealPath("upload");
		System.out.println("path: " + path);
		
		String encType = "utf-8";
		
		int sizeLimit = 20*1024*1024; // 파일 용량 크기 20mb
		
		MultipartRequest multi = 
				new MultipartRequest(request, encType, sizeLimit, new DefaultFileRenamePolicy());
		
		String name = multi.getParameter("name");
		int price = Integer.parseInt(multi.getParameter("price"));
		
		//파일 업로드 ---getFilesystemName
		String pictureUrl = multi.getFilesystemName("pictureUrl");
		String description = multi.getParameter("description");
		
	}

}
