package com.yourcompany.rates.api;

import com.yourcompany.rates.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class CurrencyPairControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    CurrencyPairControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
        prepareEnvironment(false);
        mockMvc.perform(post("/api/admin/currencyPairs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "fromCurrencyId": 1,
                  "toCurrencyId": 3
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andDo(print());

        mockMvc.perform(post("/api/admin/currencyPairs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "fromCurrencyId": 1,
                  "toCurrencyId": 2
                }
            """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").value(nullValue()))
            .andDo(print());
    }

    @Test
    public void get_test() throws Exception {
        prepareEnvironment(true);

        mockMvc.perform(get("/api/admin/currencyPairs/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.fromCurrencyId").value(1))
            .andExpect(jsonPath("$.data.toCurrencyId").value(3))
            .andDo(print());
    }

    @Test
    public void getAll() throws Exception {
        prepareEnvironment(true);

        mockMvc.perform(get("/api/admin/currencyPairs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment(true);

        mockMvc.perform(delete("/api/admin/currencyPairs/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.fromCurrencyId").value(1))
            .andExpect(jsonPath("$.data.toCurrencyId").value(3))
            .andDo(print());

        mockMvc.perform(get("/api/admin/currencyPairs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andDo(print());
    }

    @Test
    public void getAggregate_test() throws Exception {
        prepareEnvironment(true);

        mockMvc.perform(get("/api/admin/currencyPairs/aggregate/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.fromCurrency.ticker").value("USDT"))
            .andExpect(jsonPath("$.data.toCurrency.ticker").value("RUB"))
            .andDo(print());
    }

    private void prepareEnvironment(boolean withPairsInserted) throws Exception {
        mockMvc.perform(post("/api/admin/currencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 0,
                  "name": "Crypto Dollar",
                  "ticker": "USDT",
                  "type": "DIGITAL",
                  "symbol": "USD₮",
                  "rate": 100
                }
            """));

        mockMvc.perform(post("/api/admin/currencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 0,
                  "name": "Etherium",
                  "ticker": "ETH",
                  "type": "DIGITAL",
                  "symbol": "ETH",
                  "rate": 100000000
                }
            """));

        mockMvc.perform(post("/api/admin/currencies")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 0,
                  "name": "Ruble",
                  "ticker": "RUB",
                  "type": "FIAT",
                  "symbol": "₽",
                  "rate": 100
                }
            """));

        if (withPairsInserted) {
            mockMvc.perform(post("/api/admin/currencyPairs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "fromCurrencyId": 1,
                  "toCurrencyId": 3
                }
            """));
            mockMvc.perform(post("/api/admin/currencyPairs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "fromCurrencyId": 2,
                  "toCurrencyId": 3
                }
            """));
        }
    }
}
