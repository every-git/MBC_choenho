# Module Context

Controller 계층은 HTTP 요청을 받아 처리하고, Service 계층을 호출하며, 응답을 반환하는 역할을 담당합니다.
Spring MVC 패턴에서 C(Controller)에 해당하며, 프레젠테이션 로직만 포함합니다.

## Dependencies
- Spring Web MVC
- Lombok (@Log4j2, @RequiredArgsConstructor)
- Service Layer (com.example.demo.service)
- Domain Layer (com.example.demo.domain)

## Responsibilities
- HTTP 요청 매핑 및 파라미터 바인딩
- 요청 데이터 기본 검증 (Bean Validation)
- Service 호출 및 결과 처리
- Model에 데이터 추가 (뷰 렌더링용)
- 뷰 이름 반환 또는 JSON 응답 반환

## What NOT to Do
- 비즈니스 로직 작성 (Service 계층에 위임)
- 데이터베이스 직접 접근 (Mapper 직접 호출 금지)
- 복잡한 데이터 변환 로직 (Service 또는 별도 Util 사용)

# Tech Stack & Constraints

## Annotations
- @Controller: Thymeleaf 뷰 반환 시 사용
- @RestController: JSON 응답 시 사용 (@Controller + @ResponseBody)
- @RequestMapping: 클래스 레벨 공통 경로 매핑
- @GetMapping, @PostMapping, @PutMapping, @DeleteMapping: HTTP 메서드별 매핑
- @RequiredArgsConstructor: final 필드 생성자 주입 (Lombok)
- @Log4j2: 로깅 사용 (Lombok)

## Required Patterns
```java
@Controller
@RequestMapping("/{entity}")
@Log4j2
@RequiredArgsConstructor
public class {Entity}Controller {
    private final {Entity}Service service;

    @GetMapping("/list")
    public String list(Model model) {
        log.info("list.........");
        model.addAttribute("lists", service.getList());
        return "{entity}/list";
    }
}
```

## Validation
- @Valid 또는 @Validated 사용하여 DTO 검증
- BindingResult로 검증 에러 처리
- 예외 처리는 @ExceptionHandler 또는 전역 ControllerAdvice 사용

# Implementation Patterns

## File Naming
- 클래스명: `{Entity}Controller.java`
- 예: MemberController.java, OrderController.java

## Package Location
```
src/main/java/com/example/demo/controller/
```

## Template Pattern

### 1. Thymeleaf View Controller
```java
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;

import com.example.demo.service.MemberService;
import com.example.demo.domain.MemberDTO;

import java.util.List;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model) {
        log.info("list.........");
        List<MemberDTO> lists = memberService.getList();
        model.addAttribute("lists", lists);
        return "member/list"; // templates/member/list.html
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.info("detail id: {}", id);
        MemberDTO member = memberService.getById(id);
        model.addAttribute("member", member);
        return "member/detail";
    }

    @PostMapping("/register")
    public String register(MemberDTO dto) {
        log.info("register: {}", dto);
        memberService.register(dto);
        return "redirect:/member/list";
    }
}
```

### 2. REST API Controller
```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;

import com.example.demo.service.MemberService;
import com.example.demo.domain.MemberDTO;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@Log4j2
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberService memberService;

    @GetMapping
    public List<MemberDTO> list() {
        log.info("REST API list.........");
        return memberService.getList();
    }

    @GetMapping("/{id}")
    public MemberDTO detail(@PathVariable Long id) {
        log.info("REST API detail: {}", id);
        return memberService.getById(id);
    }

    @PostMapping
    public MemberDTO create(@RequestBody MemberDTO dto) {
        log.info("REST API create: {}", dto);
        return memberService.register(dto);
    }

    @PutMapping("/{id}")
    public MemberDTO update(@PathVariable Long id, @RequestBody MemberDTO dto) {
        log.info("REST API update: {}", id);
        return memberService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("REST API delete: {}", id);
        memberService.delete(id);
    }
}
```

### 3. Request Parameter Handling
```java
// Query Parameter
@GetMapping("/search")
public String search(@RequestParam String keyword, Model model) {
    log.info("search keyword: {}", keyword);
    model.addAttribute("results", memberService.search(keyword));
    return "member/search";
}

// Optional Parameter
@GetMapping("/filter")
public String filter(@RequestParam(required = false) String status, Model model) {
    log.info("filter status: {}", status);
    model.addAttribute("lists", memberService.filterByStatus(status));
    return "member/list";
}

// Multiple Parameters
@GetMapping("/range")
public String range(
    @RequestParam int page,
    @RequestParam int size,
    Model model
) {
    log.info("page: {}, size: {}", page, size);
    model.addAttribute("lists", memberService.getPage(page, size));
    return "member/list";
}
```

## Common Patterns

### Return Types
- `String`: 뷰 이름 반환 (Thymeleaf)
- `void`: 메서드명과 동일한 뷰 반환 (예: list() -> "member/list")
- `Object`: JSON 응답 (@RestController 사용 시)
- `ResponseEntity<T>`: HTTP 상태 코드와 함께 응답

### Redirect
```java
@PostMapping("/create")
public String create(MemberDTO dto) {
    memberService.register(dto);
    return "redirect:/member/list"; // 목록으로 리다이렉트
}
```

### Exception Handling
```java
@ExceptionHandler(IllegalArgumentException.class)
public String handleIllegalArgument(IllegalArgumentException e, Model model) {
    log.error("Invalid argument: {}", e.getMessage());
    model.addAttribute("error", e.getMessage());
    return "error/400";
}
```

# Testing Strategy

## Test Annotations
- @WebMvcTest: Controller 단위 테스트 (Service는 Mock)
- @SpringBootTest + @AutoConfigureMockMvc: 통합 테스트

## Test Template
```java
package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.service.MemberService;
import com.example.demo.domain.MemberDTO;

import java.util.Arrays;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    void testList() throws Exception {
        // given
        given(memberService.getList()).willReturn(Arrays.asList(
            new MemberDTO(1L, "홍길동", "hong@test.com")
        ));

        // when & then
        mockMvc.perform(get("/member/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("member/list"))
            .andExpect(model().attributeExists("lists"));
    }
}
```

## Test Commands
```bash
# Controller 테스트만 실행
./mvnw test -Dtest=*Controller*Test
```

# Local Golden Rules

## Do's
- Service는 반드시 final 필드로 선언하고 생성자 주입 사용.
- 모든 핸들러 메서드에 log.info()로 진입점 로깅.
- RESTful URL 네이밍 준수: 명사 사용, 동사 금지.
- @PathVariable과 @RequestParam을 명확히 구분.
- Model 객체 사용 시 명확한 attribute 이름 지정.

## Don'ts
- Controller에서 try-catch로 비즈니스 예외 처리하지 마라 (ControllerAdvice 사용).
- Service 메서드를 직접 트랜잭션 처리하지 마라 (Service 계층 책임).
- HttpServletRequest/Response를 직접 다루지 마라 (Spring MVC 추상화 사용).
- 하드코딩된 뷰 경로 사용 금지 (상수화 권장).
- @RequestMapping의 value와 method 속성 대신 전용 어노테이션 사용 (@GetMapping 등).

## Common Mistakes
- void 메서드 사용 시 반환 뷰 경로를 잊는 경우: void는 메서드명 기반 뷰 반환.
- @Controller와 @RestController 혼동: JSON 응답은 @RestController 필수.
- Model 대신 ModelAndView를 불필요하게 사용: 간단한 경우 Model 권장.
- Redirect 시 "redirect:" 접두사 누락.
