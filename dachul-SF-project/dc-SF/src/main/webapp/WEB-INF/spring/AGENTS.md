# Module Context

Spring Framework Configuration Files. XML 기반 설정.

## Role & Responsibilities

- Spring Bean 정의
- 의존성 주입 설정
- 데이터베이스 연결 설정
- MyBatis 설정
- MVC 설정
- Transaction Management 설정

## Configuration Files

- root-context.xml: 루트 애플리케이션 컨텍스트 (DB, Service, Repository)
- servlet-context.xml: DispatcherServlet 컨텍스트 (Controller, ViewResolver, Static Resources)
- security-context.xml: Spring Security 설정 (현재 미사용, SecurityConfig.java 사용)

# Implementation Patterns

## root-context.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:mybatis-spring="http://mybatis.org/schema/mybatis-spring"
    xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://mybatis.org/schema/mybatis-spring
        http://mybatis.org/schema/mybatis-spring.xsd
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!-- Component Scan (Controller 제외) -->
    <context:component-scan base-package="org.zerock">
        <context:exclude-filter type="annotation"
            expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>

    <!-- DataSource (HikariCP) -->
    <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver" />
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/daechul?serverTimezone=Asia/Seoul&amp;characterEncoding=UTF-8" />
        <property name="username" value="root" />
        <property name="password" value="1234" />
        <property name="maximumPoolSize" value="10" />
        <property name="minimumIdle" value="5" />
        <property name="connectionTimeout" value="30000" />
    </bean>

    <!-- SqlSessionFactory -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="configLocation" value="classpath:/mybatis-config.xml" />
        <property name="mapperLocations" value="classpath:/mapper/**/*.xml" />
    </bean>

    <!-- MyBatis Mapper Scan -->
    <mybatis-spring:scan base-package="org.zerock.mapper" />

    <!-- Transaction Manager -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <!-- Enable Annotation-Driven Transaction -->
    <tx:annotation-driven transaction-manager="transactionManager" />

</beans>
```

## servlet-context.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xmlns:mvc="http://www.springframework.org/schema/mvc"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- Enable Annotation-Driven MVC -->
    <mvc:annotation-driven />

    <!-- Static Resources -->
    <mvc:resources mapping="/resources/**" location="/resources/" />
    <mvc:resources mapping="/css/**" location="/resources/css/" />
    <mvc:resources mapping="/js/**" location="/resources/js/" />
    <mvc:resources mapping="/images/**" location="/resources/images/" />

    <!-- View Resolver -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/views/" />
        <property name="suffix" value=".jsp" />
    </bean>

    <!-- Component Scan (Controller만) -->
    <context:component-scan base-package="org.zerock.controller" />

</beans>
```

## web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
        https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
    version="6.0">

    <!-- Root Context -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring/root-context.xml</param-value>
    </context-param>

    <!-- ContextLoaderListener -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- DispatcherServlet -->
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

    <!-- Character Encoding Filter -->
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>

    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!-- Spring Security Filter -->
    <filter>
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!-- Welcome File -->
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

</web-app>
```

# Local Golden Rules

## DataSource Configuration

### Do's
- HikariCP 사용 (성능 최적화)
- serverTimezone=Asia/Seoul 필수
- characterEncoding=UTF-8 설정
- Connection Pool 크기 적절히 설정

### Don'ts
- DB 비밀번호 하드코딩 (운영 환경에서는 환경변수 또는 외부 설정 파일 사용)
- Connection Pool 크기 너무 크게 설정 금지

```xml
<!-- 개발 환경 -->
<property name="jdbcUrl" value="jdbc:mysql://localhost:3306/daechul?serverTimezone=Asia/Seoul&amp;characterEncoding=UTF-8" />

<!-- 운영 환경 (환경변수 사용 권장) -->
<property name="jdbcUrl" value="${db.url}" />
<property name="username" value="${db.username}" />
<property name="password" value="${db.password}" />
```

## Component Scan

### Do's
- root-context.xml: Controller 제외한 모든 컴포넌트
- servlet-context.xml: Controller만 스캔
- 명확한 패키지 분리

### Don'ts
- 중복 스캔 금지
- 너무 광범위한 스캔 금지

```xml
<!-- root-context.xml -->
<context:component-scan base-package="org.zerock">
    <context:exclude-filter type="annotation"
        expression="org.springframework.stereotype.Controller"/>
</context:component-scan>

<!-- servlet-context.xml -->
<context:component-scan base-package="org.zerock.controller" />
```

## Transaction Management

### Do's
- DataSourceTransactionManager 사용
- <tx:annotation-driven> 활성화
- Service 계층에서 @Transactional 사용

### Don'ts
- 여러 TransactionManager 중복 정의 금지

## Static Resources

### Do's
- <mvc:resources> 로 명시적 매핑
- 캐싱 설정 (운영 환경)

### Don'ts
- 모든 요청을 DispatcherServlet으로 보내지 말 것

```xml
<mvc:resources mapping="/resources/**" location="/resources/" cache-period="31536000" />
```

## View Resolver

### Do's
- InternalResourceViewResolver 사용
- prefix/suffix 명확히 설정

### Don'ts
- 여러 ViewResolver 중복 정의 시 order 주의

# Configuration Best Practices

## Property Placeholder

환경별 설정 분리:

```xml
<!-- root-context.xml -->
<context:property-placeholder location="classpath:db.properties" />

<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
    <property name="jdbcUrl" value="${db.url}" />
    <property name="username" value="${db.username}" />
    <property name="password" value="${db.password}" />
</bean>
```

db.properties:
```properties
db.url=jdbc:mysql://localhost:3306/daechul?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
db.username=root
db.password=1234
```

## Profile-based Configuration

```xml
<beans profile="dev">
    <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
        <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/daechul_dev" />
    </bean>
</beans>

<beans profile="prod">
    <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
        <property name="jdbcUrl" value="jdbc:mysql://prod-server:3306/daechul_prod" />
    </bean>
</beans>
```

# Testing Strategy

## Configuration Validation

```java
@SpringJUnitConfig(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
class ConfigTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void testDataSource() throws Exception {
        assertNotNull(dataSource);

        try (Connection conn = dataSource.getConnection()) {
            assertNotNull(conn);
            System.out.println("DB Connection Success");
        }
    }

    @Test
    void testSqlSessionFactory() {
        assertNotNull(sqlSessionFactory);
        System.out.println("SqlSessionFactory loaded");
    }

    @Test
    void testMyBatisConfiguration() throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            assertNotNull(session);
            System.out.println("SqlSession created");
        }
    }
}
```

# Related Contexts

- MyBatis Configuration: ../../../resources/AGENTS.md
- Security Configuration: ../../../java/org/zerock/security/AGENTS.md
- Controller Layer: ../../../java/org/zerock/controller/AGENTS.md
