package org.zerock.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class TimeMapperTest {

    @Autowired
    private TimeMapper timeMapper;

    @Test
    public void testGetTime1() {
        log.info("------------testGetTime------------");
        log.info(timeMapper.getTime());
        log.info("------------testGetTime------------");
    }

    @Test
    public void testGetTime2() {
        log.info("------------testGetTime2------------");
        log.info(timeMapper.getTime2());
        log.info("------------testGetTime2------------");
    }

}
