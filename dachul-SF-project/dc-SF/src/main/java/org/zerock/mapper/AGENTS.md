# Module Context

MyBatis Mapper Layer. 데이터베이스 접근 계층. Interface + XML 구조.

## Role & Responsibilities

- SQL 쿼리 실행
- 결과를 DTO로 매핑
- CRUD 작업
- 동적 SQL 생성

## Dependencies

- MyBatis 3.5.17
- mybatis-spring 3.0.4
- MySQL Connector
- DTO (결과 매핑)

# Tech Stack & Constraints

## MyBatis Configuration

- Mapper Interface: src/main/java/org/zerock/mapper/
- Mapper XML: src/main/resources/mapper/
- Interface와 XML 파일명 동일 필수

## Required Setup

- mybatis-config.xml: TypeAlias 설정
- root-context.xml: SqlSessionFactory, MapperScan 설정

# Implementation Patterns

## Mapper Interface

```java
package org.zerock.mapper;

import org.apache.ibatis.annotations.Param;
import org.zerock.dto.XxxDTO;

import java.util.List;

public interface XxxMapper {

    // 전체 조회
    List<XxxDTO> selectAll();

    // ID로 단건 조회
    XxxDTO selectById(Long id);

    // 조건부 조회
    List<XxxDTO> selectByCondition(@Param("name") String name,
                                    @Param("status") String status);

    // 삽입
    void insert(XxxDTO dto);

    // 수정
    int update(XxxDTO dto);

    // 삭제
    int delete(Long id);

    // 카운트
    int count();
}
```

