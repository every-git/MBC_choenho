# Module Context

Spring Framework 6.2.1 기반 대철이제철 게시판 시스템 (daechul-SF).
MVC 아키텍처 + MyBatis + Spring Security 조합으로 구성된 웹 애플리케이션.

## Role & Responsibilities

- HTTP 요청 처리 및 응답 생성 (Controller Layer)
- 비즈니스 로직 실행 (Service Layer)
- 데이터베이스 접근 (MyBatis Mapper Layer)
- 인증/인가 처리 (Spring Security)
- JSP 기반 뷰 렌더링

## Dependencies

### External

- MySQL Database (jdbc:mysql://localhost:3306/daechul)
- Apache Tomcat 10.1

### Internal

- Controller -> Service -> Mapper 단방향 의존성
- Security -> Service (UserDetailsService 구현)
- View -> Controller (HTTP 요청/응답)

## Implemented Features (Current Status)

### Authentication & Authorization

- **Member Signup**:
  - Validates ID duplication.
  - Supports Admin signup with Secret Code (`9876`).
  - Roles: `MEMBER` (Default), `ADMIN` (Requires Secret).
- **Security Check**:
  - `MemberDTO` generates `ROLE_MEMBER` or `ROLE_ADMIN`.
  - `header.jsp` and `home.jsp` conditionally render Admin links based on `hasRole('ADMIN')`.
- **View Design**:
  - All views follow original daechul-project design patterns.
  - Board pages (list, view, write, update) do not include header/footer for consistency with original.
  - Admin pages do not include header/footer for consistency with original.
  - Member pages (login, join) include header/footer.
- **HTTP Methods**:
  - Board delete: GET request (matching original project pattern).
  - Admin member/board delete: GET request (matching original project pattern).
- **Navigation**:
  - Admin pages include "게시판" link for easy navigation.
  - Board list includes "메인으로" and "로그아웃" links.

### Board Features

- **Board CRUD**:
  - Board list with pagination (10 items per page).
  - Board view with hit count increment.
  - Board write (login required).
  - Board update (author only).
  - Board delete (author only, logical delete).
- **Pagination**:
  - BoardListPaginDTO for pagination information.
  - Page block navigation (10 pages per block).
  - URL parameters: `?page=1&size=10`.

### Reply Features

- **Reply CRUD**:
  - Reply registration (REST API: POST /replies).
  - Reply list with pagination (REST API: GET /replies/{bno}/list).
  - Reply read (REST API: GET /replies/{rno}).
  - Reply update (REST API: PUT /replies/{rno}).
  - Reply delete (REST API: DELETE /replies/{rno}, logical delete).
- **Pagination**:
  - ReplyListPaginDTO for pagination information.
  - Page block navigation (10 pages per block).
  - Default: 10 replies per page.
- **REST API**:
  - ReplyController uses @RestController.
  - JSON request/response format.
  - Exception handling with ReplyException.
- **Database**:
  - Reply table: `reply` (rno, bno, replyText, replyer, replydate, updatedate, deflag).
  - Foreign key: bno references board(seq).

# Tech Stack & Constraints

## Framework Versions (LOCKED)

- Spring Framework: 6.2.1 (절대 변경 금지)
- Spring Security: 6.4.2 (Security와 함께 업그레이드만 허용)
- MyBatis: 3.5.17
- mybatis-spring: 3.0.4
- Java: 21 (downgrade 절대 금지)

## Required Libraries

### Persistence

- HikariCP 6.2.1 (다른 Connection Pool 사용 금지)
- MySQL Connector 9.1.0
- MyBatis + mybatis-spring

### Logging

- Log4j2 2.24.3 (slf4j-impl 포함)
- System.out.println() 사용 금지

### JSON Processing

- Jackson 2.18.2 (databind + datatype-jsr310)

### View

- JSP + JSTL 3.0
- Spring Security Taglib

## Configuration Constraints

### Spring Configuration

- root-context.xml: DB, MyBatis, Service 빈 등록
- servlet-context.xml: Controller, View Resolver, Static Resources
- security-context.xml: Spring Security 설정 (현재 미사용, SecurityConfig.java 사용)
- web.xml: DispatcherServlet, Security Filter 등록

### MyBatis Configuration

- mybatis-config.xml: TypeAlias, Settings
- Mapper XML: src/main/resources/mapper/ 하위 배치 필수

# Implementation Patterns

## Layer-by-Layer Patterns

### Controller Pattern

```java
@Controller
@RequestMapping("/path")
public class XxxController {

    private final XxxService xxxService;

    @Autowired
    public XxxController(XxxService xxxService) {
        this.xxxService = xxxService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<XxxDTO> list = xxxService.getList();
        model.addAttribute("list", list);
        return "path/list";  // JSP path
    }

    @PostMapping("/register")
    public String register(@ModelAttribute XxxDTO dto, RedirectAttributes rttr) {
        xxxService.register(dto);
        rttr.addFlashAttribute("message", "Success");
        return "redirect:/path/list";
    }
}
```

### Service Pattern

```java
// Interface
public interface XxxService {
    List<XxxDTO> getList();
    void register(XxxDTO dto);
}

// Implementation
@Service
public class XxxServiceImpl implements XxxService {

    private final XxxMapper xxxMapper;

    @Autowired
    public XxxServiceImpl(XxxMapper xxxMapper) {
        this.xxxMapper = xxxMapper;
    }

    @Override
    @Transactional
    public void register(XxxDTO dto) {
        xxxMapper.insert(dto);
    }
}
```

### Mapper Pattern

```java
// Interface
public interface XxxMapper {
    List<XxxDTO> selectAll();
    void insert(XxxDTO dto);
}
```

```xml
<!-- Mapper XML -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.zerock.mapper.XxxMapper">
    <select id="selectAll" resultType="org.zerock.dto.XxxDTO">
        SELECT * FROM xxx_table
    </select>

    <insert id="insert" parameterType="org.zerock.dto.XxxDTO">
        INSERT INTO xxx_table (col1, col2)
        VALUES (#{col1}, #{col2})
    </insert>
</mapper>
```

### DTO Pattern

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XxxDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
```

## File Organization

### New Feature Checklist

기능 추가 시 아래 순서대로 파일 생성:

1. DTO 작성 (src/main/java/org/zerock/dto/XxxDTO.java)
2. Mapper Interface 작성 (src/main/java/org/zerock/mapper/XxxMapper.java)
   - 복수 파라미터 사용 시 @Param 어노테이션 필수
   - import org.apache.ibatis.annotations.Param; 추가
3. Mapper XML 작성 (src/main/resources/mapper/XxxMapper.xml)
   - 페이징 쿼리: LIMIT #{size} OFFSET #{skip} 형식 사용
4. Service Interface 작성 (src/main/java/org/zerock/service/XxxService.java)
5. Service Impl 작성 (src/main/java/org/zerock/service/XxxServiceImpl.java)
   - 유효성 검사 및 예외 처리 포함
6. Controller 작성 (src/main/java/org/zerock/controller/XxxController.java)
   - REST API는 @RestController 사용
   - 일반 페이지는 @Controller 사용
7. JSP View 작성 (src/main/webapp/WEB-INF/views/xxx/*.jsp)
   - 이모티콘 사용 금지
   - 깔끔하고 직관적인 UI 구현

### File Naming Rules

- Controller: *Controller.java (예: MemberController.java)
- Service: *Service.java,*ServiceImpl.java
- Mapper: *Mapper.java,*Mapper.xml (이름 일치 필수)
- DTO: *DTO.java (예: MemberDTO.java)
- JSP: kebab-case.jsp (예: member-list.jsp)

# Testing Strategy

## Unit Testing

### Maven Command

```bash
cd dc-SF
mvn test
```

### Test Class Pattern

```java
@SpringJUnitConfig(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
class XxxServiceTest {

    @Autowired
    private XxxService xxxService;

    @Test
    void testGetList() {
        List<XxxDTO> list = xxxService.getList();
        assertNotNull(list);
    }
}
```

### Mapper Testing

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
    }
}
```

## Integration Testing

### Server Testing

1. Eclipse에서 Tomcat 서버 시작
2. <http://localhost:8080/> 접속
3. 주요 페이지 수동 확인

### Security Testing

- 인증 필요 페이지 접근 시 로그인 페이지로 리다이렉트 확인
- 로그인 후 권한별 접근 제어 확인
- CSRF 토큰 자동 포함 확인

# Local Golden Rules

## Common Mistakes to Avoid

### Layer Violation

- Controller에서 Mapper 직접 호출 금지. 반드시 Service 경유.
- Service에서 HttpServletRequest/Response 사용 금지.
- Mapper에서 비즈니스 로직 작성 금지. 순수 SQL만.

### HTTP Method Conventions

- 삭제 작업은 GET 요청 사용 (원본 프로젝트와 일관성 유지).
- BoardController.delete: @GetMapping 사용.
- AdminController.deleteMember, deleteBoard: @GetMapping 사용.
- POST는 폼 제출(작성, 수정)에만 사용.

### MyBatis Anti-Patterns

- Mapper XML에서 ${} 파라미터 바인딩 금지. SQL Injection 위험.
- SELECT 결과를 HashMap으로 받지 말 것. DTO 명시적 사용.
- Mapper Interface 메서드명과 XML id 불일치 주의.
- resultType에 풀 패키지명 명시 (예: org.zerock.dto.MemberDTO).
- 복수 파라미터 사용 시 @Param 어노테이션 필수: `@Param("skip") int skip, @Param("size") int size`.
- @Param import 필수: `import org.apache.ibatis.annotations.Param;`.

### Spring Security

- 비밀번호 평문 저장 절대 금지. BCryptPasswordEncoder 사용.
- 로그인 성공 후 CustomLoginSuccessHandler에서 권한별 리다이렉트.
- 403 에러 시 Custom403Handler에서 처리.

### View Design Consistency

- 원본 daechul-project와 디자인 일관성 유지 필수.
- 게시판 관련 페이지 (board/*.jsp): header/footer include 없음.
- 관리자 페이지 (admin/*.jsp): header/footer include 없음.
- 회원 페이지 (member/login.jsp, member/join.jsp): header/footer include 있음.
- 모든 관리자 페이지에 "게시판" 링크 포함.
- CSS 변수: --red 추가 (--danger와 동일 값, 원본 호환성).
- 이모티콘 사용 금지: 모든 UI에서 이모티콘 제거, 텍스트와 색상으로 정보 표시.
- 깔끔한 디자인: 네이버 스타일 테마 적용, 직관적인 UI/UX.

### Transaction Management

- Service 계층에만 @Transactional 사용.
- Controller나 Mapper에 @Transactional 금지.
- 읽기 전용 메서드에는 @Transactional(readOnly = true) 사용.

### Logging

- System.out.println() 절대 금지.
- Log4j2 사용: log.info(), log.error() 등.
- 민감 정보 (비밀번호, 개인정보) 로그 출력 금지.

### Exception Handling

- Controller에서 @ExceptionHandler 또는 @ControllerAdvice 사용.
- Exception 메시지를 그대로 사용자에게 노출 금지.
- DB 예외는 Service에서 비즈니스 예외로 변환.

## Performance Best Practices

### Database

- N+1 쿼리 방지: JOIN 또는 MyBatis Association 사용.
- Paging 처리 시 LIMIT/OFFSET 사용.
- SELECT * 지양. 필요한 컬럼만 명시.
- 페이징 쿼리: `LIMIT #{size} OFFSET #{skip}` 형식 사용.
- 페이징 계산: `skip = (page - 1) * size` 공식 사용.

### MyBatis

- resultMap 재사용으로 중복 정의 방지.
- Dynamic SQL (<if>, <choose>) 적극 활용.
- Batch Insert/Update 시 foreach 사용.

### Spring

- Service 빈 싱글톤 유지. 상태 저장 금지.
- Controller에서 static 리소스 직접 서비스 금지. servlet-context.xml 설정 사용.

# Operational Commands

## Build & Run

### Clean Build

```bash
cd dc-SF
mvn clean install
```

### Compile Only

```bash
mvn clean compile
```

### Package WAR

```bash
mvn clean package
# 결과: target/daechul-SF-0.0.1-SNAPSHOT.war
```

## Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test

```bash
mvn test -Dtest=MemberMapperTest
```

## Server Management

### Eclipse IDE

1. Servers 탭 열기
2. "Tomcat v10.1 Server at localhost" 우클릭
3. Start/Stop/Restart

### Manual Deployment

1. mvn clean package 실행
2. target/*.war 파일을 Tomcat webapps/ 디렉토리에 복사
3. Tomcat 재시작

## Logging

### Log File Location

- 설정: src/main/resources/log4j2.xml
- Console Output: Eclipse Console 탭

### Log Level Change

log4j2.xml에서 level 수정:

```xml
<Logger name="org.zerock" level="DEBUG" />
```

# Related Contexts

- Controller Layer: ./src/main/java/org/zerock/controller/AGENTS.md
- Service Layer: ./src/main/java/org/zerock/service/AGENTS.md
- Mapper Layer: ./src/main/java/org/zerock/mapper/AGENTS.md
- DTO: ./src/main/java/org/zerock/dto/AGENTS.md
- Security: ./src/main/java/org/zerock/security/AGENTS.md
- Views: ./src/main/webapp/WEB-INF/views/AGENTS.md
- Spring Config: ./src/main/webapp/WEB-INF/spring/AGENTS.md
- MyBatis Config: ./src/main/resources/AGENTS.md
