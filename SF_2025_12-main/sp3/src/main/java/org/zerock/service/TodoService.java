package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.mapper.TodoMapper;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.zerock.dto.TodoDTO;


@Service
@RequiredArgsConstructor
public class TodoService {
	
	@Autowired
	private TodoMapper todoMapper;

    public List<TodoDTO> getList() {
        return todoMapper.getList();
    }

    public TodoDTO todoById(int id) {
        return todoMapper.todoById(id);
    }

    public void update(TodoDTO dto) {
        todoMapper.update(dto);
    }

    public void insert(TodoDTO dto) {
        todoMapper.insert(dto);
    }

    public void delete(int id) {
        todoMapper.delete(id);
    }

}
