package org.zerock.security;

import org.springframework.security.web.access.AccessDeniedHandler;

import lombok.extern.log4j.Log4j2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("Access Denied Handler");
        log.info("Redirect...");
        response.sendRedirect("/sample/access-denied");
    }

}
