# Project Context & Operations

## Business Goal

대철이제철 게시판 시스템 (daechul-SF) - Spring Framework 기반 게시판 프로젝트.
Eclipse IDE 워크스페이스 환경에서 Tomcat v10.1 서버 사용.

## Tech Stack

- Java 21
- Spring Framework 6.2.1
- Spring Security 6.4.2
- MyBatis 3.5.17 + mybatis-spring 3.0.4
- MySQL 9.1.0 + HikariCP 6.2.1
- Log4j2 2.24.3
- Jackson 2.18.2
- JSP + JSTL 3.0
- Axios (REST API 통신)
- Bootstrap 5.3.0 (UI 컴포넌트)
- Maven 3.x
- Apache Tomcat 10.1

## Operational Commands

### Build & Package

```bash
cd dc-SF
mvn clean compile
mvn clean package
```

### Testing

```bash
cd dc-SF
mvn test
```

### Server Management

- Eclipse IDE: Servers 탭에서 "Tomcat v10.1 Server at localhost" 시작/중지
- Manual: Tomcat 설정은 ./Servers/Tomcat v10.1 Server at localhost-config/ 참조

### Clean Build

```bash
cd dc-SF
mvn clean install
```

# Golden Rules

## Immutable Constraints

### Security

- DB 접속 정보는 절대 하드코딩 금지. WEB-INF/spring/root-context.xml 또는 환경변수 사용.
- Spring Security 설정 변경 시 반드시 SecurityConfig.java와 web.xml 동기화 확인.
- 비밀번호는 BCryptPasswordEncoder로만 암호화. 다른 알고리즘 사용 금지.
- CSRF 토큰은 Spring Security 기본 설정 유지. 임의 비활성화 금지.

### Architecture

- Java 21 이하 버전으로 downgrade 금지.
- Jakarta EE 9+ (jakarta.*패키지) 사용. javax.* 패키지 사용 금지.
- MyBatis Mapper XML 파일은 반드시 src/main/resources/mapper/ 하위에 위치.
- Service 계층 없이 Controller에서 Mapper 직접 호출 금지.

### Database

- MySQL 연결 시 serverTimezone=Asia/Seoul 필수 설정.
- Connection Pool은 HikariCP만 사용. 다른 Pool 라이브러리 추가 금지.
- Mapper XML에서 #{}는 PreparedStatement 사용. ${}는 SQL Injection 위험으로 금지.

## Do's & Don'ts

### DO

- DTO 클래스에 Lombok @Data, @Getter, @Setter, @Builder 적극 활용.
- Controller는 @Controller + @RequestMapping 조합 사용.
- Service는 Interface + Impl 구조로 구현.
- Mapper는 Interface + XML 매핑 구조 유지.
- 트랜잭션은 Service 계층에서 @Transactional 어노테이션 사용.
- 로깅은 Log4j2의 slf4j 바인딩 사용. System.out.println() 금지.
- JSP에서 Spring Security 태그라이브러리 사용 (sec:authorize 등).

### DON'T

- Controller에 비즈니스 로직 작성 금지.
- Mapper XML에서 SELECT 결과를 Map으로 받지 말고 DTO 사용.
- web.xml과 Java Config 혼용 금지. web.xml 기반 설정 유지.
- pom.xml에서 Spring 버전 개별 지정 금지. ${spring.version} 변수 사용.
- src/main/webapp 하위에 Java 클래스 배치 금지.
- MyBatis resultType을 생략하지 말고 명시적으로 지정.
- 복수 파라미터 사용 시 @Param 어노테이션 생략 금지.
- UI에 이모티콘 사용 금지. 텍스트와 색상으로 정보 표시.

# Standards & References

## Coding Conventions

- Package Naming: org.zerock.[layer]
  - controller: MVC Controller
  - service: Business Logic Service
  - mapper: MyBatis Mapper Interface
  - dto: Data Transfer Object
  - security: Spring Security Configuration
