package org.zerock.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerock.dto.MemberDTO;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
class MemberMapperTests {
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Test
	void testInsert() {
		MemberDTO dto = MemberDTO.builder()
				.name("홍길동")
				.email("test@naver.com")
				.password("1234")
				.build();
		
		memberMapper.insert(dto);

		//log.info(생성된 mno : " + dto.getMno());
		
		assertNotNull(dto.getMno());
	} // end of testInsert

	@Test
	void testList() {
		memberMapper.getList().forEach(dto->log.info(dto));
	} // end of testList

	@Test
	void testInsertMultiple() {
		for (int i = 1; i <= 5; i++) {
			MemberDTO dto = MemberDTO.builder()
					.name("홍길동" + i)
					.email("test" + i + "@naver.com")
					.password("1234")
					.build();
			
			memberMapper.insert(dto);
			log.info("Inserted: " + dto.getName() + " - " + dto.getEmail());
		}
	} // end of testInsertMultiple

	@Test
	void testMemberById() {
		MemberDTO dto = memberMapper.memberById(5);
		log.info(dto);
	} // end of testMemberById

	@Test
	void testUpdate() {
		MemberDTO dto = MemberDTO.builder()
				.mno(5)
				.name("박길동")
				.email("park@naver.com")
				.password("1234")
				.build();
		memberMapper.update(dto);
		
		log.info(memberMapper.memberById(5));
	} // end of testUpdate

	@Test
	void testDelete() {
		memberMapper.delete(5);
		log.info(memberMapper.memberById(5));
	} // end of testDelete
}
