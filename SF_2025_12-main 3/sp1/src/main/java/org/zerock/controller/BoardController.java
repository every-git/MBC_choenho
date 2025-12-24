package org.zerock.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.dto.BoardDTO;
import org.zerock.dto.BoardListPaginDTO;
import org.zerock.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	//생성자 주입(DI) , @RequiredArgsConstructor 의해서
	private final BoardService boardService;
	
	/*
	//localhost:8080/board/ex1 -> /WEB-INF/ views / board / ex1.jsp
	@GetMapping("/ex1")
	void ex1() {
		
	}
	*/
	
	// localhost:8080/board/list
	// -> /WEB-INF/ views / board / list.jsp
	@GetMapping("/list")
	public void list(
			@RequestParam(name="page", defaultValue = "1") int page,
			@RequestParam(name="size", defaultValue = "10") int size,
			@RequestParam(name="types", required = false) String types,
			@RequestParam(name="keyword", required = false) String keyword,			
			Model model) {
		
		BoardListPaginDTO list = boardService.getList(page, size,types,keyword);
		
		log.info("---------------------------------------");
		log.info("types : " +  list.getKeyword());
		log.info("keyword : " + list.getTypes());		
		
		model.addAttribute("dto", list);
		
//		model.addAttribute("list", boardService.getList());

	}
	
	//등록 화면
	@GetMapping("/register")
	public void register() {
		log.info("board register");
	}
	
	//등록 처리
	@PostMapping("/register")
	public String registerPost(BoardDTO dto, RedirectAttributes rttr) {
		log.info("-------------------------------");
		log.info("board register post");
		
		//게시글 등록하면 등록된 번호를 반환
		Long  bno = boardService.register(dto);
		
		/* 
		 * 	1회용(1번 요청에만 유지되는) 데이터를 전달하는 방식
		 	redirect 이후에 단 한 번만 사용할 값을 저장할 때 사용
		 	URL 파라미터로 노출되지 않아서 보안상 안전함
		 	예) 글 작성 후 "글번호", "성공 메시지" 등을 다음 화면에 잠깐 보여줄 때 활용
		*/
		rttr.addFlashAttribute("result", bno);
		
		return "redirect:/board/list";
	}
	
	//단건 조회	localhost:8080/board/read/12 
	// db에서 1번 데이타 보여주세요
	// -> /WEB-INF/ views / board / read.jsp
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/read/{bno}")
	public String read(@PathVariable("bno") Long bno,
			@RequestParam(name="page", defaultValue = "1") int page,
			@RequestParam(name="size", defaultValue = "10") int size,
			@RequestParam(name="types", required = false) String types,
			@RequestParam(name="keyword", required = false) String keyword,
			Model model) {
		
		BoardDTO dto = boardService.read(bno);
		
		model.addAttribute("board", dto);
		model.addAttribute("page", page);
	    model.addAttribute("size", size);
	    model.addAttribute("types", types);
	    model.addAttribute("keyword", keyword);
		
		return "/board/read";
		
	}
	
	/*
	 * 수정 폼
	 * localhost:8080/board/modify/1 
	 */
	@GetMapping("/modify/{bno}")
	public String modifyGet(@PathVariable("bno") Long bno, Model model) {
		log.info("board modify get");
		
		BoardDTO dto = boardService.read(bno);
		
		// 현재 로그인한 사용자 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		
		// 작성자가 아니면 접근 거부
		if (!currentUsername.equals(dto.getWriter())) {
			log.warn("접근 거부: 작성자가 아닌 사용자(" + currentUsername + ")가 수정 시도");
			return "redirect:/sample/access-denied";
		}
		
		model.addAttribute("board", dto);
		
		return "board/modify";
	}
	
	@PreAuthorize("authentication.name == #dto.writer")
	@PostMapping("/modify")
	public String modifyPost(BoardDTO dto) {
		log.info("board modify post");
		
		boardService.modify(dto);
		
		return "redirect:/board/read/"+dto.getBno();
	}
	
	/*
	 * 삭제
	 * localhost:8080/board/remove 
	 */
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno,
			RedirectAttributes rttr) {
	
		log.info("board remove post : " + bno);
		
		boardService.remove(bno);
		
		rttr.addFlashAttribute("result", bno);
		
		return "redirect:/board/list";
	}
}
