package org.zerock.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.log4j.Log4j2;
import org.zerock.dto.AccountDTO;
import org.zerock.dto.AccountRole;
import org.zerock.mapper.AccountMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class AccountMapperTest {

	@Autowired
	private AccountMapper accountMapper;
	
	@Autowired
	private PasswordEncoder encoder;

	@Test
	void testInsert() {
		try {
			for(int i = 1; i <= 100; i++) {
				AccountDTO account = 
						AccountDTO.builder()
						.uid("user" + i)
						.upw(encoder.encode("1111"))
						.uname("사용자" + i)
						.email("user" + i + "@test.com")
						.build();
				
				account.addRole(AccountRole.USER);

				if(i>=80) {
					account.addRole(AccountRole.MANAGER);
				}
				if(i>=90) {
					account.addRole(AccountRole.ADMIN);
				}

				int result = accountMapper.insert(account);
				log.info("계정 {} insert 결과: {}", i, result);
				
				// 역할이 있는 경우에만 insertRoles 호출
				if(account.getRoleNames() != null && !account.getRoleNames().isEmpty()) {
					int roleResult = accountMapper.insertRoles(account);
					log.info("계정 {} 역할 insert 결과: {}", i, roleResult);
				}
			}
			log.info("테스트 완료: 100개 계정 생성");
		} catch (Exception e) {
			log.error("테스트 중 에러 발생", e);
			throw e;
		}
	}

	@Test
	void testSelectOne() {
		try {
			AccountDTO accountDTO = accountMapper.selectOne("user100");
			if(accountDTO == null) {
				log.error("accountDTO is null");
				return;
			}
			log.info("accountDTO: {}", accountDTO);
			log.info("uid: {}", accountDTO.getUid());
			log.info("uname: {}", accountDTO.getUname());
			log.info("email: {}", accountDTO.getEmail());
			log.info("roleNames: {}", accountDTO.getRoleNames());
		} catch (Exception e) {
			log.error("테스트 중 에러 발생", e);
			throw e;
		}
	}

}
