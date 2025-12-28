package org.zerock.security;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("========== onAuthenticationSuccess ==========");
        log.info("authentication: {}", authentication);
        log.info(authentication.getPrincipal());
        log.info(authentication.getCredentials());
        authentication.getAuthorities().forEach(auth -> log.info(auth.getAuthority().toString()));

        // 모든 요청 파라미터 확인
        log.info("========== 요청 파라미터 전체 ==========");
        request.getParameterMap().forEach((key, values) -> {
            log.info("파라미터 {} = {}", key, java.util.Arrays.toString(values));
        });

        // Remember Me 파라미터 확인
        String rememberMe = request.getParameter("remember-me");
        log.info("========== Remember Me 확인 ==========");
        log.info("Remember Me 파라미터: {}", rememberMe);
        log.info("Remember Me 체크 여부: {}", rememberMe != null && rememberMe.equals("on"));
        
        // 쿠키 확인
        log.info("========== 현재 쿠키 ==========");
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                log.info("쿠키: {} = {}", cookie.getName(), cookie.getValue());
            }
        }

        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);

        log.info("savedRequest: {}", savedRequest);

        // Remember Me 처리를 위해 필터 체인이 완료될 때까지 대기
        // Spring Security의 Remember Me는 필터 체인에서 처리되므로,
        // 리다이렉트 전에 Remember Me 처리가 완료되어야 합니다.
        // 하지만 실제로는 필터 체인이 완료된 후에 리다이렉트가 발생하므로,
        // Remember Me 처리는 필터 체인에서 자동으로 처리됩니다.
        
        // 리다이렉트 전에 쿠키를 다시 확인 (Remember Me 쿠키가 생성되었는지)
        log.info("========== 리다이렉트 전 쿠키 확인 ==========");
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                log.info("쿠키: {} = {}", cookie.getName(), cookie.getValue());
            }
        }

        if(savedRequest != null) {
            response.sendRedirect(savedRequest.getRedirectUrl());
        } else {
            response.sendRedirect("/board/list");
        }
    }

}
