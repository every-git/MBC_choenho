# Project Context & Operations

## Business Goal
Spring Boot 3.x + MyBatis 기반 웹 애플리케이션 개발.
MySQL 데이터베이스 연동, 회원 관리 기능 제공.

## Tech Stack
- Java 21
- Spring Boot 3.5.9
- Spring Web MVC
- Spring Data JDBC
- MyBatis 3.0.5
- MySQL Connector
- Thymeleaf (View Template)
- Lombok
- Spring Boot DevTools
- Maven

## Operational Commands

### Build & Run
```bash
# 빌드
./mvnw clean package

# 실행
./mvnw spring-boot:run

# 또는
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Test
```bash
# 전체 테스트
./mvnw test

# 특정 테스트
./mvnw test -Dtest=MemberMapperTest
```

### Database
```bash
# MySQL 접속
mysql -u springdbuser -p springdb

# 스키마 확인
SHOW TABLES;
DESC member;
```

## Environment
- Server Port: 8080
- Database: MySQL (localhost:3306/springdb)
- DB User: springdbuser
- Character Encoding: UTF-8
- Timezone: Asia/Seoul

# Golden Rules

## Immutable

### Security
- 절대 데이터베이스 비밀번호를 Git에 커밋하지 마라.
- application.properties의 민감 정보는 환경 변수 또는 별도 설정 파일로 분리하라.
- SQL Injection 방지: 항상 PreparedStatement 또는 MyBatis의 #{} 파라미터 바인딩을 사용하라.

### Architecture
- 3-Tier 아키텍처를 준수하라: Controller -> Service -> Mapper.
- Controller는 Service만 호출하고, Mapper를 직접 호출하지 마라.
- 비즈니스 로직은 Service 계층에만 작성하라.
- Mapper 인터페이스는 데이터 액세스만 담당하라.

### Data Integrity
- 트랜잭션이 필요한 작업은 반드시 @Transactional을 선언하라.
- DTO는 불변 객체로 설계하라 (final 필드 사용).

## Do's & Don'ts

### DO
- Lombok의 @RequiredArgsConstructor를 사용하여 의존성 주입하라.
- 로깅은 Log4j2 (@Log4j2)를 사용하라.
- MyBatis XML 매퍼에서 snake_case 컬럼명을 사용하라 (자동으로 camelCase 매핑).
- REST API는 @RestController를 사용하고, 뷰 반환은 @Controller를 사용하라.
- 테스트 코드는 @MybatisTest 또는 @SpringBootTest를 적절히 선택하라.

### DON'T
- Controller에서 비즈니스 로직을 작성하지 마라.
- 하드코딩된 SQL을 Service나 Controller에 작성하지 마라.
- System.out.println()을 사용하지 마라 (log 사용).
- null 체크 없이 객체 메서드를 호출하지 마라.
- @Autowired 필드 주입을 사용하지 마라 (생성자 주입 권장).

# Standards & References

## Package Structure
```
com.example.demo
├── controller/      # HTTP 요청 처리
├── service/         # 비즈니스 로직
├── mapper/          # MyBatis 인터페이스
└── domain/          # DTO/VO/Entity
```

## Naming Conventions

### Java Class
- Controller: `{Entity}Controller` (예: MemberController)
- Service: `{Entity}Service` (예: MemberService)
- Mapper: `{Entity}Mapper` (예: MemberMapper)
- DTO: `{Entity}DTO` (예: MemberDTO)

### Database
- 테이블명: 소문자 snake_case (예: member, order_item)
- 컬럼명: 소문자 snake_case (예: user_name, created_at)

### URL Mapping
- RESTful 스타일 권장: `/member/list`, `/member/{id}`
- 소문자 사용, 단어 구분은 하이픈 (예: `/order-item`)

## Coding Conventions
- 들여쓰기: 스페이스 2칸 (또는 프로젝트 기존 설정 따름)
- 줄 길이: 120자 이하 권장
- 주석: JavaDoc 스타일로 public 메서드 문서화
- 예외 처리: 비즈니스 예외는 커스텀 예외 클래스 사용

## Git Strategy
- Branch 전략: main (기본 브랜치)
- Commit Message 포맷:
  ```
  [타입] 제목 (50자 이내)

  상세 설명 (선택 사항)
  ```
  - 타입: feat, fix, refactor, docs, test, chore
  - 예: `[feat] 회원 목록 조회 기능 추가`

## Maintenance Policy
- 이 규칙과 실제 코드가 불일치하면 즉시 업데이트를 제안하라.
- 새로운 기술 스택이나 패턴 도입 시 Context Map을 업데이트하라.
- 하위 AGENTS.md 파일이 500라인을 초과하면 모듈을 분리하라.

# Context Map (Action-Based Routing)

## Core Layers
- **[Controller Layer](./src/main/java/com/example/demo/controller/AGENTS.md)** — HTTP 요청 처리, 뷰 라우팅, REST API 엔드포인트 작성 시.
- **[Service Layer](./src/main/java/com/example/demo/service/AGENTS.md)** — 비즈니스 로직, 트랜잭션 처리, 데이터 검증 시.
- **[Mapper Layer](./src/main/java/com/example/demo/mapper/AGENTS.md)** — MyBatis 인터페이스 정의, 데이터 액세스 로직 작성 시.
- **[Domain Layer](./src/main/java/com/example/demo/domain/AGENTS.md)** — DTO, VO, Entity 클래스 작성 및 수정 시.

## Data Access
- **[MyBatis XML Mapper](./src/main/resources/mapper/AGENTS.md)** — SQL 쿼리 작성, 동적 SQL, ResultMap 정의 시.

## Testing
- **[Unit Tests](./src/test/java/com/example/demo/AGENTS.md)** — 단위 테스트, 통합 테스트, Mock 작성 시.

## Configuration
- **[Application Properties](./src/main/resources/AGENTS.md)** — Spring Boot 설정, 데이터베이스 연결, MyBatis 옵션 수정 시.
