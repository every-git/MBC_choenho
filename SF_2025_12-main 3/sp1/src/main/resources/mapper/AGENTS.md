# Mapper Layer Context

## Module Context

Mapper 계층은 MyBatis를 사용하여 데이터베이스 접근을 담당합니다. 인터페이스는 Java에, SQL 쿼리는 XML에 작성합니다.

**의존성 관계:**
- Mapper Interface -> MyBatis (의존)
- Mapper XML -> MyBatis (의존)
- Mapper는 Service 계층에서만 호출됨

## Tech Stack & Constraints

- MyBatis 3.5.19
- MyBatis-Spring 3.0.4
- MySQL Connector 9.4.0
- HikariCP 7.0.2 (커넥션 풀)

**제약사항:**
- SQL은 반드시 XML 파일에 작성
- 인터페이스 메서드명과 XML의 id는 일치해야 함
- 네임스페이스는 인터페이스의 패키지 경로와 일치

## Implementation Patterns

**Mapper 인터페이스 패턴:**
```java
@Mapper
public interface ReplyMapper {
    int insert(@Param("replyDTO") ReplyDTO replyDTO);
    ReplyDTO read(@Param("rno") int rno);
    int delete(@Param("rno") int rno);
    int update(@Param("replyDTO") ReplyDTO replyDTO);
    List<ReplyDTO> listOfBoard(@Param("bno") Long bno, 
                               @Param("skip") int skip, 
                               @Param("size") int size);
    int countOfBoard(@Param("bno") Long bno);
}
```

**Mapper XML 패턴:**
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.zerock.mapper.ReplyMapper">
  <resultMap id="replyResultMap" type="org.zerock.dto.ReplyDTO">
    <id property="rno" column="rno"/>
    <result property="replyText" column="replyText"/>
  </resultMap>
  
  <insert id="insert">
    <selectKey order="AFTER" keyProperty="rno" resultType="int">
      select last_insert_id()
    </selectKey>
    insert into tbl_reply(bno, replyText, replyer)
    values(#{bno}, #{replyText}, #{replyer})
  </insert>
</mapper>
```

**파일 네이밍:**
- 인터페이스: `{Entity}Mapper.java` (예: `BoardMapper.java`)
- XML: `{Entity}Mapper.xml` (예: `boardMapper.xml`, `ReplyMapper.xml`)
- XML은 `src/main/resources/mapper/` 디렉토리에 위치

## Testing Strategy

Mapper 테스트는 통합 테스트로 작성:
- `@ExtendWith(SpringExtension.class)` 사용
- `@ContextConfiguration`으로 Spring 컨텍스트 로드
- 실제 데이터베이스 연결 사용

## Local Golden Rules

**Do's:**
- 파라미터는 `@Param` 어노테이션 사용
- INSERT 후 `selectKey`로 생성된 키 가져오기
- 삭제는 UPDATE로 `delflag = true` 설정
- 동적 쿼리는 `<if>`, `<foreach>` 태그 활용
- 공통 SQL은 `<sql>` 태그로 정의 후 `<include>` 사용

**Don'ts:**
- SQL을 Java 코드에 하드코딩하지 말 것
- 파라미터 바인딩 시 `#{}` 사용 (SQL Injection 방지)
- `${}` 사용 시 주의 (문자열 치환, 보안 위험)
- 네임스페이스와 인터페이스 경로 불일치 금지

