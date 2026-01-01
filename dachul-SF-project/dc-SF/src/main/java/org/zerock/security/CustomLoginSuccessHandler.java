package org.zerock.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

/**
 * 로그인 성공 후 처리 핸들러
 * 
 * - 이전 요청 페이지로 리다이렉트
 * - 기본값: 게시판 목록 페이지
 */
@Log4j2
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("========== onAuthenticationSuccess ==========");
        log.info("authentication: {}", authentication);
        log.info("principal: {}", authentication.getPrincipal());

        // 권한 출력
        authentication.getAuthorities().forEach(auth -> log.info("authority: {}", auth.getAuthority()));

        // 이전 요청 URL 확인
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        log.info("savedRequest: {}", savedRequest);

        String contextPath = request.getContextPath();

        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            log.info("Saved URL: {}", redirectUrl);

            // 로그인/로그아웃 관련 페이지면 기본 페이지로 이동 (무한 리다이렉트 방지)
            if (redirectUrl.contains("/member/login") ||
                    redirectUrl.contains("/logout") ||
                    redirectUrl.contains("/login")) {
                log.info("Ignoring login-related URL, redirecting to default");
                response.sendRedirect(contextPath + "/board/list");
            } else {
                log.info("Redirecting to saved URL: {}", redirectUrl);
                response.sendRedirect(redirectUrl);
            }
        } else {
            // 기본 리다이렉트: 게시판 목록
            log.info("Redirecting to default URL: {}/board/list", contextPath);
            response.sendRedirect(contextPath + "/board/list");
        }
    }
}
