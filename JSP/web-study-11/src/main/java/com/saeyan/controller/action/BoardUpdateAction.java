package com.saeyan.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

public class BoardUpdateAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		
		int num = Integer.parseInt(request.getParameter("num"));
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String email = request.getParameter("email");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardVO vo = new BoardVO();
		vo.setNum(num);
		vo.setName(name);
		vo.setPass(pass);
		vo.setEmail(email);
		vo.setTitle(title);
		vo.setContent(content);

		BoardDAO dao = BoardDAO.getInstance();
		dao.updateBoard(vo);

		new BoardListAction().execute(request, response);
	}

}
