package com.yourcompany.rates;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public abstract class PostgresTestContainer {

    @Container
    static final PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("db")
            .withUsername("rates")
            .withPassword("rates")
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    @DynamicPropertySource
    static void registerDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    protected JdbcTemplate jdbc;


    @BeforeEach
    void cleanDatabase() {
        jdbc.execute("""
            TRUNCATE TABLE
                "FilterValuesPerSetting",
                "FiltersValues",
                "Filters",
                "FixRates",
                "RateBroadcastingSettings",
                "CurrencyPairsPerSource",
                "CurrencyPairs",
                "Currencies",
                "RateSources",
                "SettingCalculationRules"
            RESTART IDENTITY CASCADE
        """);
    }
}
