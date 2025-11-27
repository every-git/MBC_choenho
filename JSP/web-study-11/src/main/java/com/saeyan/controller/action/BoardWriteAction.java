package com.saeyan.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dto.BoardVO;
import com.saeyan.dao.BoardDAO;

public class BoardWriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1. 값 가져오기
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String email = request.getParameter("email");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		BoardVO vo = new BoardVO();
		vo.setName(name);
		vo.setPass(pass);
		vo.setEmail(email);
		vo.setTitle(title);
		vo.setContent(content);

		//2. DB 저장하는 단계...DAO 통해서
		BoardDAO dao = BoardDAO.getInstance();
		dao.insertBoard(vo);
		
		//3. 목록 페이지로 이동
		// 절대 경로로 변경: 상대 경로는 현재 경로 기준으로 해석되어 오류 발생 가능
		String url = request.getContextPath() + "/BoardServlet?command=board_list";
		response.sendRedirect(url);

	}

}
