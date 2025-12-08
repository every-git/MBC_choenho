package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.zerock.service.TodoService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.zerock.dto.TodoDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/todo")
@RequiredArgsConstructor
@Log4j2
public class TodoController {

	private final TodoService service;


    // localhost:8080/todo/list
    // -> /WEB-INF/views/todo/list.jsp
	@GetMapping("/list")
	public void list(Model model) {
		List<TodoDTO> list = service.getList();
		log.info("Todo list size: " + list.size());
		model.addAttribute("todoList", list);
	} // end of list

	@GetMapping("/register")
	public void register() {
		log.info("Todo register");
	} // end of register

    @PostMapping("/register")
    public String registerPost(TodoDTO dto) {
        log.info("Todo register: " + dto);
        service.insert(dto);
        return "redirect:/todo/list";
    } // end of registerPost

    @GetMapping("/read/{id}")
    public String read(@PathVariable("id") int id, Model model) {
        log.info("Todo read: " + id);
        TodoDTO todo = service.todoById(id);
        model.addAttribute("todo", todo);
        return "todo/read";
    } // end of read

    @PostMapping("/modify")
    public String modifyPost(TodoDTO dto) {
        log.info("Todo modify: " + dto);
        service.update(dto);
        return "redirect:/todo/list";
    } // end of modify

    @PostMapping("/delete")
    public String deletePost(@RequestParam("id") int id) {
        log.info("Todo delete: " + id);
        service.delete(id);
        return "redirect:/todo/list";
    } // end of delete
}
