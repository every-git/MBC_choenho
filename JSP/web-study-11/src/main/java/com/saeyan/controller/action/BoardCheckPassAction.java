package com.saeyan.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saeyan.dao.BoardDAO;
import com.saeyan.dto.BoardVO;

public class BoardCheckPassAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = null;
		
		//1. 입력창 입력한 Password
		String numStr = request.getParameter("num");
		String pass = request.getParameter("pass");
		
		//2. DB에서 조회 
		BoardDAO dao = BoardDAO.getInstance();
		int num = Integer.parseInt(numStr); // String을 int로 변환
		BoardVO vo = dao.selectOneByNum(num);

		//3. 입력받은 것과 DB에 있는 pass 비교
		if(pass != null && pass.equals(vo.getPass())) {
			// 비밀번호 일치: checkSuccess.jsp로 forward하여 팝업 창에서 부모 창을 리다이렉트하고 팝업 닫기
			url = "/board/checkSuccess.jsp";
			request.setAttribute("num", numStr); // num 값 전달
		} else {
			// 비밀번호 불일치: 다시 비밀번호 입력 페이지로
			url = "/board/boardCheckPass.jsp";
			request.setAttribute("message", "비밀번호가 틀렸습니다.");
			request.setAttribute("num", numStr); // num 값 유지
			request.setAttribute("action", request.getParameter("action")); // action 값 유지
		}
		
		request.getRequestDispatcher(url)
			.forward(request, response);
		

	}

}
