package com.example.demo.mapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.domain.MemberDTO;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Transactional
@Log4j2
public class MemberMapperTest {
    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("All READ: 전체 데이터 조회")
    void findAll() {
        // 이 사항에서(given) -> 이런 행동을 하면(when) -> 이런 결과가 나온다(then).
        // given -> 테스트에 필요한 모든 조건을 만드는 단계.

        // when -> 테스트에 필요한 행동을 만드는 단계.
        List<MemberDTO> list = memberMapper.findAll();
        
        // then -> 테스트에 필요한 결과를 만드는 단계.
        // 람다 표현식에서 타입을 명시할 때는 괄호가 필요합니다: (MemberDTO m) -> ...
        // 또는 타입을 생략할 수 있습니다: m -> ...
        list.forEach(m -> log.info(m));
    }
    @Test
    @DisplayName("CREATE: 데이터 생성")
    public void insertTest() {
        // given
        MemberDTO member = MemberDTO.builder()
            .name("홍길동")
            .age(20)
            .address("강남구")
            .phone("010-1234-5678")
            .build();
        // when
        int result = memberMapper.insert(member);
        // then
        assertEquals(1, result);
        // primitive int는 null이 될 수 없으므로, 0보다 큰지 확인
        assertThat(member.getMemberId()).isGreaterThan(0);
        log.info("생성된 회원 ID: {}", member.getMemberId());
    }
    @Test
    @DisplayName("READ: ID로 회원 조회")
    public void findByIdTest() {
        // given
        int memberId = 1;
        // when
        MemberDTO member = memberMapper.findById(memberId);
        // then
        assertNotNull(member);
        log.info("조회된 회원: {}", member);
    }
    @Test
    @DisplayName("UPDATE: 데이터 수정")
    public void updateTest() {
        // given
        int memberId = 1;
        MemberDTO member = memberMapper.findById(memberId);
        member.setName("이순신");
        member.setAge(30);
        //when
        int updated = memberMapper.update(member);
        //then
        assertEquals(1, updated);
        MemberDTO found = memberMapper.findById(1);

        assertEquals("이순신", found.getName());
        assertEquals(30, found.getAge());
    }
    @Test
    @DisplayName("DELETE: 데이터 삭제")
    public void deleteTest() {
        // given
        MemberDTO member = memberMapper.findById(1);
        // when
        int result = memberMapper.delete(member.getMemberId());
        // then
        assertEquals(1, result);
        MemberDTO found = memberMapper.findById(member.getMemberId());
        assertNull(found, "삭제된 회원은 조회되지 않아야 합니다.");
    }
}
