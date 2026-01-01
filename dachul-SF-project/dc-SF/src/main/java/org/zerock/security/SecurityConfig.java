package org.zerock.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Spring Security 설정 클래스
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;

        /**
         * 정적 리소스는 보안 필터 제외
         */
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring()
                                .requestMatchers(new AntPathRequestMatcher("/resources/**"));
        }

        /**
         * 보안 필터 체인 설정
         */
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.info("---------filterChain---------");

                http
                                // URL 접근 권한 설정
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                new AntPathRequestMatcher("/"),
                                                                new AntPathRequestMatcher("/member/login"),
                                                                new AntPathRequestMatcher("/member/join"),
                                                                new AntPathRequestMatcher("/member/idCheck"),
                                                                new AntPathRequestMatcher("/member/access-denied"),
                                                                new AntPathRequestMatcher("/login"))
                                                .permitAll()
                                                .requestMatchers(
                                                                new AntPathRequestMatcher("/board/list"),
                                                                new AntPathRequestMatcher("/board/view"))
                                                .permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                                                .hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                // Remember Me 설정
                                .rememberMe(remember -> remember
                                                .key("daechul-remember-key")
                                                .tokenValiditySeconds(86400 * 7) // 7일
                                                .userDetailsService(customUserDetailsService)
                                                .rememberMeParameter("remember-me"))
                                // 폼 로그인 설정
                                .formLogin(form -> form
                                                .loginPage("/member/login")
                                                .loginProcessingUrl("/login")
                                                .usernameParameter("id")
                                                .passwordParameter("password")
                                                .successHandler(new CustomLoginSuccessHandler())
                                                .failureUrl("/member/login?error")
                                                .permitAll())
                                // 로그아웃 설정
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/member/login?logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID", "remember-me")
                                                .permitAll())
                                // CSRF 비활성화 (개발 편의)
                                .csrf(csrf -> csrf.disable())
                                // 접근 거부 핸들러
                                .exceptionHandling(config -> {
                                        config.accessDeniedHandler(custom403Handler());
                                });

                return http.build();
        }

        @Bean
        public AccessDeniedHandler custom403Handler() {
                return new Custom403Handler();
        }
}
