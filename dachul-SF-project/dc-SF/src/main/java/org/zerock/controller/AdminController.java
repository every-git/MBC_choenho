package org.zerock.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.dto.BoardDTO;
import org.zerock.dto.MemberDTO;
import org.zerock.service.BoardService;
import org.zerock.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/admin")
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final MemberService memberService;
    private final BoardService boardService;

    // 관리자 메인 페이지
    @GetMapping("/main")
    public void main() {
        log.info("admin main...");
    }

    // 회원 목록 페이지
    @GetMapping("/member/list")
    public void memberList(Model model) {
        log.info("admin member list...");
        List<MemberDTO> list = memberService.getAllMembers();
        model.addAttribute("memberList", list);
    }

    // 회원 상세 페이지
    @GetMapping("/member/detail")
    public void memberDetail(@RequestParam("id") String id, Model model) {
        log.info("admin member detail... id: " + id);
        MemberDTO dto = memberService.getMember(id);
        model.addAttribute("member", dto);
    }

    // 회원 삭제 (강제 탈퇴)
    @GetMapping("/member/delete")
    public String deleteMember(@RequestParam("id") String id) {
        log.info("admin delete member... id: " + id);
        memberService.removeMember(id);
        return "redirect:/admin/member/list";
    }

    // 게시판 관리 (목록)
    @GetMapping("/board/list")
    public void boardList(Model model) {
        log.info("admin board list...");
        List<BoardDTO> list = boardService.getAllBoards();
        model.addAttribute("boardList", list);
    }

    // 게시글 삭제
    @GetMapping("/board/delete")
    public String deleteBoard(@RequestParam("seq") int seq) {
        log.info("admin delete board... seq: " + seq);
        boardService.removeBoard(seq);
        return "redirect:/admin/board/list";
    }
}
