package com.example.demo2.mapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.demo2.domain.MemberDTO;

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
}