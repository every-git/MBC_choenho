# Module Context

Mapper 계층은 MyBatis 인터페이스를 정의하여 데이터 액세스를 추상화합니다.
XML 매퍼와 1:1 매핑되며, SQL 실행을 위한 Java 메서드를 선언합니다.

## Dependencies
- MyBatis Spring Boot Starter
- Domain Layer (com.example.demo.domain)

## Responsibilities
- SQL 메서드 시그니처 정의
- 파라미터 타입 및 반환 타입 명시
- XML Mapper와 네임스페이스 매핑

## What NOT to Do
- SQL 쿼리 작성 (XML Mapper 책임)
- 비즈니스 로직 포함 (Service 책임)
- default 메서드로 로직 구현 (간단한 유틸리티 제외)

# Tech Stack & Constraints

## Annotations
- @Mapper: MyBatis 매퍼 인터페이스 등록 (필수)
- @Param: 파라미터가 2개 이상일 때 명시적 바인딩

## Required Patterns
```java
@Mapper
public interface {Entity}Mapper {
    List<{Entity}DTO> selectAll();
    {Entity}DTO selectById(Long id);
    void insert({Entity}DTO dto);
    void update({Entity}DTO dto);
    void deleteById(Long id);
}
```

## MyBatis Configuration
- XML Mapper 위치: `src/main/resources/mapper/*.xml`
- Type Aliases: `com.example.demo.domain` (application.properties 설정)
- CamelCase 자동 매핑: `map-underscore-to-camel-case=true`

# Implementation Patterns

## File Naming
- 인터페이스명: `{Entity}Mapper.java`
- 예: MemberMapper.java, OrderMapper.java

## Package Location
```
src/main/java/com/example/demo/mapper/
```

## Template Pattern

### 1. Basic CRUD Mapper
```java
package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.MemberDTO;

import java.util.List;

@Mapper
public interface MemberMapper {

    /**
     * 전체 회원 목록 조회
     */
    List<MemberDTO> selectAll();

    /**
     * ID로 회원 조회
     */
    MemberDTO selectById(Long id);

    /**
     * 회원 등록
     */
    void insert(MemberDTO member);

    /**
     * 회원 수정
     */
    void update(MemberDTO member);

    /**
     * 회원 삭제
     */
    void deleteById(Long id);

    /**
     * 전체 회원 수 조회
     */
    int countAll();
}
```

### 2. Mapper with Multiple Parameters
```java
@Mapper
public interface MemberMapper {

    /**
     * 키워드로 회원 검색
     */
    List<MemberDTO> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 페이징 조회
     */
    List<MemberDTO> selectPage(
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    /**
     * 날짜 범위로 조회
     */
    List<MemberDTO> selectByDateRange(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );

    /**
     * 동적 조건 검색
     */
    List<MemberDTO> searchByConditions(
        @Param("name") String name,
        @Param("email") String email,
        @Param("status") String status
    );
}
```

### 3. Mapper with Complex Operations
```java
@Mapper
public interface OrderMapper {

    /**
     * 주문 및 상세 정보 조회 (JOIN)
     */
    OrderDTO selectOrderWithDetails(Long orderId);

    /**
     * 회원별 주문 목록
     */
    List<OrderDTO> selectByMemberId(@Param("memberId") Long memberId);

    /**
     * 주문 상태 일괄 업데이트
     */
    void updateStatusBulk(
        @Param("orderIds") List<Long> orderIds,
        @Param("status") String status
    );

    /**
     * 재고 차감
     */
    void decreaseStock(
        @Param("productId") Long productId,
        @Param("quantity") int quantity
    );
}
```

## Common Patterns

### Single Parameter
```java
// 파라미터가 1개일 때 @Param 생략 가능
MemberDTO selectById(Long id);
String selectNameById(Long id);
```

### Multiple Parameters
```java
// 파라미터가 2개 이상일 때 @Param 필수
List<MemberDTO> selectByNameAndEmail(
    @Param("name") String name,
    @Param("email") String email
);
```

### DTO Parameter
```java
// DTO 객체를 파라미터로 받을 때
void insert(MemberDTO member);
void update(MemberDTO member);
```

