# Module Context

Resources 디렉토리는 애플리케이션의 설정 파일, 정적 리소스, 템플릿 파일을 관리합니다.
Spring Boot 설정, MyBatis XML Mapper, Thymeleaf 템플릿, 정적 파일(CSS, JS, 이미지) 등이 포함됩니다.

## Structure
```
src/main/resources/
├── application.properties    # Spring Boot 설정
├── mapper/                   # MyBatis XML Mapper
├── static/                   # 정적 파일 (CSS, JS, Images)
└── templates/                # Thymeleaf 템플릿
```

## Responsibilities
- 애플리케이션 설정 관리
- 데이터베이스 연결 설정
- MyBatis 설정
- 뷰 템플릿 관리
- 정적 리소스 제공

## What NOT to Do
- 민감 정보를 평문으로 저장 (환경 변수 사용)
- 대용량 파일 포함 (외부 스토리지 활용)
- Java 코드 포함 (src/main/java 사용)

# application.properties

## Current Configuration
```properties
spring.application.name=demo
server.port=8080

# DB 연결 설정
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/springdb?serverTimezone=Asia/Seoul&characterEncoding=utf-8
spring.datasource.username=springdbuser
spring.datasource.password=1234

# MyBatis 설정
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=com.example.demo.domain
mybatis.configuration.map-underscore-to-camel-case=true
```

## Configuration Categories

### Server Settings
```properties
# 서버 포트
server.port=8080

# Context Path
server.servlet.context-path=/

# Session Timeout (30분)
server.servlet.session.timeout=30m
```

### Database Settings
```properties
# DataSource 설정
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/springdb?serverTimezone=Asia/Seoul&characterEncoding=utf-8
spring.datasource.username=springdbuser
spring.datasource.password=1234

# HikariCP Connection Pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

### MyBatis Settings
```properties
# XML Mapper 위치
mybatis.mapper-locations=classpath:mapper/*.xml

# Type Aliases 패키지
mybatis.type-aliases-package=com.example.demo.domain

# CamelCase 자동 매핑
mybatis.configuration.map-underscore-to-camel-case=true

# SQL 로깅 (개발 시만 활성화)
mybatis.configuration.log-impl=org.apache.ibatis.logging.slf4j.Slf4jImpl
```

### Logging Settings
```properties
# 로그 레벨 설정
logging.level.root=INFO
logging.level.com.example.demo=DEBUG
logging.level.com.example.demo.mapper=TRACE

# 로그 파일 설정
logging.file.name=logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30
```

### Thymeleaf Settings
```properties
# Thymeleaf 캐시 (개발: false, 운영: true)
spring.thymeleaf.cache=false

# Thymeleaf 인코딩
spring.thymeleaf.encoding=UTF-8

# Thymeleaf 템플릿 위치
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

### DevTools Settings
```properties
# DevTools 활성화
spring.devtools.restart.enabled=true

# 재시작 제외 경로
spring.devtools.restart.exclude=static/**,templates/**
```

## Environment-Specific Configuration

### Profile 분리
```properties
# application.properties (기본)
spring.profiles.active=dev

# application-dev.properties (개발)
spring.datasource.url=jdbc:mysql://localhost:3306/springdb_dev
logging.level.com.example.demo=DEBUG

# application-prod.properties (운영)
spring.datasource.url=jdbc:mysql://prod-server:3306/springdb
logging.level.com.example.demo=INFO
```

### Environment Variables (권장)
```properties
# 환경 변수 사용
spring.datasource.username=${DB_USERNAME:springdbuser}
spring.datasource.password=${DB_PASSWORD:1234}
```

# Static Resources

## Directory Structure
```
static/
├── css/
│   └── style.css
├── js/
│   └── app.js
└── images/
    └── logo.png
```

## Access Path
```html
<!-- Thymeleaf 템플릿에서 접근 -->
<link rel="stylesheet" th:href="@{/css/style.css}">
<script th:src="@{/js/app.js}"></script>
<img th:src="@{/images/logo.png}" alt="Logo">
```

## Static Resource Configuration
```properties
# 정적 리소스 위치
spring.web.resources.static-locations=classpath:/static/

# 캐시 설정 (초 단위)
spring.web.resources.cache.cachecontrol.max-age=3600
```

# Templates (Thymeleaf)

## Directory Structure
```
templates/
├── member/
│   ├── list.html
│   └── detail.html
├── layout/
│   ├── header.html
│   └── footer.html
└── error/
    ├── 404.html
    └── 500.html
```

## Basic Template Example
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:text="${title}">Default Title</title>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <h1 th:text="${title}">Title</h1>

    <div th:if="${lists != null}">
        <ul>
            <li th:each="member : ${lists}" th:text="${member.name}"></li>
        </ul>
    </div>

    <script th:src="@{/js/app.js}"></script>
</body>
</html>
```

# Local Golden Rules

## Do's
- 민감 정보는 환경 변수로 관리 (${ENV_VAR}).
- Profile별로 설정 파일 분리 (dev, prod).
- 개발 환경에서는 Thymeleaf 캐시 비활성화.
- MyBatis SQL 로깅은 개발 시에만 TRACE 레벨 사용.
- 정적 리소스는 static/ 디렉토리에 체계적으로 정리.

## Don'ts
- application.properties에 DB 비밀번호 하드코딩 금지.
- 프로덕션 환경에서 DEBUG 로그 레벨 사용 금지.
- 대용량 파일을 static/에 포함하지 마라.
- 불필요한 설정을 application.properties에 추가하지 마라.
- Thymeleaf 템플릿에 비즈니스 로직 포함 금지.

## Common Mistakes
- Thymeleaf 캐시가 활성화되어 변경사항이 반영 안 되는 경우.
- Profile 설정 오류로 잘못된 DB에 연결되는 경우.
- 정적 리소스 경로 오류 (@{/} 누락).
- MyBatis mapper-locations 경로 오류.
- 환경 변수 기본값 누락으로 런타임 에러.

## Security Best Practices
```properties
# Bad: 하드코딩
spring.datasource.password=1234

# Good: 환경 변수
spring.datasource.password=${DB_PASSWORD}

# Better: 환경 변수 + 기본값 (개발용)
spring.datasource.password=${DB_PASSWORD:1234}
```

## Production Checklist
- [ ] DB 비밀번호가 환경 변수로 설정되었는가?
- [ ] 로그 레벨이 INFO 이상인가?
- [ ] Thymeleaf 캐시가 활성화되었는가?
- [ ] DevTools가 비활성화되었는가?
- [ ] Profile이 prod로 설정되었는가?
- [ ] 불필요한 디버그 설정이 제거되었는가?