- Class Naming:
  - Controller: *Controller.java
  - Service Interface: *Service.java
  - Service Impl: *ServiceImpl.java
  - Mapper: *Mapper.java
  - DTO: *DTO.java
- File Naming:
  - Mapper XML: *Mapper.xml (동일 이름 Interface와 매핑)
  - JSP: kebab-case.jsp (예: member-list.jsp)
  - Spring Config: root-context.xml, servlet-context.xml, security-context.xml

## Project Structure Standards

```
dc-SF/
├── src/main/java/org/zerock/
│   ├── controller/      # MVC Controllers
│   ├── service/         # Business Logic
│   ├── mapper/          # MyBatis Mappers
│   ├── dto/             # DTOs
│   └── security/        # Spring Security Config
├── src/main/resources/
│   ├── mapper/          # MyBatis XML Mappers
│   ├── mybatis-config.xml
│   └── log4j2.xml
└── src/main/webapp/
    ├── WEB-INF/
    │   ├── spring/      # Spring Configuration
    │   ├── views/       # JSP Views
    │   └── web.xml
    └── resources/       # Static Resources
```

## Git Strategy

### Branching

- main: Production-ready code
- 기능 개발 시 feature/* 브랜치 생성 권장

### Commit Message Format

```
[TYPE] Brief description

Detailed description (optional)

- TYPE: feat, fix, refactor, docs, style, test, chore
- Examples:
  - [feat] Add member login feature
  - [fix] Fix SQL injection in BoardMapper
  - [refactor] Separate SecurityConfig from web.xml
```

### Pre-commit Checks

- mvn clean compile 성공 확인
- Java 파일 주석 처리된 코드 제거
- System.out.println() 제거 후 Log4j2 사용

## Maintenance Policy

### Rule-Code Sync

- 이 AGENTS.md는 프로젝트 변경 시 함께 업데이트되어야 함.
- 새로운 Layer나 Framework가 추가되면 해당 디렉토리에 AGENTS.md 생성 제안.
- 규칙과 실제 코드가 불일치하면 AI는 업데이트를 제안해야 함.

### Version Updates

- Spring/Security 버전 업그레이드 시 pom.xml의 properties 섹션만 수정.
- Jakarta EE 스펙 변경 시 web.xml의 schema 버전도 함께 업데이트.

# Context Map (Action-Based Routing)

## Core Application

- **[Spring Framework Project (dc-SF)](./dc-SF/AGENTS.md)** — 전체 애플리케이션 아키텍처, 빌드, 설정 파일 수정 시.

## Backend Layers

- **[MVC Controllers](./dc-SF/src/main/java/org/zerock/controller/AGENTS.md)** — HTTP 요청 처리, @RequestMapping, 뷰 반환 로직 수정 시.
- **[Business Services](./dc-SF/src/main/java/org/zerock/service/AGENTS.md)** — 비즈니스 로직, 트랜잭션 처리 수정 시.
- **[MyBatis Mappers](./dc-SF/src/main/java/org/zerock/mapper/AGENTS.md)** — DB 쿼리, Mapper Interface/XML 수정 시.
- **[DTOs](./dc-SF/src/main/java/org/zerock/dto/AGENTS.md)** — 데이터 전송 객체 구조 변경 시.
- **[Spring Security](./dc-SF/src/main/java/org/zerock/security/AGENTS.md)** — 인증/인가, 보안 설정 수정 시.

## Frontend

- **[JSP Views](./dc-SF/src/main/webapp/WEB-INF/views/AGENTS.md)** — JSP/JSTL, UI 템플릿, 정적 리소스 수정 시.

## Configuration

- **[Spring Configuration](./dc-SF/src/main/webapp/WEB-INF/spring/AGENTS.md)** — root-context.xml, servlet-context.xml, security-context.xml 수정 시.
- **[MyBatis Configuration](./dc-SF/src/main/resources/AGENTS.md)** — mybatis-config.xml, Mapper XML 파일 수정 시.

## Development Environment

- **[Eclipse Workspace](./WORKSPACE.md)** — Eclipse 설정, Tomcat 서버 설정 수정 시.
