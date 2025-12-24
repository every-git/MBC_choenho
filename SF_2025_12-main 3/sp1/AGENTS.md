# SP1 Project Context & Governance

## Project Context & Operations

**Business Goal:** Spring Boot 기반 게시판 애플리케이션. 게시글 CRUD 및 댓글 기능 제공.

**Tech Stack:**
- Java 21
- Spring Framework 6.2.14 (Spring MVC, Spring JDBC, Spring TX)
- MyBatis 3.5.19
- MySQL 9.4.0
- JSP (Jakarta Servlet JSP 3.1.1)
- Lombok 1.18.42
- Maven 3.x
- Tomcat 10.1 (배포 환경)

**Operational Commands:**
- 빌드: `mvn clean package` (target/sp1.war 생성)
- 테스트: `mvn test` (JUnit 5)
- 서버 실행: Eclipse/IntelliJ에서 Tomcat 서버에 배포 후 실행
- 데이터베이스 연결: `mysql -u springdbuser -p1234 springdb`

## Golden Rules

**Immutable:**
- 절대 `@Autowired` 필드 주입 대신 생성자 주입 사용 (`@RequiredArgsConstructor` 활용)
- MyBatis 매퍼는 인터페이스만 정의하고 XML에서 구현
- REST API는 `@RestController` 사용, 일반 페이지는 `@Controller` 사용
- 삭제는 물리 삭제가 아닌 논리 삭제 (`delflag = true`)로 처리

**Do's:**
- DTO는 `@Builder` 패턴 사용 권장
- Service 계층에서 예외는 커스텀 Exception으로 변환하여 던지기
- REST API 응답은 `ResponseEntity`로 래핑
- JSP에서 EL 표현식 사용 시 `isELIgnored="false"` 명시
- 페이징은 `BoardListPaginDTO`, `ReplyListPaginDTO` 사용

**Don'ts:**
- Controller에서 직접 Mapper 호출 금지 (Service 계층 경유 필수)
- SQL을 Java 코드에 하드코딩하지 말 것 (MyBatis XML 사용)
- 비즈니스 로직을 Controller에 작성하지 말 것
- JSP에서 스크립트릿(`<% %>`) 사용 지양, JSTL과 EL 사용
- API 키나 비밀번호를 코드에 하드코딩하지 말 것

## Standards & References

**Coding Convention:**
- 패키지 구조: `org.zerock.{controller|service|mapper|dto}`
- 클래스 네이밍: `{Entity}Controller`, `{Entity}Service`, `{Entity}Mapper`, `{Entity}DTO`
- 메서드 네이밍: CRUD는 `add`, `getOne`, `modify`, `remove` 사용
- 로깅: `@Log4j2` 사용, `log.info()`, `log.error()` 활용

**Git Strategy:**
- 커밋 메시지: `[타입] 간단한 설명` (예: `[FEAT] 댓글 삭제 기능 추가`)
- 타입: FEAT, FIX, REFACTOR, DOCS, TEST

**Maintenance Policy:**
- 규칙과 실제 코드 간 괴리 발견 시 즉시 AGENTS.md 업데이트 제안
- 새로운 패턴 도입 시 해당 모듈의 AGENTS.md에 문서화

## Context Map (Action-Based Routing)

- **[Controller 계층 수정](./src/main/java/org/zerock/controller/AGENTS.md)** — REST API 엔드포인트 추가/수정, 요청 매핑 변경 시
- **[Service 계층 수정](./src/main/java/org/zerock/service/AGENTS.md)** — 비즈니스 로직 구현, 트랜잭션 처리, 예외 처리 시
- **[Mapper 계층 수정](./src/main/resources/mapper/AGENTS.md)** — MyBatis 쿼리 작성, SQL 최적화, 매퍼 인터페이스 정의 시
- **[View 계층 수정 (JSP)](./src/main/webapp/WEB-INF/views/AGENTS.md)** — JSP 페이지 작성/수정, 프론트엔드 로직 추가 시
- **[테스트 작성](./src/test/java/AGENTS.md)** — 단위 테스트, 통합 테스트 작성 시
