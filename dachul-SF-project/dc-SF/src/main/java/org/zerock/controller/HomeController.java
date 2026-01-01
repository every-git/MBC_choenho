package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j2;

/**
 * 홈 컨트롤러
 */
@Controller
@Log4j2
public class HomeController {

    @GetMapping("/")
    public String home() {
        log.info("home...");
        return "home";
    }
}
