package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.zerock.service.MemberService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.zerock.dto.MemberDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

	private final MemberService service;


    // localhost:8080/member/list
    // -> /WEB-INF/views/member/list.jsp
	@GetMapping("/list")
	public void list(Model model) {
		List<MemberDTO> list = service.getList();
		log.info("Member list size: " + list.size());
		model.addAttribute("memberList", list);
	} // end of list

	@GetMapping("/register")
	public void register() {
		log.info("Member register");
	} // end of register

    @PostMapping("/register")
    public String registerPost(MemberDTO dto) {
        log.info("Member register: " + dto);
        service.insert(dto);
        return "redirect:/member/list";
    } // end of registerPost

    @GetMapping("/read/{mno}")
    public String read(@PathVariable("mno") int mno, Model model) {
        log.info("Member read: " + mno);
        MemberDTO member = service.memberById(mno);
        model.addAttribute("member", member);
        return "member/read";
    } // end of read

    @PostMapping("/modify")
    public String modify(MemberDTO dto) {
        log.info("Member modify: " + dto);
        service.update(dto);
        return "redirect:/member/list";
    } // end of modify

    @PostMapping("/delete")
    public String delete(int mno) {
        log.info("Member delete: " + mno);
        service.delete(mno);
        return "redirect:/member/list";
    } // end of delete
}
