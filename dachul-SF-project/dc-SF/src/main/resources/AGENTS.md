# Module Context

MyBatis & Logging Configuration. 리소스 파일 관리.

## Role & Responsibilities

- MyBatis 설정 (mybatis-config.xml)
- Mapper XML 파일 관리
- 로깅 설정 (log4j2.xml)
- 기타 리소스 파일

## Directory Structure

```
resources/
├── mybatis-config.xml     # MyBatis 전역 설정
├── log4j2.xml             # Log4j2 설정
└── mapper/                # MyBatis Mapper XML 파일들
    ├── MemberMapper.xml
    ├── BoardMapper.xml
    └── ...
```

# Implementation Patterns

## mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

    <!-- Settings -->
    <settings>
        <!-- 카멜 케이스 자동 변환 (DB: user_name -> Java: userName) -->
        <setting name="mapUnderscoreToCamelCase" value="true" />

        <!-- JDBC NULL 타입 지정 -->
        <setting name="jdbcTypeForNull" value="NULL" />

        <!-- 로깅 구현체 지정 -->
        <setting name="logImpl" value="SLF4J" />

        <!-- 캐시 활성화 -->
        <setting name="cacheEnabled" value="true" />

        <!-- Lazy Loading -->
        <setting name="lazyLoadingEnabled" value="true" />

        <!-- 공격적 Lazy Loading 비활성화 -->
        <setting name="aggressiveLazyLoading" value="false" />
    </settings>

    <!-- Type Aliases -->
    <typeAliases>
        <package name="org.zerock.dto" />
    </typeAliases>

</configuration>
```

## log4j2.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">

    <Appenders>
        <!-- Console Appender -->
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n" />
        </Console>

        <!-- File Appender -->
        <RollingFile name="RollingFile"
                     fileName="logs/app.log"
                     filePattern="logs/app-%d{yyyy-MM-dd}.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n" />
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true" />
                <SizeBasedTriggeringPolicy size="10MB" />
            </Policies>
            <DefaultRolloverStrategy max="30" />
        </RollingFile>
    </Appenders>

    <Loggers>
        <!-- Application Logger -->
        <Logger name="org.zerock" level="DEBUG" additivity="false">
            <AppenderRef ref="Console" />
            <AppenderRef ref="RollingFile" />
        </Logger>

        <!-- MyBatis Logger -->
        <Logger name="org.zerock.mapper" level="TRACE" additivity="false">
            <AppenderRef ref="Console" />
        </Logger>

        <!-- SQL Logger -->
        <Logger name="java.sql" level="DEBUG" additivity="false">
            <AppenderRef ref="Console" />
        </Logger>

        <!-- Spring Framework Logger -->
        <Logger name="org.springframework" level="INFO" additivity="false">
            <AppenderRef ref="Console" />
        </Logger>

        <!-- Spring Security Logger -->
        <Logger name="org.springframework.security" level="DEBUG" additivity="false">
            <AppenderRef ref="Console" />
        </Logger>

        <!-- HikariCP Logger -->
        <Logger name="com.zaxxer.hikari" level="INFO" additivity="false">
            <AppenderRef ref="Console" />
        </Logger>

        <!-- Root Logger -->
        <Root level="INFO">
            <AppenderRef ref="Console" />
            <AppenderRef ref="RollingFile" />
        </Root>
    </Loggers>

</Configuration>
```

