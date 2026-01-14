package com.example.demo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@SpringBootTest
@Log4j2
class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("데이터베이스 연결 테스트")
    void testDatabaseConnection() {
        try {
            // DataSource를 통한 연결 테스트
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            
            log.info("=== 데이터베이스 연결 정보 ===");
            log.info("URL: " + metaData.getURL());
            log.info("Driver: " + metaData.getDriverName());
            log.info("Driver Version: " + metaData.getDriverVersion());
            log.info("Database Product: " + metaData.getDatabaseProductName());
            log.info("Database Version: " + metaData.getDatabaseProductVersion());
            log.info("Username: " + metaData.getUserName());
            log.info("=== 연결 성공! ===");
            
            // 간단한 쿼리 테스트
            String result = jdbcTemplate.queryForObject("SELECT 1", String.class);
            log.info("쿼리 테스트 결과: " + result);
            
            connection.close();
            
        } catch (Exception e) {
            log.error("데이터베이스 연결 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