### List Parameter
```java
// List를 파라미터로 받을 때 (동적 SQL 활용)
void deleteByIds(@Param("ids") List<Long> ids);
List<MemberDTO> selectByIds(@Param("ids") List<Long> ids);
```

### Return Types
```java
// 다양한 반환 타입
List<MemberDTO> selectAll();           // List
MemberDTO selectById(Long id);         // Single Object (null 가능)
int countAll();                        // int (COUNT)
void insert(MemberDTO member);         // void (INSERT/UPDATE/DELETE)
Long insertAndGetId(MemberDTO member); // Auto-generated ID 반환
```

# Testing Strategy

## Test Annotations
- @MybatisTest: Mapper 단위 테스트 (최적화)
- @SpringBootTest: 전체 통합 테스트
- @AutoConfigureTestDatabase: 실제 DB 사용 설정

## Test Template
```java
package com.example.demo.mapper;

import org.junit.jupiter.api.Test;
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
    void testSelectAll() {
        // when
        List<MemberDTO> list = memberMapper.selectAll();

        // then
        assertThat(list).isNotNull();
    }

    @Test
    void testSelectById() {
        // when
        MemberDTO member = memberMapper.selectById(1L);

        // then
        assertThat(member).isNotNull();
        assertThat(member.getId()).isEqualTo(1L);
    }

    @Test
    void testInsert() {
        // given
        MemberDTO dto = MemberDTO.builder()
            .name("테스터")
            .email("test@example.com")
            .build();

        // when
        memberMapper.insert(dto);

        // then
        assertThat(dto.getId()).isNotNull(); // Auto-generated ID
    }

    @Test
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
    void testDelete() {
        // when
        memberMapper.deleteById(1L);

        // then
        MemberDTO deleted = memberMapper.selectById(1L);
        assertThat(deleted).isNull();
    }
}
```

## Test Commands
```bash
# Mapper 테스트만 실행
./mvnw test -Dtest=*Mapper*Test
```

# XML Mapper Connection

## Namespace Mapping
XML 매퍼의 namespace는 인터페이스 풀 패키지명과 일치해야 합니다.

```xml
<!-- MemberMapper.xml -->
<mapper namespace="com.example.demo.mapper.MemberMapper">
```

## Method Mapping
XML의 id는 인터페이스 메서드명과 일치해야 합니다.

```java
// Interface
List<MemberDTO> selectAll();
```

```xml
<!-- XML -->
<select id="selectAll" resultType="MemberDTO">
    SELECT * FROM member
</select>
```

## Parameter Mapping
```java
// Single Parameter
MemberDTO selectById(Long id);
```

```xml
<select id="selectById" resultType="MemberDTO">
    SELECT * FROM member WHERE id = #{id}
</select>
```

```java
// Multiple Parameters with @Param
List<MemberDTO> selectByNameAndEmail(
    @Param("name") String name,
    @Param("email") String email
);
```

```xml
<select id="selectByNameAndEmail" resultType="MemberDTO">
    SELECT * FROM member
    WHERE name = #{name} AND email = #{email}
</select>
```

# Local Golden Rules

## Do's
- 모든 Mapper 인터페이스에 @Mapper 어노테이션 필수.
- 파라미터가 2개 이상이면 반드시 @Param 사용.
- 메서드명과 XML id를 동일하게 유지.
- JavaDoc으로 메서드 역할 명시.
- void 반환 타입은 INSERT/UPDATE/DELETE에만 사용.

## Don'ts
- Mapper 인터페이스에 구현 코드 작성 금지 (순수 인터페이스 유지).
- SQL 쿼리를 @Select, @Insert 등 어노테이션으로 작성하지 마라 (XML 사용 권장).
- 비즈니스 로직을 default 메서드로 구현하지 마라.
- XML Mapper와 네임스페이스가 불일치하면 런타임 에러 발생.

## Common Mistakes
- @Mapper 어노테이션 누락 시 빈 등록 안 됨.
- @Param 누락 시 `param1`, `param2`로 자동 매핑됨 (명시성 감소).
- XML id와 메서드명 불일치 시 `BindingException`.
- resultType 오타 또는 잘못된 타입 지정 시 매핑 실패.
- 파라미터 타입 불일치 시 런타임 에러.
