package org.zerock.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerock.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
class TodoServiceTest {

	@Autowired
	private TodoService todoService;

	@Test
	void testGetList() {
		todoService.getList().forEach(todo-> log.info(todo));
	} // end of testGetList

	@Test
	void testTodoById() {
		TodoDTO todo = todoService.todoById(1);
		log.info(todo);
	} // end of testTodoById

	@Test
	void testInsert() {
		TodoDTO dto = TodoDTO.builder()
				.title("서비스 테스트 Todo")
				.description("서비스를 통한 등록 테스트")
				.done(false)
				.build();
		todoService.insert(dto);
		log.info("Inserted todo: " + todoService.todoById(dto.getId()));
	} // end of testInsert

	@Test
	void testUpdate() {
		todoService.update(TodoDTO.builder()
				.id(1)
				.title("서비스 수정 테스트")
				.description("서비스를 통한 수정")
				.done(true)
				.build());
		log.info(todoService.todoById(1));
	} // end of testUpdate

	@Test
	void testDelete() {
		todoService.delete(1);
		log.info(todoService.todoById(1));
	} // end of testDelete
}
