package org.zerock.mapper;

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
class TodoMapperTests {
	
	@Autowired
	private TodoMapper todoMapper;
	
	@Test
	void testInsert() {
		TodoDTO dto = TodoDTO.builder()
				.title("테스트 Todo")
				.description("테스트 설명입니다")
				.done(false)
				.build();
		
		todoMapper.insert(dto);

		log.info("생성된 id: " + dto.getId());
		
		assertNotNull(dto.getId());
	} // end of testInsert

	@Test
	void testList() {
		todoMapper.getList().forEach(dto->log.info(dto));
	} // end of testList

	@Test
	void testInsertMultiple() {
		for (int i = 1; i <= 5; i++) {
			TodoDTO dto = TodoDTO.builder()
					.title("Todo 제목 " + i)
					.description("Todo 설명 " + i)
					.done(i % 2 == 0)
					.build();
			
			todoMapper.insert(dto);
			log.info("Inserted: " + dto.getTitle() + " - " + dto.getDoneStatus());
		}
	} // end of testInsertMultiple

	@Test
	void testTodoById() {
		TodoDTO dto = todoMapper.todoById(1);
		log.info(dto);
	} // end of testTodoById

	@Test
	void testUpdate() {
		TodoDTO dto = TodoDTO.builder()
				.id(1)
				.title("수정된 Todo 제목")
				.description("수정된 설명")
				.done(true)
				.build();
		todoMapper.update(dto);
		
		log.info(todoMapper.todoById(1));
	} // end of testUpdate

	@Test
	void testDelete() {
		todoMapper.delete(1);
		log.info(todoMapper.todoById(1));
	} // end of testDelete
}
