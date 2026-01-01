# Module Context

Domain 계층은 데이터 전송 객체(DTO), 값 객체(VO), 엔티티(Entity)를 정의합니다.
애플리케이션 전역에서 사용되는 데이터 구조를 표현하며, 계층 간 데이터 전달 역할을 담당합니다.

## Dependencies
- Lombok (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)

## Responsibilities
- 데이터 구조 정의
- 필드 및 타입 선언
- Getter/Setter 제공 (Lombok 사용)
- 불변 객체 설계 (VO)
- 유효성 검증 어노테이션 추가 (선택적)

## What NOT to Do
- 비즈니스 로직 포함 (Service 책임)
- 데이터베이스 접근 로직 (Mapper 책임)
- 뷰 렌더링 로직 (Controller/Thymeleaf 책임)

# Tech Stack & Constraints

## Annotations
- @Data: Getter/Setter/toString/equals/hashCode 자동 생성 (Lombok)
- @Builder: 빌더 패턴 제공 (Lombok)
- @NoArgsConstructor: 기본 생성자 생성 (MyBatis 필수)
- @AllArgsConstructor: 모든 필드 생성자 생성 (Lombok)
- @Getter, @Setter: 개별 Getter/Setter 생성 (불변 객체 설계 시)

## Bean Validation (Optional)
- @NotNull: null 불가
- @NotBlank: 빈 문자열 불가
- @Email: 이메일 형식 검증
- @Min, @Max: 숫자 범위 검증
- @Size: 문자열/컬렉션 크기 검증

# Implementation Patterns

## File Naming
- DTO: `{Entity}DTO.java`
- VO: `{Entity}VO.java`
- Entity: `{Entity}.java` 또는 `{Entity}Entity.java`

## Package Location
```
src/main/java/com/example/demo/domain/
```

## Template Pattern

### 1. Basic DTO with Lombok
```java
package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
```

### 2. DTO with Validation
```java
package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다")
    private String name;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    private String email;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다")
    private String phone;

    @Size(max = 200, message = "주소는 200자 이하여야 합니다")
    private String address;
}
```

### 3. Immutable VO (Value Object)
```java
package com.example.demo.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Money {
    private final int amount;
    private final String currency;

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(this.amount + other.amount, this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(this.amount - other.amount, this.currency);
    }
}
```

### 4. DTO with Nested Objects
```java
package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private String orderDate;
    private String status;
    private int totalAmount;

    // 주문 상세 목록
    private List<OrderItemDTO> orderItems;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OrderItemDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private int price;
}
```

### 5. DTO with Date/Time Fields
```java
package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int viewCount;
}
```

## Common Patterns

### Builder Pattern Usage
```java
// 객체 생성 시 빌더 패턴 사용
MemberDTO member = MemberDTO.builder()
    .name("홍길동")
    .email("hong@example.com")
    .phone("010-1234-5678")
    .build();
```

### Copy with Modification
```java
// 기존 객체를 복사하며 일부 필드만 변경
MemberDTO updated = MemberDTO.builder()
    .id(existing.getId())
    .name("변경된이름")
    .email(existing.getEmail())
    .phone(existing.getPhone())
    .build();
```

### Null Safety
```java
// Null 체크 방어 코드
public String getDisplayName() {
    return name != null ? name : "Unknown";
}

public String getPhoneOrDefault() {
    return phone != null ? phone : "N/A";
}
```

## MyBatis Mapping Considerations

### Constructor vs NoArgsConstructor
MyBatis는 기본 생성자를 사용하여 객체를 생성하므로 @NoArgsConstructor 필수.

```java
@Data
@Builder
@NoArgsConstructor  // MyBatis 필수
@AllArgsConstructor // @Builder 사용 시 필요
public class MemberDTO {
    private Long id;
    private String name;
}
```

### Snake Case to Camel Case
DB 컬럼 `user_name`은 자동으로 DTO 필드 `userName`에 매핑됩니다.
(application.properties: `mybatis.configuration.map-underscore-to-camel-case=true`)

```java
// DB: user_name, created_at
// DTO: userName, createdAt
@Data
public class UserDTO {
    private Long id;
    private String userName;    // DB: user_name
    private LocalDateTime createdAt; // DB: created_at
}
```

# Testing Strategy

## Test Approach
DTO는 순수 데이터 클래스이므로 별도 테스트가 불필요한 경우가 많습니다.
하지만 커스텀 로직이나 검증 규칙이 있다면 테스트 작성을 권장합니다.

## Test Template
```java
package com.example.demo.domain;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class MemberDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testBuilder() {
        // given & when
        MemberDTO member = MemberDTO.builder()
            .id(1L)
            .name("홍길동")
            .email("hong@example.com")
            .build();

        // then
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getName()).isEqualTo("홍길동");
        assertThat(member.getEmail()).isEqualTo("hong@example.com");
    }

    @Test
    void testValidation_Success() {
        // given
        MemberDTO member = MemberDTO.builder()
            .name("홍길동")
            .email("hong@example.com")
            .build();

        // when
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(member);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidation_InvalidEmail() {
        // given
        MemberDTO member = MemberDTO.builder()
            .name("홍길동")
            .email("invalid-email")
            .build();

        // when
        Set<ConstraintViolation<MemberDTO>> violations = validator.validate(member);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .contains("이메일");
    }

    @Test
    void testEqualsAndHashCode() {
        // given
        MemberDTO member1 = MemberDTO.builder().id(1L).name("홍길동").build();
        MemberDTO member2 = MemberDTO.builder().id(1L).name("홍길동").build();

        // when & then
        assertThat(member1).isEqualTo(member2);
        assertThat(member1.hashCode()).isEqualTo(member2.hashCode());
    }
}
```

# Local Golden Rules

## Do's
- 모든 DTO에 @NoArgsConstructor 추가 (MyBatis 요구사항).
- @Builder를 사용하여 가독성 높은 객체 생성.
- DB 컬럼명은 snake_case, DTO 필드는 camelCase 사용.
- 불변 객체가 필요한 경우 @Getter + final 필드 + @RequiredArgsConstructor 조합 사용.
- 유효성 검증이 필요한 DTO는 Bean Validation 어노테이션 추가.

## Don'ts
- DTO에 비즈니스 로직 메서드 추가 금지 (순수 데이터 클래스 유지).
- Setter 남용하지 마라 (불변성 고려).
- @Data 사용 시 순환 참조 주의 (toString, equals, hashCode).
- DTO 내부에서 외부 의존성 주입하지 마라 (Service, Mapper 등).
- static 필드 남용 금지 (인스턴스 데이터만 포함).

## Common Mistakes
- @NoArgsConstructor 누락 시 MyBatis 매핑 실패.
- @Builder만 사용하고 @NoArgsConstructor/@AllArgsConstructor 누락 시 컴파일 에러.
- DB 컬럼명과 DTO 필드명 불일치 시 null 값 매핑.
- @Data 사용 시 양방향 참조로 인한 StackOverflowError.
- final 필드에 @Data 사용 시 Setter 생성 안 됨 (의도된 동작).

## DTO vs VO vs Entity

### DTO (Data Transfer Object)
- 계층 간 데이터 전달 목적.
- Mutable (변경 가능).
- @Data 사용 일반적.

### VO (Value Object)
- 값 자체를 표현.
- Immutable (불변).
- equals/hashCode 필수 구현.

### Entity
- 데이터베이스 테이블과 1:1 매핑.
- JPA 환경에서 주로 사용.
- MyBatis에서는 DTO와 유사하게 사용.
