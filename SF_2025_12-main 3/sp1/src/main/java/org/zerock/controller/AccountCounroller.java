package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/account")
@Log4j2
public class AccountCounroller {

    @GetMapping("/login")
    public void loginGET() {
        log.info("/account/login - GET 요청");
    }

    @GetMapping("/logout")
    public String logoutGET() {
        log.info("/account/logout - GET 요청");
        return "account/logout";
    }
}
