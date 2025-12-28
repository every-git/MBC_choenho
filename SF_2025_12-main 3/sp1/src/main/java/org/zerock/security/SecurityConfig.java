package org.zerock.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import java.sql.Connection;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // root-context에서 로드되므로 AntPathRequestMatcher를 명시적으로 사용
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/resources/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository persistentTokenRepository) throws Exception {
        log.info("---------filterChain---------");

        // rememberMe 설정은 formLogin 이후에 설정해야 함

        // root-context에서 로드되므로 MvcRequestMatcher 사용 불가
        // AntPathRequestMatcher를 명시적으로 사용하여 MvcRequestMatcher 사용 방지
        AntPathRequestMatcher rootMatcher = new AntPathRequestMatcher("/");
        AntPathRequestMatcher accountLoginMatcher = new AntPathRequestMatcher("/account/login");
        AntPathRequestMatcher accountLogoutMatcher = new AntPathRequestMatcher("/account/logout");
        AntPathRequestMatcher sampleLoginMatcher = new AntPathRequestMatcher("/sample/login");
        AntPathRequestMatcher sampleAccessDeniedMatcher = new AntPathRequestMatcher("/sample/access-denied");

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(rootMatcher, accountLoginMatcher, accountLogoutMatcher, sampleLoginMatcher, sampleAccessDeniedMatcher).permitAll()
                .anyRequest().authenticated()
            )
            .rememberMe(remember -> {
                remember
                    .key("remember-me-key")
                    .tokenRepository(persistentTokenRepository)
                    .tokenValiditySeconds(86400 * 30) // 30일
                    .userDetailsService(customUserDetailsService)
                    .rememberMeParameter("remember-me") // 파라미터 이름 명시적 지정
                    .alwaysRemember(false); // 체크박스가 체크되었을 때만 작동
                log.info("Remember Me 설정 완료 - tokenRepository: {}", persistentTokenRepository);
                log.info("Remember Me 파라미터 이름: remember-me");
            })
            .formLogin(form -> form
                .loginPage("/account/login")
                .loginProcessingUrl("/login")
                .successHandler(new CustomLoginSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/account/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(config -> {
                config.accessDeniedHandler(custom403Handler());
            });

        return http.build();
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        LoggingPersistentTokenRepository repo = new LoggingPersistentTokenRepository();
        repo.setDataSource(dataSource);
        log.info("PersistentTokenRepository 초기화 완료");
        log.info("DataSource: {}", dataSource);
        
        // 데이터베이스 연결 테스트
        try (Connection conn = dataSource.getConnection()) {
            log.info("데이터베이스 연결 성공: {}", conn.getMetaData().getURL());
            log.info("데이터베이스 사용자: {}", conn.getMetaData().getUserName());
            
            // persistent_logins 테이블 존재 확인
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery("SHOW TABLES LIKE 'persistent_logins'")) {
                if (rs.next()) {
                    log.info("persistent_logins 테이블 존재 확인됨");
                } else {
                    log.warn("persistent_logins 테이블이 존재하지 않습니다!");
                }
            }
        } catch (Exception e) {
            log.error("데이터베이스 연결 실패", e);
        }
        
        return repo;
    }

    @Bean
    public AccessDeniedHandler custom403Handler() {
        return new Custom403Handler();
    }

}
