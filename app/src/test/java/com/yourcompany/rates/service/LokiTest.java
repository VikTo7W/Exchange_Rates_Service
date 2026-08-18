package com.yourcompany.rates.service;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.logging.Logger;

@SpringBootTest
@ActiveProfiles("test")
public class LokiTest {

    private static final Logger log = Logger.getLogger(LokiTest.class.getName());

    @Test
    public void testLogs() {
        log.info("Test logs");
    }
}
