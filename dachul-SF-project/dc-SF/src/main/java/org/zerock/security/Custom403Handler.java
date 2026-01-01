package org.zerock.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * 접근 거부 (403) 처리 핸들러
 * 
 * 권한이 없는 페이지 접근 시 처리
 */
@Log4j2
public class Custom403Handler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.warn("========== Access Denied ==========");
        log.warn("Request URI: {}", request.getRequestURI());
        log.warn("Exception: {}", accessDeniedException.getMessage());

        // 에러 메시지를 세션에 저장
        request.getSession().setAttribute("accessDeniedMsg", "접근 권한이 없습니다.");

        // 접근 거부 페이지로 리다이렉트
        response.sendRedirect("/member/access-denied");
    }
}
