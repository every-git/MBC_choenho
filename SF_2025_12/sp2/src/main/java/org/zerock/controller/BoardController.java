package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.zerock.service.BoardService;
import java.util.List;
import org.zerock.dto.BoardDTO;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    // 생성자 주입
    private final BoardService boardService;
	/*
    //localhost:8080/board/ex1 -> Method:Get
    //-> /WEB-INF/views/board/ex1.jsp
    @GetMapping("/ex1")
    public void ex1() {
        
    }*/
    //localhost:8080/board/list -> Method:Get
    //-> /WEB-INF/views/board/list.jsp
    @GetMapping("/list")
    public void list(Model model) {
        log.info("board list");

        List<BoardDTO> boardDtoList = boardService.getList();
        log.info("list: {}", boardDtoList);
        
        // Model에 데이터 추가 (JSP에서 ${list}로 접근 가능)
        model.addAttribute("list", boardDtoList);
    }

    //localhost:8080/board/register -> Method:Get
    //-> /WEB-INF/views/board/register.jsp
    @GetMapping("/register")
    public void register() {
        log.info("board register");
    }

    //localhost:8080/board/register -> Method:Post
    //-> redirect -> localhost:8080/board/list
    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes rttr) {
        log.info("--------------------------------");
        log.info("board register Post");
        log.info("--------------------------------");
        
        Long bno = boardService.register(dto);
        
        rttr.addFlashAttribute("result", bno);
        
        return "redirect:/board/list";
    }
    
    // 단건 조회
    //localhost:8080/board/read/1 -> Method:Get
    //-> /WEB-INF/views/board/read.jsp
    @GetMapping("/read/{bno}")
    public String read(@PathVariable("bno") Long bno, Model model) {
    	
    	log.info("board read");
    	BoardDTO dto = boardService.read(bno);
    	model.addAttribute("board", dto);
    	return "/board/read";
    }

    // 수정
    //localhost:8080/board/modify/1 -> Method:Get
    //-> /WEB-INF/views/board/modify.jsp
    @GetMapping("/modify/{bno}")
    public String modify(@PathVariable("bno") Long bno, Model model) {
    	log.info("board modify");
    	BoardDTO dto = boardService.read(bno);
    	model.addAttribute("board", dto);
    	return "/board/modify";
    }


    @PostMapping("/modify")
    public String modifyPost(BoardDTO dto, RedirectAttributes rttr) {
    	log.info("board modify Post");
    	boardService.modify(dto);
    	rttr.addFlashAttribute("result", dto.getBno());
    	return "redirect:/board/read/" + dto.getBno();
    }
    
    // 삭제
    //localhost:8080/board/remove -> Method:Post
    //-> redirect -> localhost:8080/board/list
    @PostMapping("/remove")
    public String remove(@RequestParam("bno") Long bno, RedirectAttributes rttr) {
    	log.info("board remove");
    	boardService.remove(bno);
    	rttr.addFlashAttribute("result", bno);
    	return "redirect:/board/list";
    }


}



