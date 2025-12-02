package com.saeyan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.saeyan.dto.BoardVO;
import com.saeyan.service.BoardService;

import lombok.extern.log4j.Log4j2;




@Controller
@Log4j2
public class BoardServlet {

	@Autowired
	private BoardService boardService;

	@GetMapping("/board_list")
	public String board_list(Model model) {
		
		List<BoardVO> list = boardService.selectAllBoards();
		model.addAttribute("boardList", list);

		return "board/boardList";
	}
	
	@GetMapping("/board_write_form")
	public String board_write_form() {
		return "board/boardWrite";
	}
	
	@PostMapping("board_write")
	public String board_write(BoardVO vo) {

		boardService.insertBoard(vo);

		return "redirect:/board_list";
	}

	@GetMapping("/board_view")
	public String board_view(@RequestParam("num") int num,
			Model model) {

		BoardVO vo = boardService.selectOneByNum(num);
		model.addAttribute("board", vo);
		
		return "board/boardView";
	}

	@GetMapping("/board_check_pass_form")
	public String board_check_pass_form() {

		return "board/boardCheckPass";
	}

	@PostMapping("/board_check_pass")
	public String board_check_pass(@RequestParam("num") int num,
				@RequestParam("pass") String pass, Model model) {

		String url = null;
		BoardVO vo = boardService.selectOneByNum(num);

		 //3. 입력받은 것과 DB있는 pass 비교
		 if(pass.equals(vo.getPass())) {
			url = "board/checkSuccess";
		 }else {
			url = "board/boardCheckPass";
			model.addAttribute("message", "비밀번호가 틀렸습니다.");
		 }
		return url;
	}

	@GetMapping("/board_delete")
	public String board_delete(@RequestParam("num") int num) {
		boardService.deleteBoard(num);
		return "redirect:/board_list";
	}


	@GetMapping("/board_update_form")
	public String board_update_form(@RequestParam("num") int num, Model model) {
		BoardVO vo = boardService.selectOneByNum(num);
		model.addAttribute("board", vo);

		return "board/boardUpdate";
	}

	@PostMapping("/board_update")
	public String board_update(BoardVO vo) {
		boardService.updateBoard(vo);
		return "redirect:/board_list";
	}
}
