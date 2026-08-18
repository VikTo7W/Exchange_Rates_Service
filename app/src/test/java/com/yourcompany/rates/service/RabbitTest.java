package com.yourcompany.rates.service;

import com.yourcompany.rates.service.messaging.RateUpdate;
import com.yourcompany.rates.service.messaging.RabbitEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@SpringBootTest
@ActiveProfiles("test")
public class RabbitTest {

    @Autowired
    private RabbitEventPublisher publisher;

    @Test
    public void testRabbit() {
        publisher.publishRateUpdate(new RateUpdate("USDT/RUB", new BigDecimal("76.47"), OffsetDateTime.now(), 3));
    }
}
