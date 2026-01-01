package org.zerock.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.dto.MemberDTO;
import org.zerock.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 회원 컨트롤러
 */
@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        log.info("login form... error: {}, logout: {}", error, logout);

        if (error != null) {
            model.addAttribute("errorMsg", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "로그아웃되었습니다.");
        }

        return "member/login";
    }

    /**
     * 회원가입 페이지
     */
    @GetMapping("/join")
    public String joinForm() {
        log.info("join form...");
        return "member/join";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/join")
    public String join(MemberDTO dto, @RequestParam(value = "admin_password", required = false) String adminPassword,
            RedirectAttributes rttr) {
        log.info("join... dto: {}", dto);

        // 관리자 가입 시 비밀번호(시크릿 코드) 검증
        if ("ADMIN".equals(dto.getRole())) {
            if (!"9876".equals(adminPassword)) {
                rttr.addFlashAttribute("errorMsg", "관리자 가입 비밀번호가 올바르지 않습니다.");
                return "redirect:/member/join";
            }
        }

        boolean result = memberService.join(dto);

        if (result) {
            rttr.addFlashAttribute("msg", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/member/login";
        } else {
            rttr.addFlashAttribute("errorMsg", "회원가입에 실패했습니다. 아이디가 중복되었을 수 있습니다.");
            return "redirect:/member/join";
        }
    }

    /**
     * 아이디 중복 체크 (AJAX)
     */
    @GetMapping("/idCheck")
    @ResponseBody
    public ResponseEntity<String> idCheck(@RequestParam("id") String id) {
        log.info("idCheck... id: {}", id);

        boolean isDuplicate = memberService.isDuplicateId(id);

        if (isDuplicate) {
            return ResponseEntity.ok("duplicate");
        } else {
            return ResponseEntity.ok("available");
        }
    }

    /**
     * 접근 거부 페이지
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        log.info("access denied...");
        return "member/access-denied";
    }
}
