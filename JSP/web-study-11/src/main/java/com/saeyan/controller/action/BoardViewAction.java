package com.saeyan.controller.action;

import java.io.IOException;
import java.net.Authenticator.RequestorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

public class BoardViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1. num값 가져오기
		int num = Integer.parseInt(request.getParameter("num"));
		
		//2. DB에서 num값에 해당하는 게시글 조회
		BoardDAO dao = BoardDAO.getInstance();
		BoardVO vo = dao.selectOneByNum(num);

		//3. 조회수 증가
		dao.updateReadCount(num);

		//4. boardView에 값 전달하기
		request.setAttribute("board", vo);

		//5. boardView.jsp로 forward
		String url = "/board/boardView.jsp";
		
		request.getRequestDispatcher(url)
			.forward(request, response);

	}

}
