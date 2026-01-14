package com.example.jpa.controller;

import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.jpa.domain.Member;
import com.example.jpa.service.MemberService;
import org.springframework.ui.Model;



@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public void getList(@PageableDefault(size=5, sort = "memberId",
            direction = Sort.Direction.DESC) Pageable pageable, Model model){

        Page<Member> memberPage = memberService.findByAll(pageable);

        log.info("controller pageable : ");
        log.info(pageable);

        //1. 실제 데이터 리스트
        model.addAttribute("memberList", memberPage.getContent());

        log.info("memberPage.hasPrevious : " + memberPage.hasPrevious());
        log.info("memberPage.hasNext : " + memberPage.hasNext());
        log.info("memberPage.getNumber : " + memberPage.getNumber());

        //2. 페이지 정보(HTML에서 버튼 만들 때 사용)
        model.addAttribute("page", memberPage);

    }

    // 페이징 처리 없는 전체 조회 메서드 (중복 매핑으로 인해 주석 처리)
    /*
    @GetMapping("/list")
    public void getList(Model model) {
        List<Member> memberList = memberService.findByAll();
        model.addAttribute("memberList", memberList);
    }
    */

    @GetMapping("/new")
    public void getNew() {
    }

    @PostMapping("/new")
    public String postNew(Member member) {
        memberService.insert(member);
        return "redirect:/members/list";
    }

    @GetMapping("/delete/{memberId}")
    public String delete(@PathVariable("memberId") int memberId) {
        memberService.delete(memberId);
        return "redirect:/members/list";
    }

    @GetMapping("/edit/{memberId}")
    public String edit(@PathVariable("memberId") int memberId, Model model) {
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "members/edit";
    }

    @PostMapping("/edit/{memberId}")
    public String editPost(Member member) {
        memberService.update(member);
        return "redirect:/members/list";
    }
}