## Mapper XML - Basic CRUD

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="org.zerock.mapper.XxxMapper">

    <!-- ResultMap 정의 -->
    <resultMap id="xxxResultMap" type="org.zerock.dto.XxxDTO">
        <id property="id" column="id" />
        <result property="name" column="name" />
        <result property="description" column="description" />
        <result property="status" column="status" />
        <result property="createdAt" column="created_at" />
        <result property="updatedAt" column="updated_at" />
    </resultMap>

    <!-- 전체 조회 -->
    <select id="selectAll" resultMap="xxxResultMap">
        SELECT id, name, description, status, created_at, updated_at
        FROM xxx_table
        ORDER BY created_at DESC
    </select>

    <!-- ID로 단건 조회 -->
    <select id="selectById" parameterType="long" resultMap="xxxResultMap">
        SELECT id, name, description, status, created_at, updated_at
        FROM xxx_table
        WHERE id = #{id}
    </select>

    <!-- 조건부 조회 -->
    <select id="selectByCondition" resultMap="xxxResultMap">
        SELECT id, name, description, status, created_at, updated_at
        FROM xxx_table
        WHERE 1=1
        <if test="name != null and name != ''">
            AND name LIKE CONCAT('%', #{name}, '%')
        </if>
        <if test="status != null and status != ''">
            AND status = #{status}
        </if>
        ORDER BY created_at DESC
    </select>

    <!-- 삽입 (자동 생성된 키 반환) -->
    <insert id="insert" parameterType="org.zerock.dto.XxxDTO"
            useGeneratedKeys="true" keyProperty="id">
        INSERT INTO xxx_table (name, description, status, created_at, updated_at)
        VALUES (#{name}, #{description}, #{status}, NOW(), NOW())
    </insert>

    <!-- 수정 -->
    <update id="update" parameterType="org.zerock.dto.XxxDTO">
        UPDATE xxx_table
        SET name = #{name},
            description = #{description},
            status = #{status},
            updated_at = NOW()
        WHERE id = #{id}
    </update>

    <!-- 삭제 -->
    <delete id="delete" parameterType="long">
        DELETE FROM xxx_table
        WHERE id = #{id}
    </delete>

    <!-- 카운트 -->
    <select id="count" resultType="int">
        SELECT COUNT(*) FROM xxx_table
    </select>

</mapper>
```

## Dynamic SQL Patterns

### If/Choose/When

```xml
<select id="selectByDynamicCondition" resultMap="xxxResultMap">
    SELECT * FROM xxx_table
    WHERE 1=1
    <if test="name != null">
        AND name = #{name}
    </if>
    <choose>
        <when test="status == 'ACTIVE'">
            AND status = 'ACTIVE'
        </when>
        <when test="status == 'INACTIVE'">
            AND status = 'INACTIVE'
        </when>
        <otherwise>
            AND status IN ('ACTIVE', 'INACTIVE')
        </otherwise>
    </choose>
</select>
```

### Where/Set

```xml
<select id="selectWithWhere" resultMap="xxxResultMap">
    SELECT * FROM xxx_table
    <where>
        <if test="name != null">
            AND name = #{name}
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
    </where>
</select>

<update id="updateDynamic">
    UPDATE xxx_table
    <set>
        <if test="name != null">name = #{name},</if>
        <if test="description != null">description = #{description},</if>
        <if test="status != null">status = #{status},</if>
        updated_at = NOW()
    </set>
    WHERE id = #{id}
</update>
```

### Foreach (Batch Operations)

```xml
<insert id="insertBatch" parameterType="list">
    INSERT INTO xxx_table (name, description, status, created_at)
    VALUES
    <foreach collection="list" item="item" separator=",">
        (#{item.name}, #{item.description}, #{item.status}, NOW())
    </foreach>
</insert>

<delete id="deleteByIds" parameterType="list">
    DELETE FROM xxx_table
    WHERE id IN
    <foreach collection="list" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</delete>

<select id="selectByIds" resultMap="xxxResultMap">
    SELECT * FROM xxx_table
    WHERE id IN
    <foreach collection="list" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>
```

## Association/Collection (JOIN)

### One-to-One

```xml
<resultMap id="memberWithProfile" type="org.zerock.dto.MemberDTO">
    <id property="id" column="member_id" />
    <result property="username" column="username" />
    <association property="profile" javaType="org.zerock.dto.ProfileDTO">
        <id property="id" column="profile_id" />
        <result property="bio" column="bio" />
        <result property="avatarUrl" column="avatar_url" />
    </association>
</resultMap>

<select id="selectMemberWithProfile" resultMap="memberWithProfile">
    SELECT
        m.id AS member_id,
        m.username,
        p.id AS profile_id,
        p.bio,
        p.avatar_url
    FROM member m
    LEFT JOIN profile p ON m.id = p.member_id
    WHERE m.id = #{id}
</select>
```

### One-to-Many

```xml
<resultMap id="boardWithComments" type="org.zerock.dto.BoardDTO">
    <id property="id" column="board_id" />
    <result property="title" column="title" />
    <result property="content" column="content" />
    <collection property="comments" ofType="org.zerock.dto.CommentDTO">
        <id property="id" column="comment_id" />
        <result property="content" column="comment_content" />
        <result property="createdAt" column="comment_created_at" />
    </collection>
</resultMap>

<select id="selectBoardWithComments" resultMap="boardWithComments">
    SELECT
        b.id AS board_id,
        b.title,
        b.content,
        c.id AS comment_id,
        c.content AS comment_content,
        c.created_at AS comment_created_at
    FROM board b
    LEFT JOIN comment c ON b.id = c.board_id
    WHERE b.id = #{id}
    ORDER BY c.created_at ASC
</select>
```

# Local Golden Rules

## Pagination Pattern

### Mapper Interface

```java
@Mapper
public interface XxxMapper {
    
    // 페이징 조회
    List<XxxDTO> selectWithPaging(@Param("skip") int skip, @Param("size") int size);
    
    // 전체 개수 조회
    int count();
}
```

### Mapper XML

```xml
<!-- 페이징 조회 -->
<select id="selectWithPaging" resultType="org.zerock.dto.XxxDTO">
    SELECT seq, writer, title, content, hit, regdate, updatedate, delflag
    FROM board
    WHERE delflag = false
    ORDER BY seq DESC
    LIMIT #{size} OFFSET #{skip}
</select>

<!-- 전체 개수 조회 -->
<select id="count" resultType="int">
    SELECT COUNT(seq)
    FROM board
    WHERE delflag = false
</select>
```

### 페이징 계산 공식

```java
// Service에서 계산
int skip = (page - 1) * size;
// page=1, size=10 -> skip=0
// page=2, size=10 -> skip=10
// page=3, size=10 -> skip=20
```

## Security - SQL Injection Prevention

### Do's
- #{} 파라미터 바인딩 사용 (PreparedStatement)
- @Param 어노테이션으로 명시적 파라미터명 지정
- 복수 파라미터 사용 시 반드시 @Param 사용
- import org.apache.ibatis.annotations.Param; 필수

### Don'ts
- ${} 사용 절대 금지 (SQL Injection 위험)
- 동적 테이블명/컬럼명이 필요하면 Java에서 검증 후 전달
- @Param 어노테이션 생략 금지 (복수 파라미터 시)

```xml
<!-- GOOD -->
<select id="selectByName" resultMap="xxxResultMap">
    SELECT * FROM xxx_table WHERE name = #{name}
</select>

<!-- BAD - SQL Injection 위험 -->
<select id="selectByNameBad" resultMap="xxxResultMap">
    SELECT * FROM xxx_table WHERE name = ${name}
</select>
```

## Performance

### Do's
- SELECT * 지양. 필요한 컬럼만 명시
- JOIN 시 필요한 컬럼만 선택
- 페이징 처리 (LIMIT/OFFSET)
- 인덱스가 걸린 컬럼으로 검색

### Don'ts
- N+1 쿼리 발생 방지 (JOIN 또는 Association 사용)
- 대량 데이터 한 번에 조회 금지
- WHERE 절 없는 UPDATE/DELETE 금지

## Naming Conventions

### Mapper Interface Method
- select*: 조회
- insert*: 삽입
- update*: 수정
- delete*: 삭제
- count*: 카운트

### XML id
- Interface 메서드명과 동일

### Parameter Naming
- 단일 파라미터: 타입명 또는 의미있는 이름
- 복수 파라미터: @Param 사용

## Result Mapping

### Do's
- resultMap 정의 후 재사용
- column과 property 명시적 매핑
- TypeAlias 활용 (mybatis-config.xml)

### Don'ts
- HashMap으로 결과 받기 금지
- resultType과 resultMap 혼용 주의

# Testing Strategy

## Mapper Test Pattern

```java
@SpringJUnitConfig(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
class XxxMapperTest {

    @Autowired
    private XxxMapper xxxMapper;

    @Test
    void testSelectAll() {
        List<XxxDTO> list = xxxMapper.selectAll();
        assertNotNull(list);
        list.forEach(System.out::println);
    }

    @Test
    void testSelectById() {
        XxxDTO dto = xxxMapper.selectById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
    }

    @Test
    void testInsert() {
        XxxDTO dto = XxxDTO.builder()
            .name("Test")
            .description("Test Description")
            .status("ACTIVE")
            .build();

        xxxMapper.insert(dto);

        assertNotNull(dto.getId());
        System.out.println("Generated ID: " + dto.getId());
    }

    @Test
    void testUpdate() {
        XxxDTO dto = xxxMapper.selectById(1L);
        dto.setName("Updated");

        int count = xxxMapper.update(dto);

        assertEquals(1, count);

        XxxDTO updated = xxxMapper.selectById(1L);
        assertEquals("Updated", updated.getName());
    }

    @Test
    void testDelete() {
        int count = xxxMapper.delete(1L);
        assertEquals(1, count);

        XxxDTO deleted = xxxMapper.selectById(1L);
        assertNull(deleted);
    }
}
```

# Related Contexts

- Service Layer: ../service/AGENTS.md
- DTO: ../dto/AGENTS.md
- MyBatis Configuration: ../../../resources/AGENTS.md
