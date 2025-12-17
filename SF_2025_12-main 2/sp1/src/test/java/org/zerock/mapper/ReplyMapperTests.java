package org.zerock.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerock.dto.ReplyDTO;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
class ReplyMapperTests {

    @Autowired
    private ReplyMapper replyMapper;

    @Test
    public void testInsert() {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .bno(5635996L)
                .replyText("댓글내용2")
                .replyer("이길동")
                .build();

        int result = replyMapper.insert(replyDTO);

        log.info("result : " + result);
    }

    @Test
    public void testRead() {

        ReplyDTO dto = replyMapper.read(1);
        log.info("dto : " + dto);
    }
    
    @Test
    public void testDelete() {
        log.info("delete result : " + replyMapper.delete(1));
    }

    @Test
    public void testUpdate() {
        ReplyDTO replyDTO = new ReplyDTO();

        replyDTO.setReplyText("댓글내용2");
        replyDTO.setRno(1);

        replyMapper.update(replyDTO);
    }

    @Test
    public void testInserts() {
        long[] bnos = {49899L, 49898L, 49897L};
        for(Long bno : bnos) {
            for(int i = 0; i < 100; i++) {
                ReplyDTO replyDTO = ReplyDTO.builder()
                .bno(bno)
                .replyText("댓글내용" + i)
                .replyer("이길동" + i)
                .build();
                replyMapper.insert(replyDTO);
            }
        }
    }

    @Test
    public void testList() {
        replyMapper.listOfBoard(49899L, 0, 10)
        .forEach(reply->log.info("reply : " + reply));
    }

}
