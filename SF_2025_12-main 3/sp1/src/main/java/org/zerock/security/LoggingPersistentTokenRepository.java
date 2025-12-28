package org.zerock.security;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggingPersistentTokenRepository extends JdbcTokenRepositoryImpl {
    
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        log.info("========== createNewToken 호출 ==========");
        log.info("Username: {}", token.getUsername());
        log.info("Series: {}", token.getSeries());
        log.info("Token: {}", token.getTokenValue());
        log.info("Date: {}", token.getDate());
        
        try {
            super.createNewToken(token);
            log.info("토큰 생성 성공!");
        } catch (Exception e) {
            log.error("토큰 생성 실패!", e);
            throw e;
        }
    }
    
    @Override
    public void updateToken(String series, String tokenValue, java.util.Date lastUsed) {
        log.info("========== updateToken 호출 ==========");
        log.info("Series: {}", series);
        log.info("Token: {}", tokenValue);
        log.info("LastUsed: {}", lastUsed);
        
        try {
            super.updateToken(series, tokenValue, lastUsed);
            log.info("토큰 업데이트 성공!");
        } catch (Exception e) {
            log.error("토큰 업데이트 실패!", e);
            throw e;
        }
    }
    
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        log.info("========== getTokenForSeries 호출 ==========");
        log.info("Series: {}", seriesId);
        
        try {
            PersistentRememberMeToken token = super.getTokenForSeries(seriesId);
            log.info("토큰 조회 결과: {}", token != null ? "존재함" : "없음");
            return token;
        } catch (Exception e) {
            log.error("토큰 조회 실패!", e);
            throw e;
        }
    }
    
    @Override
    public void removeUserTokens(String username) {
        log.info("========== removeUserTokens 호출 ==========");
        log.info("Username: {}", username);
        
        try {
            super.removeUserTokens(username);
            log.info("사용자 토큰 삭제 성공!");
        } catch (Exception e) {
            log.error("사용자 토큰 삭제 실패!", e);
            throw e;
        }
    }
}

