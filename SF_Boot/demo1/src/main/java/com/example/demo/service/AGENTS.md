# Module Context

Service 계층은 비즈니스 로직을 처리하는 핵심 계층입니다.
Controller와 Mapper 사이에서 중재자 역할을 하며, 트랜잭션 경계를 정의합니다.

## Dependencies
- Spring Framework (@Transactional)
- Lombok (@RequiredArgsConstructor, @Log4j2)
- Mapper Layer (com.example.demo.mapper)
- Domain Layer (com.example.demo.domain)

## Responsibilities
- 비즈니스 로직 구현
- 트랜잭션 관리
- 데이터 검증 및 변환
- 여러 Mapper 조합 및 조율
- 예외 처리 및 비즈니스 규칙 적용

## What NOT to Do
- HTTP 관련 처리 (Controller 책임)
- SQL 쿼리 작성 (Mapper/XML 책임)
- 뷰 렌더링 로직 (Controller 책임)

# Tech Stack & Constraints

## Annotations
- @Service: 서비스 빈 등록
- @Transactional: 트랜잭션 경계 설정 (클래스 또는 메서드 레벨)
- @RequiredArgsConstructor: final 필드 생성자 주입 (Lombok)
- @Log4j2: 로깅 사용 (Lombok)

## Required Patterns
```java
@Service
@Log4j2
@RequiredArgsConstructor
public class {Entity}Service {
    private final {Entity}Mapper mapper;

    public List<{Entity}DTO> getList() {
        log.info("getList.........");
        return mapper.selectAll();
    }
}
```

## Transaction Management
- 읽기 전용: @Transactional(readOnly = true)
- 쓰기 작업: @Transactional
- 격리 수준: @Transactional(isolation = Isolation.READ_COMMITTED)
- 전파 수준: @Transactional(propagation = Propagation.REQUIRED) [기본값]

# Implementation Patterns

## File Naming
- 클래스명: `{Entity}Service.java`
- 예: MemberService.java, OrderService.java

## Package Location
```
src/main/java/com/example/demo/service/
```

## Template Pattern

### 1. Basic CRUD Service
```java
package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.MemberMapper;
import com.example.demo.domain.MemberDTO;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberMapper memberMapper;

    public List<MemberDTO> getList() {
        log.info("getList.........");
        return memberMapper.selectAll();
    }

    public MemberDTO getById(Long id) {
        log.info("getById: {}", id);
        return memberMapper.selectById(id);
    }

    @Transactional
    public MemberDTO register(MemberDTO dto) {
        log.info("register: {}", dto);
        validateMember(dto);
        memberMapper.insert(dto);
        return dto;
    }

    @Transactional
    public MemberDTO update(Long id, MemberDTO dto) {
        log.info("update id: {}, dto: {}", id, dto);
        MemberDTO existing = memberMapper.selectById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Member not found: " + id);
        }
        memberMapper.update(dto);
        return dto;
    }

    @Transactional
    public void delete(Long id) {
        log.info("delete id: {}", id);
        memberMapper.deleteById(id);
    }

    private void validateMember(MemberDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }
}
```

### 2. Service with Multiple Mappers
```java
@Service
@Log4j2
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final MemberMapper memberMapper;
    private final ProductMapper productMapper;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        log.info("createOrder: {}", orderDTO);

        // 회원 검증
        MemberDTO member = memberMapper.selectById(orderDTO.getMemberId());
        if (member == null) {
            throw new IllegalArgumentException("Invalid member ID");
        }

        // 상품 재고 확인
        ProductDTO product = productMapper.selectById(orderDTO.getProductId());
        if (product.getStock() < orderDTO.getQuantity()) {
            throw new IllegalStateException("Insufficient stock");
        }

        // 주문 생성
        orderMapper.insert(orderDTO);

        // 재고 차감
        productMapper.decreaseStock(orderDTO.getProductId(), orderDTO.getQuantity());

        return orderDTO;
    }
}
```

