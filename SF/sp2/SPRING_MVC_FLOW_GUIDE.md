# Spring MVC 애플리케이션 전체 흐름 설명서

## 📋 목차
1. [프로젝트 구조](#프로젝트-구조)
2. [Spring MVC 아키텍처](#spring-mvc-아키텍처)
3. [요청 처리 흐름](#요청-처리-흐름)
4. [설정 파일 분석](#설정-파일-분석)
5. [컨트롤러 엔드포인트 상세](#컨트롤러-엔드포인트-상세)
6. [데이터 바인딩](#데이터-바인딩)

---

## 프로젝트 구조

```
sp1/
├── src/main/
│   ├── java/
│   │   └── org/zerock/
│   │       ├── controller/
│   │       │   └── HelloController.java    # 컨트롤러 (요청 처리)
│   │       ├── service/
│   │       │   └── HelloService.java      # 서비스 레이어
│   │       └── dto/
│   │           └── SampleDTO.java         # 데이터 전송 객체
│   └── webapp/
│       └── WEB-INF/
│           ├── web.xml                    # 웹 애플리케이션 설정
│           ├── spring/
│           │   ├── root-context.xml       # 루트 컨텍스트 (서비스, DB)
│           │   └── servlet-context.xml    # 서블릿 컨텍스트 (MVC)
│           └── views/
│               └── sample/
│                   ├── ex1.jsp
│                   ├── ex3Result.jsp
│                   └── success.jsp
```

---

## Spring MVC 아키텍처

### 1. 이중 컨텍스트 구조

Spring MVC는 **두 개의 ApplicationContext**를 사용합니다:

#### Root ApplicationContext (root-context.xml)
- **용도**: 비즈니스 로직, 서비스, 데이터베이스 관련 Bean
- **스캔 패키지**: `org.zerock.service`
- **주요 Bean**:
  - `HelloService` (서비스 레이어)
  - `hikariConfig` (HikariCP 설정)
  - `dataSource` (데이터베이스 연결)
- **생성 시점**: 웹 애플리케이션 시작 시 (ContextLoaderListener)

#### Servlet ApplicationContext (servlet-context.xml)
- **용도**: 웹 관련 Bean (컨트롤러, 뷰 리졸버)
- **스캔 패키지**: `org.zerock.controller`
- **주요 Bean**:
  - `HelloController` (컨트롤러)
  - `InternalResourceViewResolver` (JSP 뷰 리졸버)
- **생성 시점**: DispatcherServlet 초기화 시

### 2. 컨텍스트 계층 구조

```
Root ApplicationContext (부모)
    └── Servlet ApplicationContext (자식)
        └── Servlet ApplicationContext는 Root를 참조 가능
```

---

## 요청 처리 흐름

### 전체 흐름도

```
[브라우저]
    ↓ HTTP 요청: http://localhost:8080/ex1
[Tomcat 서버]
    ↓
[DispatcherServlet] (web.xml에서 매핑: url-pattern="/")
    ↓
[HandlerMapping] → HelloController의 @GetMapping("/ex1") 찾기
    ↓
[HandlerAdapter] → ex1() 메서드 실행
    ↓
[Controller] → HelloController.ex1()
    ↓
[ViewResolver] → "sample/ex1" → "/WEB-INF/views/sample/ex1.jsp"
    ↓
[JSP 렌더링]
    ↓
[HTTP 응답]
    ↓
[브라우저]
```

### 단계별 상세 설명

#### 1단계: 요청 수신 (DispatcherServlet)
```xml
<!-- web.xml -->
<servlet-mapping>
    <servlet-name>appServlet</servlet-name>
    <url-pattern>/</url-pattern>  <!-- 모든 요청을 DispatcherServlet이 처리 -->
</servlet-mapping>
```
- 모든 HTTP 요청(`/`로 시작하는 모든 경로)이 DispatcherServlet으로 전달됩니다.

#### 2단계: 핸들러 매핑 (HandlerMapping)
```java
@Controller
public class HelloController {
    @GetMapping("/ex1")  // URL 패턴 매핑
    public String ex1() { ... }
}
```
- `@GetMapping("/ex1")` 어노테이션을 통해 요청 URL과 컨트롤러 메서드를 매핑합니다.
- `servlet-context.xml`의 `<mvc:annotation-driven/>`이 이 기능을 활성화합니다.

#### 3단계: 핸들러 어댑터 (HandlerAdapter)
- 컨트롤러 메서드를 실행하기 위한 어댑터를 선택하고 실행합니다.
- `@Controller` + `@GetMapping` 조합은 `RequestMappingHandlerAdapter`가 처리합니다.

#### 4단계: 컨트롤러 실행
```java
@GetMapping("/ex1")
public String ex1() {
    log.info("========== /ex1 컨트롤러 호출됨 ==========");
    return "sample/ex1";  // 뷰 이름 반환
}
```
- 비즈니스 로직을 처리하고 뷰 이름을 반환합니다.

#### 5단계: 뷰 리졸버 (ViewResolver)
```xml
<!-- servlet-context.xml -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/" />
    <property name="suffix" value=".jsp" />
</bean>
```
- 반환된 뷰 이름 `"sample/ex1"`을 실제 JSP 경로로 변환:
  - Prefix: `/WEB-INF/views/`
  - 뷰 이름: `sample/ex1`
  - Suffix: `.jsp`
  - **최종 경로**: `/WEB-INF/views/sample/ex1.jsp`

#### 6단계: 뷰 렌더링
- JSP 파일을 컴파일하고 HTML을 생성합니다.
- 생성된 HTML이 HTTP 응답으로 전송됩니다.

---

## 설정 파일 분석

### 1. web.xml

```xml
<!-- 루트 컨텍스트 설정 -->
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/spring/root-context.xml</param-value>
</context-param>

<!-- DispatcherServlet 설정 -->
<servlet>
    <servlet-name>appServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring/servlet-context.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>appServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

**역할**:
- `ContextLoaderListener`: Root ApplicationContext 생성
- `DispatcherServlet`: Servlet ApplicationContext 생성 및 요청 처리
- `url-pattern="/"`: 모든 요청을 DispatcherServlet이 처리

### 2. servlet-context.xml

```xml
<!-- 어노테이션 기반 MVC 활성화 -->
<mvc:annotation-driven/>

<!-- 뷰 리졸버 설정 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/" />
    <property name="suffix" value=".jsp" />
</bean>

<!-- 컨트롤러 스캔 -->
<context:component-scan base-package="org.zerock.controller" />
```

**역할**:
- `@Controller`, `@GetMapping` 등 어노테이션 활성화
- JSP 뷰 리졸버 설정
- 컨트롤러 자동 스캔 및 Bean 등록

### 3. root-context.xml

```xml
<!-- 서비스 레이어 스캔 -->
<context:component-scan base-package="org.zerock.service" />

<!-- 데이터베이스 연결 설정 -->
<bean name="hikariConfig" class="com.zaxxer.hikari.HikariConfig">
    <property name="driverClassName" value="com.mysql.cj.jdbc.Driver" />
    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/springdb?serverTimezone=Asia/Seoul" />
    ...
</bean>
<bean name="dataSource" class="com.zaxxer.hikari.HikariDataSource">
    <constructor-arg ref="hikariConfig" />
</bean>
```

**역할**:
- 서비스 레이어 Bean 등록
- 데이터베이스 연결 풀 설정

---

## 컨트롤러 엔드포인트 상세

### 1. `/ex1` - 기본 뷰 반환

```java
@GetMapping("/ex1")
public String ex1() {
    log.info("========== /ex1 컨트롤러 호출됨 ==========");
    return "sample/ex1";  // 명시적 뷰 이름 반환
}
```

**요청**: `GET http://localhost:8080/ex1`

**처리 과정**:
1. DispatcherServlet이 요청 수신
2. `@GetMapping("/ex1")` 매핑 발견
3. `ex1()` 메서드 실행
4. 뷰 이름 `"sample/ex1"` 반환
5. ViewResolver가 `/WEB-INF/views/sample/ex1.jsp`로 변환
6. JSP 렌더링 후 HTML 응답

**응답**: `ex1.jsp`의 HTML 내용

---

### 2. `/ex2` - 암시적 뷰 반환

```java
@GetMapping("/ex2")
public void ex2() {
    log.info("sample/ex2");
    // 반환값이 없음 (void)
}
```

**요청**: `GET http://localhost:8080/ex2`

**처리 과정**:
1. 메서드가 `void`를 반환
2. Spring이 요청 URL을 기반으로 뷰 이름 추론
3. URL: `/ex2` → 뷰 이름: `ex2`
4. ViewResolver가 `/WEB-INF/views/ex2.jsp`로 변환
5. **주의**: `ex2.jsp` 파일이 존재해야 함

**응답**: `ex2.jsp`의 HTML 내용 (파일이 존재하는 경우)

---

### 3. `/ex3re` - 리다이렉트 뷰

```java
@GetMapping("/ex3re")
public String ex3Re() {
    return "sample/ex3Result";
}
```

**요청**: `GET http://localhost:8080/ex3re`

**처리 과정**:
1. 뷰 이름 `"sample/ex3Result"` 반환
2. ViewResolver가 `/WEB-INF/views/sample/ex3Result.jsp`로 변환
3. JSP 렌더링

**응답**: `ex3Result.jsp`의 HTML 내용

---

### 4. `/ex4` - 요청 파라미터 바인딩

```java
@GetMapping("/ex4")
public void ex4(@RequestParam(name="n1", defaultValue = "1") int num,
                @RequestParam("name") String name) {
    log.info("num :" + num);
    log.info("name : " + name);
}
```

**요청 예시**: 
- `GET http://localhost:8080/ex4?n1=10&name=홍길동`
- `GET http://localhost:8080/ex4?name=홍길동` (n1 생략 시 기본값 1 사용)

**처리 과정**:
1. `@RequestParam` 어노테이션이 HTTP 요청 파라미터를 메서드 파라미터로 바인딩
2. `name="n1"`: URL 파라미터 이름 지정
3. `defaultValue = "1"`: 파라미터가 없을 때 기본값
4. 타입 변환: String → int (자동)
5. 로그 출력 후 void 반환 → 뷰 이름: `ex4`

**파라미터 바인딩 규칙**:
- `@RequestParam("name")`: 필수 파라미터 (없으면 400 Bad Request)
- `@RequestParam(name="n1", defaultValue = "1")`: 선택적 파라미터 (기본값 제공)

---

### 5. `/ex5` - 객체 바인딩 (DTO)

```java
@GetMapping("/ex5")
public void ex5(SampleDTO dto) {
    log.info(dto);
}
```

**SampleDTO 클래스**:
```java
@Setter
@ToString
public class SampleDTO {
    private String name;
    private int age;
}
```

**요청 예시**: 
- `GET http://localhost:8080/ex5?name=홍길동&age=25`

**처리 과정**:
1. Spring이 요청 파라미터를 `SampleDTO` 객체로 자동 바인딩
2. 파라미터 이름과 DTO 필드 이름이 일치하면 자동 매핑
   - `name` → `dto.name`
   - `age` → `dto.age` (String → int 자동 변환)
3. `@Setter` (Lombok)가 setter 메서드를 자동 생성
4. `@ToString` (Lombok)이 로그 출력 시 객체 내용을 문자열로 변환
5. 로그 출력 후 void 반환 → 뷰 이름: `ex5`

**자동 바인딩 규칙**:
- HTTP 파라미터 이름 = DTO 필드 이름
- 타입 변환 자동 처리 (String → int, String → Date 등)
- `@RequestParam` 없이도 객체로 바인딩 가능

---

## 데이터 바인딩

### 1. 단순 파라미터 바인딩

```java
@GetMapping("/ex4")
public void ex4(@RequestParam("name") String name) { ... }
```

**요청**: `?name=홍길동`
- `name` 파라미터가 `String name` 변수에 바인딩

### 2. 객체 바인딩

```java
@GetMapping("/ex5")
public void ex5(SampleDTO dto) { ... }
```

**요청**: `?name=홍길동&age=25`
- `name` → `dto.name`
- `age` → `dto.age` (String → int 자동 변환)

### 3. 기본값 설정

```java
@RequestParam(name="n1", defaultValue = "1") int num
```

- 파라미터가 없으면 기본값 `1` 사용

---

## 전체 흐름 요약

### 서버 시작 시

1. **Tomcat 서버 시작**
2. **ContextLoaderListener 실행**
   - `root-context.xml` 로드
   - Root ApplicationContext 생성
   - `HelloService`, `dataSource` 등 Bean 등록
3. **DispatcherServlet 초기화**
   - `servlet-context.xml` 로드
   - Servlet ApplicationContext 생성 (Root를 부모로 참조)
   - `HelloController` 등 Bean 등록
   - HandlerMapping, ViewResolver 등 설정

### 요청 처리 시

1. **요청 수신**: `GET /ex1`
2. **DispatcherServlet**: 요청 처리 시작
3. **HandlerMapping**: `@GetMapping("/ex1")` 찾기
4. **HandlerAdapter**: `HelloController.ex1()` 실행
5. **Controller**: 비즈니스 로직 처리, 뷰 이름 반환
6. **ViewResolver**: 뷰 이름 → JSP 경로 변환
7. **View**: JSP 렌더링
8. **응답**: HTML 반환

---

## 주요 어노테이션 설명

| 어노테이션 | 용도 | 위치 |
|-----------|------|------|
| `@Controller` | 컨트롤러 클래스 표시 | 클래스 |
| `@GetMapping` | GET 요청 매핑 | 메서드 |
| `@RequestParam` | 요청 파라미터 바인딩 | 메서드 파라미터 |
| `@Service` | 서비스 레이어 표시 | 클래스 |
| `@Setter` | Setter 메서드 자동 생성 (Lombok) | 클래스/필드 |
| `@ToString` | toString() 메서드 자동 생성 (Lombok) | 클래스 |

---

## 체크리스트

### ✅ 설정 확인 사항

- [x] `web.xml`에 DispatcherServlet 설정
- [x] `servlet-context.xml`에 `<mvc:annotation-driven/>` 설정
- [x] `servlet-context.xml`에 ViewResolver 설정
- [x] `servlet-context.xml`에 컨트롤러 패키지 스캔 설정
- [x] `root-context.xml`에 서비스 패키지 스캔 설정
- [x] 컨트롤러에 `@Controller` 어노테이션
- [x] 메서드에 `@GetMapping` 어노테이션
- [x] JSP 파일이 `/WEB-INF/views/` 경로에 존재

### ⚠️ 주의사항

1. **뷰 이름 반환 방식**:
   - `String` 반환: 명시적 뷰 이름
   - `void` 반환: URL 기반 암시적 뷰 이름

2. **파라미터 바인딩**:
   - `@RequestParam` 없이도 객체 바인딩 가능 (필드명 일치 시)
   - 타입 변환은 Spring이 자동 처리

3. **패키지 구조**:
   - 컨트롤러: `org.zerock.controller`
   - 서비스: `org.zerock.service`
   - DTO: `org.zerock.dto`

---

## 참고 자료

- Spring MVC 공식 문서: https://docs.spring.io/spring-framework/reference/web/webmvc.html

---

**작성일**: 2025-12-03  
**프로젝트**: sp1 (Spring MVC Web Application)

