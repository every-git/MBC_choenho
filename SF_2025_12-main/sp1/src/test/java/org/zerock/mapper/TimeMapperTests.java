package org.zerock.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.IntStream;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class TimeMapperTests {

	@Autowired
	private TimeMapper timeMapper;
	
	@Test
	public void testTime1() {
		log.info("--------------------------");
		log.info(timeMapper.getTime());
	}

	@Test
	public void testTime2() {
		log.info("--------------------------");
		log.info(timeMapper.getTime2());
	}

	@Test
	public void testPagNums() {
		IntStream.range(1, 5).forEach(i->log.info(i));
	}
}
