package org.zerock.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.dto.BoardDTO;
import org.zerock.dto.BoardListPaginDTO;
import org.zerock.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 게시판 컨트롤러
 */
@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 목록 (페이징)
     */
    @GetMapping("/list")
    public String list(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        log.info("board list... page: {}, size: {}", page, size);
        BoardListPaginDTO paginDTO = boardService.getBoardsWithPaging(page, size);
        model.addAttribute("dto", paginDTO);
        return "board/list";
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/view")
    public String view(@RequestParam("seq") int seq, Model model) {
        log.info("board view... seq: {}", seq);
        BoardDTO board = boardService.getBoard(seq);
        model.addAttribute("board", board);
        return "board/view";
    }

    /**
     * 게시글 작성 폼
     */
    @GetMapping("/write")
    public String writeForm() {
        log.info("board write form...");
        return "board/write";
    }

    /**
     * 게시글 작성 처리
     */
    @PostMapping("/write")
    public String write(BoardDTO dto, Principal principal, RedirectAttributes rttr) {
        log.info("board write... dto: {}", dto);

        // 로그인한 사용자를 작성자로 설정
        if (principal != null) {
            dto.setWriter(principal.getName());
        }

        boardService.addBoard(dto);
        rttr.addFlashAttribute("msg", "게시글이 등록되었습니다.");
        return "redirect:/board/list";
    }

    /**
     * 게시글 수정 폼
     */
    @GetMapping("/update")
    public String updateForm(@RequestParam("seq") int seq, Model model) {
        log.info("board update form... seq: {}", seq);
        BoardDTO board = boardService.getBoard(seq);
        model.addAttribute("board", board);
        return "board/update";
    }

    /**
     * 게시글 수정 처리
     */
    @PostMapping("/update")
    public String update(BoardDTO dto, RedirectAttributes rttr) {
        log.info("board update... dto: {}", dto);
        boardService.modifyBoard(dto);
        rttr.addFlashAttribute("msg", "게시글이 수정되었습니다.");
        return "redirect:/board/view?seq=" + dto.getSeq();
    }

    /**
     * 게시글 삭제
     */
    @GetMapping("/delete")
    public String delete(@RequestParam("seq") int seq, RedirectAttributes rttr) {
        log.info("board delete... seq: {}", seq);
        boardService.removeBoard(seq);
        rttr.addFlashAttribute("msg", "게시글이 삭제되었습니다.");
        return "redirect:/board/list";
    }
}
