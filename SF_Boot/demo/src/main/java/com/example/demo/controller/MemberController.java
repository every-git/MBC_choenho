package com.example.demo.controller;

import com.example.demo.domain.MemberDTO;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //list
    //http://localhost:8080/member/list
    @GetMapping("/list")
    public void list(Model model){
        log.info("List...........");
        List<MemberDTO> getList = memberService.getList();
        model.addAttribute("list", getList);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("data", "world!");
    }

    //update form
    //http://localhost:8080/member/update/1
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable int id, Model model){
        log.info("Update Form - id: " + id);
        MemberDTO member = memberService.getById(id);
        model.addAttribute("member", member);
        return "member/updateForm";
    }

    //update
    //http://localhost:8080/member/update
    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/member/list";
    }
    
    //insert form
    //http://localhost:8080/member/insert
    @GetMapping("/insert")
    public String insertForm(Model model){
        log.info("Insert Form...........");
        model.addAttribute("member", new MemberDTO());
        return "member/insertForm";
    }

    //insert
    //http://localhost:8080/member/insert
    @PostMapping("/insert")
    public String insert(@ModelAttribute MemberDTO memberDTO){
        log.info("Insert - memberDTO: {}", memberDTO);
        memberService.insert(memberDTO);
        return "redirect:/member/list";
    }

    //delete
    //http://localhost:8080/member/delete/1
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        log.info("Delete - id: {}", id);
        memberService.delete(id);
        return "redirect:/member/list";
    }

}

