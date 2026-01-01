# Module Context

MyBatis XML Mapper는 SQL 쿼리를 정의하고 실행하는 핵심 파일입니다.
Java Mapper 인터페이스와 1:1 매핑되며, 동적 SQL, ResultMap, 파라미터 바인딩을 제공합니다.

## Dependencies
- MyBatis 3.0.5
- MySQL Connector

## Responsibilities
- SQL 쿼리 작성 (SELECT, INSERT, UPDATE, DELETE)
- 동적 SQL 구성 (if, choose, foreach)
- ResultMap 정의 (복잡한 매핑)
- 파라미터 바인딩 (#{} vs ${})

## What NOT to Do
- 비즈니스 로직 포함 (Service 책임)
- Java 코드 작성 (Mapper Interface 책임)
- 직접적인 예외 처리 (MyBatis가 자동 처리)

# Tech Stack & Constraints

## XML Structure
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.demo.mapper.{Entity}Mapper">
    <!-- SQL 쿼리들 -->
</mapper>
```

## Required Configuration
- Namespace: 반드시 Mapper 인터페이스 풀 패키지명과 일치
- DOCTYPE: MyBatis DTD 선언 필수
- Encoding: UTF-8

# Implementation Patterns

## File Naming
- XML 파일명: `{Entity}Mapper.xml`
- 예: MemberMapper.xml, OrderMapper.xml

## File Location
```
src/main/resources/mapper/
```

## Template Pattern

### 1. Basic CRUD Queries
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.demo.mapper.MemberMapper">

    <!-- 전체 목록 조회 -->
    <select id="selectAll" resultType="MemberDTO">
        SELECT id, name, email, phone, address
        FROM member
        ORDER BY id DESC
    </select>

    <!-- ID로 조회 -->
    <select id="selectById" resultType="MemberDTO">
        SELECT id, name, email, phone, address
        FROM member
        WHERE id = #{id}
    </select>

    <!-- 등록 -->
    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO member (name, email, phone, address)
        VALUES (#{name}, #{email}, #{phone}, #{address})
    </insert>

    <!-- 수정 -->
    <update id="update">
        UPDATE member
        SET name = #{name},
            email = #{email},
            phone = #{phone},
            address = #{address}
        WHERE id = #{id}
    </update>

    <!-- 삭제 -->
    <delete id="deleteById">
        DELETE FROM member
        WHERE id = #{id}
    </delete>

    <!-- 전체 개수 조회 -->
    <select id="countAll" resultType="int">
        SELECT COUNT(*)
        FROM member
    </select>

</mapper>
```

### 2. Dynamic SQL with <if>
```xml
<!-- 동적 검색 조건 -->
<select id="searchByConditions" resultType="MemberDTO">
    SELECT id, name, email, phone, address
    FROM member
    <where>
        <if test="name != null and name != ''">
            AND name LIKE CONCAT('%', #{name}, '%')
        </if>
        <if test="email != null and email != ''">
            AND email LIKE CONCAT('%', #{email}, '%')
        </if>
        <if test="status != null and status != ''">
            AND status = #{status}
        </if>
    </where>
    ORDER BY id DESC
</select>
```

### 3. Dynamic SQL with <foreach>
```xml
<!-- IN 절을 사용한 다중 ID 조회 -->
<select id="selectByIds" resultType="MemberDTO">
    SELECT id, name, email, phone, address
    FROM member
    WHERE id IN
    <foreach item="id" collection="ids" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>

<!-- 일괄 삭제 -->
<delete id="deleteByIds">
    DELETE FROM member
    WHERE id IN
    <foreach item="id" collection="ids" open="(" separator="," close=")">
        #{id}
    </foreach>
</delete>
```

### 4. ResultMap for Complex Mapping
```xml
<!-- 복잡한 매핑 정의 -->
<resultMap id="orderResultMap" type="OrderDTO">
    <id property="id" column="order_id"/>
    <result property="memberId" column="member_id"/>
    <result property="memberName" column="member_name"/>
    <result property="orderDate" column="order_date"/>
    <result property="status" column="status"/>
    <result property="totalAmount" column="total_amount"/>

    <!-- 1:N 관계 매핑 -->
    <collection property="orderItems" ofType="OrderItemDTO">
        <id property="id" column="item_id"/>
        <result property="productId" column="product_id"/>
        <result property="productName" column="product_name"/>
        <result property="quantity" column="quantity"/>
        <result property="price" column="price"/>
    </collection>
</resultMap>

<!-- JOIN 쿼리 -->
<select id="selectOrderWithDetails" resultMap="orderResultMap">
    SELECT
        o.id AS order_id,
        o.member_id,
        m.name AS member_name,
        o.order_date,
        o.status,
        o.total_amount,
        oi.id AS item_id,
        oi.product_id,
        p.name AS product_name,
        oi.quantity,
        oi.price
    FROM orders o
    INNER JOIN member m ON o.member_id = m.id
    LEFT JOIN order_item oi ON o.id = oi.order_id
    LEFT JOIN product p ON oi.product_id = p.id
    WHERE o.id = #{orderId}
</select>
```

### 5. Pagination
```xml
<!-- 페이징 조회 -->
<select id="selectPage" resultType="MemberDTO">
    SELECT id, name, email, phone, address
    FROM member
    ORDER BY id DESC
    LIMIT #{limit} OFFSET #{offset}
</select>
```

### 6. <choose> for Multiple Conditions
```xml
<!-- 조건별 정렬 -->
<select id="selectBySort" resultType="MemberDTO">
    SELECT id, name, email, phone, address
    FROM member
    ORDER BY
    <choose>
        <when test="sortBy == 'name'">
            name ASC
        </when>
        <when test="sortBy == 'email'">
            email ASC
        </when>
        <otherwise>
            id DESC
        </otherwise>
    </choose>
</select>
```

## Common Patterns

### Parameter Binding: #{} vs ${}
```xml
<!-- #{}: PreparedStatement (권장) - SQL Injection 방지 -->
<select id="selectById" resultType="MemberDTO">
    SELECT * FROM member WHERE id = #{id}
</select>

<!-- ${}: String Substitution (위험) - 동적 테이블명/컬럼명에만 사용 -->
<select id="selectByColumn" resultType="MemberDTO">
    SELECT * FROM ${tableName} WHERE ${columnName} = #{value}
</select>
```

### Auto-Generated Keys
```xml
<!-- INSERT 후 자동 생성된 ID 반환 -->
<insert id="insert" useGeneratedKeys="true" keyProperty="id">
    INSERT INTO member (name, email)
    VALUES (#{name}, #{email})
</insert>
```

### CDATA for Special Characters
```xml
<!-- <, > 등 특수문자 사용 시 -->
<select id="selectByRange" resultType="MemberDTO">
    SELECT * FROM member
    <![CDATA[
    WHERE age >= #{minAge} AND age <= #{maxAge}
    ]]>
</select>
```

### Reusable SQL Fragment
```xml
<!-- 공통 SQL 조각 정의 -->
<sql id="memberColumns">
    id, name, email, phone, address, created_at, updated_at
</sql>

<!-- 재사용 -->
<select id="selectAll" resultType="MemberDTO">
    SELECT <include refid="memberColumns"/>
    FROM member
</select>

<select id="selectById" resultType="MemberDTO">
    SELECT <include refid="memberColumns"/>
    FROM member
    WHERE id = #{id}
</select>
```

### Batch Insert
```xml
<!-- 일괄 등록 -->
<insert id="insertBulk">
    INSERT INTO member (name, email)
    VALUES
    <foreach item="item" collection="list" separator=",">
        (#{item.name}, #{item.email})
    </foreach>
</insert>
```

# Testing Strategy

## Test Approach
XML Mapper는 Mapper 인터페이스 테스트를 통해 간접적으로 검증됩니다.
SQL 문법 오류는 런타임에 발견되므로, 테스트 커버리지가 중요합니다.

## Validation Checklist
- [ ] namespace가 Mapper 인터페이스와 일치하는가?
- [ ] 모든 SQL id가 Mapper 메서드명과 일치하는가?
- [ ] resultType 또는 resultMap이 올바르게 지정되었는가?
- [ ] #{} 파라미터 바인딩이 올바른가?
- [ ] 동적 SQL 조건이 정확한가?

## Common SQL Testing
```bash
# MySQL에서 직접 쿼리 테스트
mysql -u springdbuser -p springdb

# 쿼리 실행 예제
SELECT id, name, email FROM member WHERE id = 1;
SELECT COUNT(*) FROM member;
```

# Local Golden Rules

## Do's
- 항상 #{} 파라미터 바인딩 사용 (SQL Injection 방지).
- 복잡한 JOIN이나 중첩 객체는 ResultMap 사용.
- 동적 SQL은 <where>, <set>, <trim> 태그로 안전하게 작성.
- 컬럼명은 snake_case, DTO 필드는 camelCase (자동 매핑).
- SQL 가독성을 위해 들여쓰기 및 줄바꿈 활용.

## Don'ts
- ${}를 파라미터 값에 사용하지 마라 (SQL Injection 위험).
- SELECT * 사용 금지 (명시적 컬럼 지정 권장).
- XML에 비즈니스 로직(복잡한 CASE WHEN 등) 과도하게 포함 금지.
- resultType과 resultMap을 동시에 사용하지 마라.
- 하드코딩된 값을 직접 쿼리에 포함하지 마라 (파라미터 사용).

## Common Mistakes
- namespace 오타 또는 불일치 시 `BindingException`.
- SQL id와 Mapper 메서드명 불일치 시 `BindingException`.
- resultType 오타 시 `ClassNotFoundException` 또는 매핑 실패.
- #{} 대신 ${} 사용 시 SQL Injection 위험.
- <where> 태그 없이 AND로 시작하는 동적 SQL 작성 시 문법 오류.
- useGeneratedKeys 누락 시 INSERT 후 ID 반환 불가.
- CDATA 누락으로 인한 XML 파싱 오류.

## Performance Tips
- 필요한 컬럼만 SELECT (SELECT * 지양).
- INDEX가 있는 컬럼으로 WHERE 조건 작성.
- JOIN 시 적절한 INDEX 사용 확인.
- 페이징 시 LIMIT/OFFSET 활용.
- 대량 데이터 INSERT는 BATCH INSERT 사용.

## XML Formatting
```xml
<!-- Good: 가독성 좋은 포맷 -->
<select id="selectAll" resultType="MemberDTO">
    SELECT
        id,
        name,
        email,
        phone,
        address
    FROM member
    WHERE status = 'ACTIVE'
    ORDER BY created_at DESC
</select>

<!-- Bad: 한 줄로 작성 -->
<select id="selectAll" resultType="MemberDTO">SELECT id, name, email FROM member WHERE status = 'ACTIVE'</select>
```
