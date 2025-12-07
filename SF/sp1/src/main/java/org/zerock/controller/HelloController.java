package org.zerock.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.dto.SampleDTO;
import org.zerock.service.HelloService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sample")
public class HelloController {
	
	private static final Logger log = LogManager.getLogger(HelloController.class);

	@GetMapping("/ex1")
	public String ex1() {
		log.info("========== /ex1 컨트롤러 호출됨 ==========");
		return "sample/ex1";
	}

	@GetMapping("/ex2")
	public void ex2() {
		log.info("sample/ex2");
	}
	
	@GetMapping("/ex3re")
	public String ex3Re() {
		return "sample/ex3Result";
	}

	@GetMapping("/ex4")
	public void ex4(@RequestParam(name="n1", defaultValue = "1") int num,
					@RequestParam("name") String name) {
		log.info("num :" + num);
		log.info("name : " + name);
	}
	
	@GetMapping("/ex5")
	public void ex5 (SampleDTO dto) {
		log.info(dto);
	}

	// 요청은 get 방식으로 sample/ex6를 통해 받는다.
	// 
	@GetMapping("/ex6")
	public void ex6(Model model) {
		model.addAttribute("name", "Hong Gil Dong");
		model.addAttribute("age", 20);
	}

	@GetMapping("/ex7")
	public String ex7(RedirectAttributes rttr) {
		rttr.addAttribute("name", "Hong Gil Dong");
		rttr.addAttribute("age", 25);
		return "redirect:/sample/ex8";
	}

	@GetMapping("/ex8")
	public void ex8(@RequestParam("name") String name, @RequestParam("age") Integer age, Model model) {
		model.addAttribute("name", name);
		model.addAttribute("age", age);
	}

}
