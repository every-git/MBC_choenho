package org.zerock.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringJUnitConfig
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class RememberMeTest {

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testPersistentTokenRepository() throws Exception {
        System.out.println("PersistentTokenRepository: " + persistentTokenRepository);
        System.out.println("DataSource: " + dataSource);

        // 데이터베이스 연결 테스트
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("데이터베이스 연결 성공: " + conn.getMetaData().getURL());

            // persistent_logins 테이블 확인
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'persistent_logins'")) {
                if (rs.next()) {
                    System.out.println("persistent_logins 테이블 존재 확인됨");
                } else {
                    System.out.println("persistent_logins 테이블이 존재하지 않습니다!");
                }
            }

            // 테이블 구조 확인
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("DESCRIBE persistent_logins")) {
                System.out.println("\n테이블 구조:");
                while (rs.next()) {
                    System.out.println("컬럼: " + rs.getString("Field") + 
                                     ", 타입: " + rs.getString("Type") + 
                                     ", NULL: " + rs.getString("Null") + 
                                     ", Key: " + rs.getString("Key"));
                }
            }

            // 현재 데이터 확인
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM persistent_logins")) {
                System.out.println("\n현재 persistent_logins 데이터:");
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println("Username: " + rs.getString("username") + 
                                     ", Series: " + rs.getString("series") + 
                                     ", Token: " + rs.getString("token") + 
                                     ", LastUsed: " + rs.getTimestamp("last_used"));
                }
                if (count == 0) {
                    System.out.println("데이터가 없습니다.");
                }
            }
        }
    }
}

