package org.zerock.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.log4j.Log4j2;
import org.zerock.dto.BoardDTO;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class BoardMapperTests {
    @Autowired
    private BoardMapper boardMapper;

    @Test
    public void testInsert() {

        /*BoardDTO dto = new BoardDTO();
        dto.setTitle("Test Title");
        dto.setContent("Test Content");
        dto.setWriter("Test Writer");*/

        BoardDTO dto = BoardDTO.builder()
            .title("Test Title01")
            .content("Test Content01")
            .writer("Test Writer01")
            .build();
        
        int insertCount = boardMapper.insert(dto);
        log.info("--------------------------------");
        log.info("insertCount: " + insertCount);
        log.info("--------------------------------");

        log.info("BNO: " + dto.getBno());
        log.info("--------------------------------");
    }

    @Test
    public void testSelectOne() {
        BoardDTO dto = boardMapper.selectOne(1L);
        log.info("--------------------------------");
        log.info("dto: " + dto);
        log.info("--------------------------------");
    }

    @Test
    public void testRemove() {
        int result = boardMapper.remove(1L);
        log.info("--------------------------------");
        log.info("result: " + result);
        log.info("--------------------------------");
    }

    @Test
    public void testUpdate() {
        BoardDTO dto = BoardDTO.builder()
            .bno(1L)  // 업데이트할 게시글 번호 지정
            .title("new Title")
            .content("new Content")
            .delFlag(false)
            .build();
        int result = boardMapper.update(dto);
        log.info("--------------------------------");
        log.info("result: " + result);
        log.info("--------------------------------");
    }

    @Test
    public void testSelect() {

        List<BoardDTO> list = boardMapper.list();

        for (BoardDTO dto : list) {
            log.info(dto);
        }
    }
}