### 3. Service with Business Logic
```java
@Service
@Log4j2
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Transactional
    public PaymentDTO processPayment(Long orderId, String paymentMethod) {
        log.info("processPayment orderId: {}, method: {}", orderId, paymentMethod);

        OrderDTO order = orderMapper.selectById(orderId);
        validateOrder(order);

        // 비즈니스 로직: 결제 금액 계산
        int totalAmount = calculateTotalAmount(order);

        PaymentDTO payment = PaymentDTO.builder()
            .orderId(orderId)
            .amount(totalAmount)
            .method(paymentMethod)
            .status("COMPLETED")
            .build();

        paymentMapper.insert(payment);
        orderMapper.updateStatus(orderId, "PAID");

        return payment;
    }

    private void validateOrder(OrderDTO order) {
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Order is not in pending status");
        }
    }

    private int calculateTotalAmount(OrderDTO order) {
        int baseAmount = order.getPrice() * order.getQuantity();
        int discount = order.getDiscountRate() != null
            ? baseAmount * order.getDiscountRate() / 100
            : 0;
        return baseAmount - discount;
    }
}
```

## Common Patterns

### Pagination
```java
public List<MemberDTO> getPage(int page, int size) {
    log.info("getPage: {}, size: {}", page, size);
    int offset = (page - 1) * size;
    return memberMapper.selectPage(offset, size);
}

public int getTotalCount() {
    return memberMapper.countAll();
}
```

### Search
```java
public List<MemberDTO> search(String keyword) {
    log.info("search keyword: {}", keyword);
    if (keyword == null || keyword.trim().isEmpty()) {
        return memberMapper.selectAll();
    }
    return memberMapper.searchByKeyword(keyword);
}
```

### Bulk Operations
```java
@Transactional
public void deleteMultiple(List<Long> ids) {
    log.info("deleteMultiple: {}", ids);
    if (ids == null || ids.isEmpty()) {
        throw new IllegalArgumentException("IDs are required");
    }
    ids.forEach(memberMapper::deleteById);
}

@Transactional
public void registerBulk(List<MemberDTO> members) {
    log.info("registerBulk size: {}", members.size());
    members.forEach(member -> {
        validateMember(member);
        memberMapper.insert(member);
    });
}
```

# Testing Strategy

## Test Annotations
- @SpringBootTest: 전체 애플리케이션 컨텍스트 로드
- @MybatisTest: MyBatis 관련 빈만 로드 (Service 테스트엔 부적합)
- @Transactional: 테스트 후 롤백

## Test Template
```java
package com.example.demo.service;

import org.junit.jupiter.api.Test;
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
    void testGetList() {
        // when
        List<MemberDTO> list = memberService.getList();

        // then
        assertThat(list).isNotNull();
    }

    @Test
    void testRegister() {
        // given
        MemberDTO dto = MemberDTO.builder()
            .name("테스터")
            .email("test@example.com")
            .build();

        // when
        MemberDTO result = memberService.register(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("테스터");
    }

    @Test
    void testValidation() {
        // given
        MemberDTO invalid = MemberDTO.builder().build();

        // when & then
        assertThatThrownBy(() -> memberService.register(invalid))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Name is required");
    }
}
```

## Test Commands
```bash
# Service 테스트만 실행
./mvnw test -Dtest=*Service*Test
```

# Local Golden Rules

## Do's
- 클래스 레벨에 @Transactional(readOnly = true)를 선언하고, 쓰기 메서드에만 @Transactional 오버라이드.
- 비즈니스 검증은 반드시 Service 계층에서 수행.
- 복잡한 로직은 private 메서드로 분리하여 가독성 향상.
- Mapper는 final 필드로 선언하고 생성자 주입 사용.
- 예외는 명확한 메시지와 함께 발생 (IllegalArgumentException, IllegalStateException 활용).

## Don'ts
- Controller나 Mapper 계층의 책임을 Service에 혼합하지 마라.
- try-catch로 예외를 무분별하게 잡지 마라 (비즈니스 예외는 상위로 전파).
- SQL 로직을 Service에 하드코딩하지 마라.
- static 메서드로 비즈니스 로직 작성 금지 (테스트 어려움).
- @Transactional 없이 여러 Mapper 호출 시 데이터 정합성 문제 발생 주의.

## Common Mistakes
- readOnly = true 상태에서 INSERT/UPDATE/DELETE 실행 시 예외 발생.
- 트랜잭션 경계 누락으로 인한 롤백 미작동.
- null 체크 없이 Mapper 결과 사용 시 NullPointerException.
- 검증 로직을 Controller에 중복 작성하는 경우.
- 비즈니스 로직을 Mapper XML에 작성하는 경우 (동적 SQL과 혼동).
