package org.zerock.service;

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
class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Test
	void testGetList() {
		memberService.getList().forEach(member-> log.info(member));
	} // end of testGetList

	@Test
	void testMemberById() {
		MemberDTO member = memberService.memberById(6);
		log.info(member);
	} // end of testMemberById

	@Test
	void testInsert() {
		String uniqueEmail = "test" + System.currentTimeMillis() + "@naver.com";
		MemberDTO dto = MemberDTO.builder()
				.name("이길동")
				.email(uniqueEmail)
				.password("1234")
				.build();
		memberService.insert(dto);
		log.info("Inserted member: " + memberService.memberById(dto.getMno()));
	} // end of testInsert

	@Test
	void testUpdate() {
		memberService.update(MemberDTO.builder().mno(6).name("김길동").email("test11@naver.com").password("1234").build());
		log.info(memberService.memberById(6));
	} // end of testUpdate

	@Test
	void testDelete() {
		memberService.delete(6);
		log.info(memberService.memberById(6));
	} // end of testDelete
}
