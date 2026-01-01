# Module Context

Spring Security Configuration Layer. 인증 및 인가 처리.

## Role & Responsibilities

- 사용자 인증 (Authentication)
- 권한 기반 접근 제어 (Authorization)
- 비밀번호 암호화
- 로그인/로그아웃 처리
- CSRF 보호
- 403/401 에러 핸들링

## Dependencies

- Spring Security 6.4.2
- BCryptPasswordEncoder
- UserDetailsService
- Member Service/Mapper

# Tech Stack & Constraints

## Spring Security Version

- Spring Security 6.4.2 (Spring 6.2.1과 호환)
- Jakarta EE 9+ (jakarta.servlet.*)

## Configuration Files

- SecurityConfig.java: Java Config 기반 설정
- web.xml: DelegatingFilterProxy 등록
- CustomUserDetailsService.java: 사용자 정보 로드
- CustomLoginSuccessHandler.java: 로그인 성공 처리
- Custom403Handler.java: 403 에러 처리
- PasswordEncoderConfig.java: 비밀번호 암호화 설정

# Implementation Patterns

## SecurityConfig (Java Config)

```java
package org.zerock.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/resources/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customLoginSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler(custom403Handler())
            )
            .csrf(csrf -> csrf.disable())  // 개발 중에만, 운영에서는 활성화
            .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler custom403Handler() {
        return new Custom403Handler();
    }
}
```

## PasswordEncoderConfig

```java
package org.zerock.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## CustomUserDetailsService

```java
package org.zerock.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.dto.MemberDTO;
import org.zerock.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: {}", username);

        MemberDTO member = memberMapper.selectByUsername(username);

        if (member == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // 권한 문자열을 SimpleGrantedAuthority로 변환
        // 예: "ROLE_USER,ROLE_ADMIN" -> [ROLE_USER, ROLE_ADMIN]
        List<SimpleGrantedAuthority> authorities = Arrays.stream(member.getRoles().split(","))
            .map(role -> new SimpleGrantedAuthority(role.trim()))
            .collect(Collectors.toList());

        return User.builder()
            .username(member.getUsername())
            .password(member.getPassword())  // 이미 암호화된 비밀번호
            .authorities(authorities)
            .build();
    }
}
```

## CustomLoginSuccessHandler

```java
package org.zerock.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        log.info("Login Success: {}", authentication.getName());

        // 권한별로 다른 페이지로 리다이렉트
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/member/mypage");
        }
    }
}
```

## Custom403Handler

```java
package org.zerock.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Custom403Handler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        log.warn("Access Denied: {}", accessDeniedException.getMessage());

        // 403 에러 페이지로 포워드
        request.setAttribute("errorMessage", "접근 권한이 없습니다.");
        request.getRequestDispatcher("/WEB-INF/views/error/403.jsp").forward(request, response);
    }
}
```

## web.xml (DelegatingFilterProxy 등록)

```xml
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>

<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

# Local Golden Rules

## Password Security

### Do's
- BCryptPasswordEncoder 사용 필수
- 회원가입 시 passwordEncoder.encode(rawPassword)
- 로그인 시 Spring Security가 자동 검증

### Don'ts
- 평문 비밀번호 저장 절대 금지
- 다른 암호화 알고리즘 사용 금지
- 비밀번호를 로그에 출력 금지

```java
// 회원가입 예시
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(MemberDTO dto) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);

        memberMapper.insert(dto);
    }
}
```

## Authority Management

### Do's
- 권한은 "ROLE_" 접두사 사용 (예: ROLE_USER, ROLE_ADMIN)
- hasRole() 사용 시 "ROLE_" 제외 (예: hasRole("USER"))
- hasAuthority() 사용 시 전체 문자열 (예: hasAuthority("ROLE_USER"))

### Don'ts
- 권한 문자열 직접 비교 지양
- 하드코딩된 권한 체크 금지

## CSRF Protection

### Do's
- 운영 환경에서는 CSRF 활성화 필수
- JSP에서 <sec:csrfInput/> 또는 ${_csrf.token} 사용

### Don'ts
- 운영 환경에서 csrf().disable() 금지
- 개발 중에만 임시로 비활성화 허용

```jsp
<!-- JSP에서 CSRF 토큰 포함 -->
<form method="post" action="/member/register">
    <sec:csrfInput/>
    <!-- 또는 -->
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    ...
</form>
```

## Session Management

### Do's
- 로그아웃 시 세션 무효화
- JSESSIONID 쿠키 삭제
- 동시 세션 제어 (필요 시)

### Don'ts
- 세션에 민감 정보 저장 금지
- 세션 타임아웃 너무 길게 설정 금지

## URL Pattern Security

### Do's
- 정적 리소스는 permitAll()
- 관리자 페이지는 hasRole("ADMIN")
- 로그인 페이지는 permitAll()
- 나머지는 authenticated()

### Pattern Example
```java
.requestMatchers("/", "/home", "/resources/**", "/css/**", "/js/**", "/images/**").permitAll()
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
.anyRequest().authenticated()
```

# Testing Strategy

## Security Test Pattern

```java
@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testMemberPageAccess() throws Exception {
        mockMvc.perform(get("/member/mypage"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testAdminPageDenied() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/member/mypage"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }
}
```

## Password Encoding Test

```java
@SpringJUnitConfig(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
})
class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPasswordEncoding() {
        String rawPassword = "password123";

        String encoded = passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, encoded);
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }

    @Test
    void testDifferentEncodedPasswords() {
        String rawPassword = "password123";

        String encoded1 = passwordEncoder.encode(rawPassword);
        String encoded2 = passwordEncoder.encode(rawPassword);

        // BCrypt는 매번 다른 해시 생성
        assertNotEquals(encoded1, encoded2);

        // 하지만 둘 다 매칭됨
        assertTrue(passwordEncoder.matches(rawPassword, encoded1));
        assertTrue(passwordEncoder.matches(rawPassword, encoded2));
    }
}
```

# Related Contexts

- Service Layer: ../service/AGENTS.md
- Controller Layer: ../controller/AGENTS.md
- Mapper Layer: ../mapper/AGENTS.md
- Views: ../../../webapp/WEB-INF/views/AGENTS.md
