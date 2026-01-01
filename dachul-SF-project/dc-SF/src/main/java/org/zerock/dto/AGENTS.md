# Module Context

DTO (Data Transfer Object) Layer. 계층 간 데이터 전달 객체.

## Role & Responsibilities

- Controller-Service-Mapper 간 데이터 전달
- MyBatis 결과 매핑
- JSON 직렬화/역직렬화
- 데이터 검증

## Dependencies

- Lombok (코드 간소화)
- Jackson (JSON 처리)
- Jakarta Validation (검증)

# Tech Stack & Constraints

## Required Annotations

- @Data: Getter/Setter/toString/equals/hashCode 자동 생성
- @NoArgsConstructor: 기본 생성자
- @AllArgsConstructor: 모든 필드 생성자
- @Builder: 빌더 패턴
- @Getter/@Setter: 개별 적용 가능

## Validation Annotations

- @NotNull: null 불가
- @NotEmpty: 빈 문자열 불가
- @NotBlank: 공백 불가
- @Size: 길이 제한
- @Min/@Max: 숫자 범위
- @Pattern: 정규식
- @Email: 이메일 형식

# Implementation Patterns

## Basic DTO

```java
package org.zerock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XxxDTO {

    private Long id;
    private String name;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

## Pagination DTO Pattern

```java
package org.zerock.dto;

import java.util.List;
import java.util.stream.IntStream;
import lombok.Data;

@Data
public class XxxListPaginDTO {

    private List<XxxDTO> xxxDTOList;
    private int totalCount;
    private int page;
    private int size;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;
    private List<Integer> pageNums;
    
    public XxxListPaginDTO(List<XxxDTO> xxxDTOList, int totalCount, int page, int size) {
        this.xxxDTOList = xxxDTOList;
        this.totalCount = totalCount;
        this.page = page;
        this.size = size;

        // 페이지 블록 계산
        int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;
        this.start = tempEnd - 9;

        if ((tempEnd * size) > totalCount) {
            this.end = (int)(Math.ceil(totalCount / (double)size));
        } else {
            this.end = tempEnd;
        }

        this.prev = start != 1;
        this.next = totalCount > (this.end * size);

        this.pageNums = IntStream.rangeClosed(start, end)
                                 .boxed()
                                 .toList();
    }
}
```

## DTO with Validation

```java
package org.zerock.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    private Long id;

    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 20, message = "사용자명은 3~20자여야 합니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다")
    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

## DTO with Nested Objects

```java
package org.zerock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private String memberName;  // JOIN 결과
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // One-to-Many 관계
    private List<CommentDTO> comments;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CommentDTO {

    private Long id;
    private Long boardId;
    private String content;
    private Long memberId;
    private String memberName;
    private LocalDateTime createdAt;
}
```

## DTO with Jackson Annotations

```java
package org.zerock.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    private Long id;
    private String username;

    @JsonIgnore  // JSON 직렬화 시 제외
    private String password;

    @JsonProperty("email_address")  // JSON 필드명 변경
    private String email;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // 날짜 포맷 지정
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
```

# Local Golden Rules

## Lombok Usage

### Do's
- @Data 기본 사용
- @Builder로 객체 생성 간소화
- @NoArgsConstructor, @AllArgsConstructor 함께 사용
- @Slf4j (필요 시 DTO에서는 거의 사용 안 함)

### Don'ts
- @EqualsAndHashCode 커스터마이징 필요 시 주의
- @ToString에 순환 참조 주의 (exclude 사용)

```java
@Data
@ToString(exclude = {"comments"})  // 순환 참조 방지
public class BoardDTO {
    private Long id;
    private String title;
    private List<CommentDTO> comments;
}
```

## Validation

### Do's
- 필수 필드에 @NotNull, @NotBlank 사용
- 길이 제한은 @Size 사용
- 정규식 검증은 @Pattern 사용
- 커스텀 검증 메시지 작성

### Don'ts
- 비즈니스 로직을 DTO에 작성 금지
- static 메서드로 변환 로직 작성 지양

## Date/Time Handling

### Do's
- LocalDateTime 사용 (Java 8+)
- Jackson의 JavaTimeModule 사용 (pom.xml에 jackson-datatype-jsr310 포함)

### Don'ts
- java.util.Date, java.sql.Timestamp 사용 금지
- String으로 날짜 저장 금지

## JSON Handling

### Do's
- @JsonIgnore로 민감 정보 제외 (password 등)
- @JsonFormat으로 날짜 포맷 통일
- @JsonProperty로 API 명세에 맞게 필드명 변경

### Don'ts
- 순환 참조 발생 주의
- null 필드 처리 고려 (@JsonInclude)

```java
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 필드 제외
public class ResponseDTO {
    private String status;
    private String message;
    private Object data;
}
```

# Testing Strategy

## DTO Test Pattern

```java
class MemberDTOTest {

    @Test
    void testBuilder() {
        MemberDTO member = MemberDTO.builder()
            .username("testuser")
            .password("password123")
            .email("test@example.com")
            .name("Test User")
            .build();

        assertNotNull(member);
        assertEquals("testuser", member.getUsername());
    }

    @Test
    void testEqualsAndHashCode() {
        MemberDTO member1 = MemberDTO.builder()
            .id(1L)
            .username("testuser")
            .build();

        MemberDTO member2 = MemberDTO.builder()
            .id(1L)
            .username("testuser")
            .build();

        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());
    }

    @Test
    void testValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        MemberDTO member = MemberDTO.builder()
            .username("")  // Invalid
            .password("123")  // Too short
            .email("invalid")  // Invalid format
            .build();

        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(member);

        assertFalse(violations.isEmpty());
        violations.forEach(v -> System.out.println(v.getMessage()));
    }
}
```

## JSON Serialization Test

```java
class MemberDTOJsonTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testJsonSerialization() throws Exception {
        MemberDTO member = MemberDTO.builder()
            .id(1L)
            .username("testuser")
            .email("test@example.com")
            .createdAt(LocalDateTime.now())
            .build();

        String json = objectMapper.writeValueAsString(member);

        System.out.println(json);
        assertNotNull(json);
        assertTrue(json.contains("testuser"));
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json = "{\"id\":1,\"username\":\"testuser\",\"email\":\"test@example.com\"}";

        MemberDTO member = objectMapper.readValue(json, MemberDTO.class);

        assertNotNull(member);
        assertEquals(1L, member.getId());
        assertEquals("testuser", member.getUsername());
    }
}
```

# Related Contexts

- Mapper Layer: ../mapper/AGENTS.md
- Service Layer: ../service/AGENTS.md
- Controller Layer: ../controller/AGENTS.md
