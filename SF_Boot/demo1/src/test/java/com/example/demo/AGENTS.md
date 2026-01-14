# Module Context

테스트 계층은 애플리케이션의 각 레이어를 검증하는 역할을 담당합니다.
단위 테스트, 통합 테스트, Mapper 테스트 등을 작성하여 코드 품질을 보장합니다.

## Dependencies
- Spring Boot Test
- JUnit 5 (Jupiter)
- AssertJ (Assertions)
- Mockito (Mocking)
- MyBatis Spring Boot Starter Test

## Responsibilities
- 각 계층별 기능 검증
- 비즈니스 로직 정확성 확인
- 데이터베이스 연동 테스트
- 예외 처리 검증
- 성능 및 안정성 확인

## What NOT to Do
- 프로덕션 데이터베이스에서 테스트 실행
- 외부 API 직접 호출 (Mock 사용)
- 테스트 간 의존성 생성 (독립적 실행 보장)

# Tech Stack & Constraints

## Test Annotations

### Class Level
- @SpringBootTest: 전체 애플리케이션 컨텍스트 로드
- @WebMvcTest: Controller 레이어만 테스트
- @MybatisTest: MyBatis Mapper만 테스트
- @DataJdbcTest: JDBC 관련 테스트
- @Transactional: 테스트 후 자동 롤백

### Method Level
- @Test: 테스트 메서드 지정
- @BeforeEach: 각 테스트 전 실행
- @AfterEach: 각 테스트 후 실행
- @BeforeAll: 전체 테스트 시작 전 실행 (static)
- @AfterAll: 전체 테스트 종료 후 실행 (static)
- @Disabled: 테스트 비활성화
- @DisplayName: 테스트 설명 추가

## Required Dependencies (pom.xml)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter-test</artifactId>
    <version>3.0.5</version>
    <scope>test</scope>
</dependency>
```

# Implementation Patterns

## File Naming
- 테스트 클래스명: `{Target}Test.java`
- 예: MemberControllerTest.java, MemberServiceTest.java, MemberMapperTest.java

## Package Structure
```
src/test/java/com/example/demo/
├── controller/
│   └── MemberControllerTest.java
├── service/
│   └── MemberServiceTest.java
└── mapper/
    └── MemberMapperTest.java