## Mapper XML Template

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="org.zerock.mapper.XxxMapper">

    <!-- ResultMap -->
    <resultMap id="xxxResultMap" type="XxxDTO">
        <id property="id" column="id" />
        <result property="name" column="name" />
        <result property="description" column="description" />
        <result property="createdAt" column="created_at" />
        <result property="updatedAt" column="updated_at" />
    </resultMap>

    <!-- Select -->
    <select id="selectAll" resultMap="xxxResultMap">
        SELECT id, name, description, created_at, updated_at
        FROM xxx_table
        ORDER BY created_at DESC
    </select>

    <!-- Insert -->
    <insert id="insert" parameterType="XxxDTO" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO xxx_table (name, description, created_at, updated_at)
        VALUES (#{name}, #{description}, NOW(), NOW())
    </insert>

    <!-- Update -->
    <update id="update" parameterType="XxxDTO">
        UPDATE xxx_table
        SET name = #{name},
            description = #{description},
            updated_at = NOW()
        WHERE id = #{id}
    </update>

    <!-- Delete -->
    <delete id="delete" parameterType="long">
        DELETE FROM xxx_table WHERE id = #{id}
    </delete>

</mapper>
```

# Local Golden Rules

## MyBatis Configuration

### Settings Best Practices

#### Do's
- mapUnderscoreToCamelCase=true 활성화 (DB 컬럼명 자동 변환)
- jdbcTypeForNull=NULL 설정 (NULL 값 처리)
- cacheEnabled=true (2차 캐시 활성화)
- lazyLoadingEnabled=true (지연 로딩 활성화)

#### Don'ts
- defaultExecutorType을 BATCH로 설정 금지 (예상치 못한 동작 발생 가능)
- autoMappingBehavior=FULL 지양 (보안 위험)

### Type Aliases

#### Do's
- package 스캔 사용 (간편)
- DTO 클래스명을 alias로 자동 사용

#### Don'ts
- 개별 typeAlias 남발 금지

```xml
<!-- GOOD -->
<typeAliases>
    <package name="org.zerock.dto" />
</typeAliases>

<!-- BAD -->
<typeAliases>
    <typeAlias type="org.zerock.dto.MemberDTO" alias="MemberDTO" />
    <typeAlias type="org.zerock.dto.BoardDTO" alias="BoardDTO" />
    ...
</typeAliases>
```

## Mapper XML Organization

### File Naming
- Interface: org.zerock.mapper.MemberMapper
- XML: src/main/resources/mapper/MemberMapper.xml
- 이름 일치 필수

### Namespace
- Mapper Interface 풀 패키지명 사용

```xml
<mapper namespace="org.zerock.mapper.MemberMapper">
```

### SQL ID
- Mapper Interface 메서드명과 동일

## Logging Configuration

### Log Levels

- TRACE: 가장 상세한 로그 (SQL 파라미터 등)
- DEBUG: 디버깅 정보
- INFO: 일반 정보
- WARN: 경고
- ERROR: 에러

### Logger Configuration Best Practices

#### Development Environment
```xml
<Logger name="org.zerock" level="DEBUG" />
<Logger name="org.zerock.mapper" level="TRACE" />  <!-- SQL 상세 로그 -->
<Logger name="java.sql" level="DEBUG" />  <!-- JDBC 로그 -->
```

#### Production Environment
```xml
<Logger name="org.zerock" level="INFO" />
<Logger name="org.zerock.mapper" level="INFO" />
<Logger name="java.sql" level="WARN" />
```

### Do's
- Console과 File Appender 함께 사용
- 로그 파일 rotation 설정 (날짜별, 크기별)
- 민감 정보 로깅 금지
- 적절한 로그 레벨 사용

### Don'ts
- 운영 환경에서 DEBUG 레벨 사용 금지
- 로그 파일 무제한 증가 방지
- System.out.println() 사용 금지

## SQL Logging

### MyBatis SQL 로그 상세 출력

```xml
<!-- SQL 쿼리 + 파라미터 함께 출력 -->
<Logger name="org.zerock.mapper" level="TRACE" />
```

출력 예시:
```
DEBUG org.zerock.mapper.MemberMapper.selectById - ==>  Preparing: SELECT * FROM member WHERE id = ?
DEBUG org.zerock.mapper.MemberMapper.selectById - ==> Parameters: 1(Long)
TRACE org.zerock.mapper.MemberMapper.selectById - <==    Columns: id, username, password, email, created_at
TRACE org.zerock.mapper.MemberMapper.selectById - <==        Row: 1, testuser, $2a$10$..., test@example.com, 2024-12-30 10:00:00
DEBUG org.zerock.mapper.MemberMapper.selectById - <==      Total: 1
```

# Testing Strategy

## MyBatis Configuration Test

```java
@SpringJUnitConfig(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
class MyBatisConfigTest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void testTypeAliases() {
        Configuration config = sqlSessionFactory.getConfiguration();

        // TypeAlias 등록 확인
        TypeAliasRegistry typeAliasRegistry = config.getTypeAliasRegistry();
        assertNotNull(typeAliasRegistry.getTypeAliases().get("memberDTO"));
        assertNotNull(typeAliasRegistry.getTypeAliases().get("boardDTO"));
    }

    @Test
    void testMapperRegistered() {
        Configuration config = sqlSessionFactory.getConfiguration();

        // Mapper 등록 확인
        assertTrue(config.hasMapper(MemberMapper.class));
        assertTrue(config.hasMapper(BoardMapper.class));
    }

    @Test
    void testSettings() {
        Configuration config = sqlSessionFactory.getConfiguration();

        // mapUnderscoreToCamelCase 설정 확인
        assertTrue(config.isMapUnderscoreToCamelCase());

        // cacheEnabled 설정 확인
        assertTrue(config.isCacheEnabled());
    }
}
```

## Logging Test

```java
@Slf4j
class LoggingTest {

    @Test
    void testLogging() {
        log.trace("TRACE level log");
        log.debug("DEBUG level log");
        log.info("INFO level log");
        log.warn("WARN level log");
        log.error("ERROR level log");
    }

    @Test
    void testExceptionLogging() {
        try {
            throw new RuntimeException("Test Exception");
        } catch (Exception e) {
            log.error("Exception occurred: ", e);
        }
    }
}
```

# File Organization

## Mapper XML Grouping

도메인별로 그룹화:

```
mapper/
├── member/
│   ├── MemberMapper.xml
│   └── MemberRoleMapper.xml
├── board/
│   ├── BoardMapper.xml
│   └── CommentMapper.xml
└── admin/
    └── AdminMapper.xml
```

root-context.xml에서 재귀 스캔:
```xml
<property name="mapperLocations" value="classpath:/mapper/**/*.xml" />
```

# Related Contexts

- Mapper Layer: ../java/org/zerock/mapper/AGENTS.md
- Spring Configuration: ../webapp/WEB-INF/spring/AGENTS.md
- DTO: ../java/org/zerock/dto/AGENTS.md
