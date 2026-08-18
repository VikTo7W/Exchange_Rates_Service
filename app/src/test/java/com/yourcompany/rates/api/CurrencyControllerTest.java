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
public class CurrencyControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    CurrencyControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
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
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andDo(print());

        mockMvc.perform(post("/api/admin/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "name": "Etherium",
                  "ticker": "ETH",
                  "type": "DIGITAL",
                  "symbol": "ETH",
                  "rate": 50
                }
            """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").value(nullValue()))
            .andDo(print());
    }

    @Test
    public void get_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/currencies/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.ticker").value("USDT"))
            .andDo(print());
    }

    @Test
    public void getByType_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/currencies").param("type", "FIAT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data.[0].ticker").value("RUB"))
            .andDo(print());
    }

    @Test
    public void getAll_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/currencies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andDo(print());
    }

    @Test
    public void changeRate_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/currencies/{id}/changeRate", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "newRate": 1000
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.rate").value(1000))
            .andExpect(jsonPath("$.data.ticker").value("USDT"))
            .andDo(print());

        mockMvc.perform(put("/api/admin/currencies/{id}/changeRate", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newRate": 150
                }
            """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").value(nullValue()))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(delete("/api/admin/currencies/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.ticker").value("USDT"))
            .andDo(print());

        mockMvc.perform(get("/api/admin/currencies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andDo(print());
    }

    private void prepareEnvironment() throws Exception {
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
                  "name": "Ruble",
                  "ticker": "RUB",
                  "type": "FIAT",
                  "symbol": "₽",
                  "rate": 100
                }
            """));
    }
}
