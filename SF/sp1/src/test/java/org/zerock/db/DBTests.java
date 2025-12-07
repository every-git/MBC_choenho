package org.zerock.db;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.log4j.Log4j2;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class DBTests {
    
    private static final Logger log = LogManager.getLogger(DBTests.class);

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnection() {
        log.info("------------testConnection------------");
        log.info(dataSource);
        log.info("------------testConnection------------");
    }
}
