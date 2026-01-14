package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

import com.example.demo.service.MemberService;
import com.example.demo.domain.MemberDTO;

import java.util.List;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  // 회원 목록
  @GetMapping("/list")
  public String list(Model model) {
    log.info("list.........");
    List<MemberDTO> getList = memberService.getList();
    model.addAttribute("list", getList);
    return "member/list";
  }

  // 등록 폼
  @GetMapping("/register")
  public String registerForm() {
    log.info("register form.........");
    return "member/register";
  }

  // 등록 처리
  @PostMapping("/register")
  public String register(MemberDTO memberDTO, RedirectAttributes rttr) {
    log.info("register.........");
    memberService.register(memberDTO);
    rttr.addFlashAttribute("message", "회원이 등록되었습니다.");
    return "redirect:/member/list";
  }

  // 상세 조회
  @GetMapping("/view/{memberId}")
  public String view(@PathVariable int memberId, Model model) {
    log.info("view......... memberId: {}", memberId);
    MemberDTO member = memberService.getById(memberId);
    model.addAttribute("member", member);
    return "member/view";
  }

  // 수정 폼
  @GetMapping("/modify/{memberId}")
  public String modifyForm(@PathVariable int memberId, Model model) {
    log.info("modify form......... memberId: {}", memberId);
    MemberDTO member = memberService.getById(memberId);
    model.addAttribute("member", member);
    return "member/modify";
  }

  // 수정 처리
  @PostMapping("/modify")
  public String modify(MemberDTO memberDTO, RedirectAttributes rttr) {
    log.info("modify.........");
    memberService.modify(memberDTO);
    rttr.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
    return "redirect:/member/view/" + memberDTO.getMemberId();
  }

  // 삭제 처리
  @PostMapping("/delete/{memberId}")
  public String delete(@PathVariable int memberId, RedirectAttributes rttr) {
    log.info("delete......... memberId: {}", memberId);
    memberService.remove(memberId);
    rttr.addFlashAttribute("message", "회원이 삭제되었습니다.");
    return "redirect:/member/list";
  }
}