```

## Template Patterns

### 1. Mapper Test (@MybatisTest)
```java
package com.example.demo.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.example.demo.domain.MemberDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("전체 회원 목록 조회 테스트")
    void testSelectAll() {
        // when
        List<MemberDTO> list = memberMapper.selectAll();

        // then
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
    }

    @Test
    @DisplayName("ID로 회원 조회 테스트")
    void testSelectById() {
        // given
        Long id = 1L;

        // when
        MemberDTO member = memberMapper.selectById(id);

        // then
        assertThat(member).isNotNull();
        assertThat(member.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("회원 등록 테스트")
    void testInsert() {
        // given
        MemberDTO dto = MemberDTO.builder()
            .name("테스트사용자")
            .email("test@example.com")
            .phone("010-1234-5678")
            .build();

        // when
        memberMapper.insert(dto);

        // then
        assertThat(dto.getId()).isNotNull();

        MemberDTO inserted = memberMapper.selectById(dto.getId());
        assertThat(inserted.getName()).isEqualTo("테스트사용자");
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void testUpdate() {
        // given
        MemberDTO member = memberMapper.selectById(1L);
        member.setName("수정된이름");

        // when
        memberMapper.update(member);

        // then
        MemberDTO updated = memberMapper.selectById(1L);
        assertThat(updated.getName()).isEqualTo("수정된이름");
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void testDelete() {
        // given
        Long id = 1L;

        // when
        memberMapper.deleteById(id);

        // then
        MemberDTO deleted = memberMapper.selectById(id);
        assertThat(deleted).isNull();
    }
}
```

### 2. Service Test (@SpringBootTest)
```java
package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.MemberDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("전체 회원 목록 조회 서비스 테스트")
    void testGetList() {
        // when
        List<MemberDTO> list = memberService.getList();

        // then
        assertThat(list).isNotNull();
    }

    @Test
    @DisplayName("회원 등록 서비스 테스트")
    void testRegister() {
        // given
        MemberDTO dto = MemberDTO.builder()
            .name("홍길동")
            .email("hong@example.com")
            .build();

        // when
        MemberDTO result = memberService.register(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
    }

    @Test
    @DisplayName("회원 등록 검증 실패 테스트")
    void testRegisterValidationFail() {
        // given
        MemberDTO invalidDto = MemberDTO.builder().build();

        // when & then
        assertThatThrownBy(() -> memberService.register(invalidDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Name is required");
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void testUpdate() {
        // given
        Long id = 1L;
        MemberDTO dto = MemberDTO.builder()
            .id(id)
            .name("변경된이름")
            .email("changed@example.com")
            .build();

        // when
        MemberDTO updated = memberService.update(id, dto);

        // then
        assertThat(updated.getName()).isEqualTo("변경된이름");
    }
}
```

### 3. Controller Test (@WebMvcTest)
```java
package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.service.MemberService;
import com.example.demo.domain.MemberDTO;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원 목록 페이지 호출 테스트")
    void testList() throws Exception {
        // given
        List<MemberDTO> mockList = Arrays.asList(
            MemberDTO.builder().id(1L).name("홍길동").email("hong@test.com").build(),
            MemberDTO.builder().id(2L).name("김철수").email("kim@test.com").build()
        );
        given(memberService.getList()).willReturn(mockList);

        // when & then
        mockMvc.perform(get("/member/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("member/list"))
            .andExpect(model().attributeExists("lists"))
            .andExpect(model().attribute("lists", hasSize(2)));
    }

    @Test
    @DisplayName("회원 상세 페이지 호출 테스트")
    void testDetail() throws Exception {
        // given
        Long id = 1L;
        MemberDTO mockMember = MemberDTO.builder()
            .id(id)
            .name("홍길동")
            .email("hong@test.com")
            .build();
        given(memberService.getById(id)).willReturn(mockMember);

        // when & then
        mockMvc.perform(get("/member/{id}", id))
            .andExpect(status().isOk())
            .andExpect(view().name("member/detail"))
            .andExpect(model().attributeExists("member"))
            .andExpect(model().attribute("member", mockMember));
    }
}
```

### 4. REST API Controller Test
```java
package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.service.MemberService;
import com.example.demo.domain.MemberDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberRestController.class)
class MemberRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    void testGetList() throws Exception {
        // given
        given(memberService.getList()).willReturn(Arrays.asList(
            MemberDTO.builder().id(1L).name("홍길동").build()
        ));

        // when & then
        mockMvc.perform(get("/api/member"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("홍길동"));
    }

    @Test
    void testCreate() throws Exception {
        // given
        MemberDTO dto = MemberDTO.builder()
            .name("홍길동")
            .email("hong@test.com")
            .build();

        MemberDTO savedDto = MemberDTO.builder()
            .id(1L)
            .name("홍길동")
            .email("hong@test.com")
            .build();

        given(memberService.register(any(MemberDTO.class))).willReturn(savedDto);

        // when & then
        mockMvc.perform(post("/api/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("홍길동"));
    }
}
```

## Testing Best Practices

### AAA Pattern
모든 테스트는 Arrange-Act-Assert 패턴을 따릅니다.
```java
@Test
void testExample() {
    // Arrange (Given): 테스트 준비
    MemberDTO dto = MemberDTO.builder().name("홍길동").build();

    // Act (When): 테스트 실행
    MemberDTO result = memberService.register(dto);

    // Assert (Then): 검증
    assertThat(result).isNotNull();
}
```

### AssertJ vs JUnit Assertions
AssertJ를 우선 사용합니다 (가독성 향상).
```java
// Good: AssertJ
assertThat(member.getName()).isEqualTo("홍길동");
assertThat(list).hasSize(5);
assertThat(member).isNotNull();

// Avoid: JUnit Assertions
assertEquals("홍길동", member.getName());
assertEquals(5, list.size());
assertNotNull(member);
```

### Exception Testing
```java
@Test
void testException() {
    assertThatThrownBy(() -> memberService.register(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("required");
}
```

# Testing Strategy

## Test Execution Commands
```bash
# 전체 테스트
./mvnw test

# 특정 패키지 테스트
./mvnw test -Dtest=com.example.demo.mapper.*Test

# 특정 클래스 테스트
./mvnw test -Dtest=MemberMapperTest

# 특정 메서드 테스트
./mvnw test -Dtest=MemberMapperTest#testSelectAll
```

## Test Coverage
```bash
# JaCoCo 플러그인 사용 (pom.xml에 추가 필요)
./mvnw test jacoco:report
```

# Local Golden Rules

## Do's
- 모든 public 메서드는 테스트 작성.
- 테스트는 독립적으로 실행 가능해야 함 (순서 무관).
- @Transactional로 테스트 후 자동 롤백 설정.
- @DisplayName으로 테스트 의도 명확히 표현.
- AssertJ를 사용하여 가독성 높은 검증 작성.

## Don'ts
- 프로덕션 데이터베이스에서 테스트 실행 금지.
- 테스트 간 데이터 의존성 생성 금지.
- System.out.println() 대신 로깅 사용.
- 불필요한 Thread.sleep() 사용 금지.
- 테스트 실패 시 @Disabled로 숨기지 마라 (원인 해결).

## Common Mistakes
- @Transactional 누락으로 테스트 데이터가 DB에 남는 경우.
- Mock 객체 설정 누락으로 NullPointerException 발생.
- given/when/then 구조 없이 테스트 작성.
- 예외 테스트 시 assertThatThrownBy 대신 try-catch 사용.
- 테스트 메서드명이 의미 없는 경우 (test1, test2 등).

## Test Naming Convention
```java
// Good
@Test
void testSelectAll() { }

@Test
void testRegisterWithValidInput() { }

@Test
void testUpdateFailsWhenMemberNotFound() { }

// Bad
@Test
void test1() { }

@Test
void a() { }
```
