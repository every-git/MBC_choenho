package org.zerock.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j2;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Log4j2
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // root-context에서 로드되므로 AntPathRequestMatcher를 명시적으로 사용
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/resources/**"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("---------filterChain---------");

        // root-context에서 로드되므로 MvcRequestMatcher 사용 불가
        // AntPathRequestMatcher를 명시적으로 사용하여 MvcRequestMatcher 사용 방지
        AntPathRequestMatcher sampleMatcher = new AntPathRequestMatcher("/sample/**");

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(sampleMatcher).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/sample/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/board/list", true)
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

            http.exceptionHandling(config -> {
                config.accessDeniedHandler(custom403Handler());
            });

        return http.build();
    }

    @Bean
    public AccessDeniedHandler custom403Handler() {
        return new Custom403Handler();
    }

}
