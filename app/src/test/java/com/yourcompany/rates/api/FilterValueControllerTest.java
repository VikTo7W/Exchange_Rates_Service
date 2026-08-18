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
public class FilterValueControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    FilterValueControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
        prepareEnvironment(false);
        mockMvc.perform(post("/api/admin/filterValues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "filterId": 1,
                  "currencyId": null,
                  "name": "Only Tradable",
                  "value": "true"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andDo(print());
    }

    @Test
    public void get_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/filterValues/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Only Tradable"))
            .andDo(print());
    }

    @Test
    public void getByFilterId_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/filterValues").param("filterId", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data.[0].name").value("0%"))
            .andExpect(jsonPath("$.data.[1].name").value("60%"))
            .andDo(print());
    }

    @Test
    public void getByFilterAndCurrencyId_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/filterValues").param("filterId", "2").param("currencyId", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data.[0].name").value("60%"))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(delete("/api/admin/filterValues/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Only Tradable"))
            .andDo(print());

        mockMvc.perform(get("/api/admin/filterValues").param("filterId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(0))
            .andDo(print());
    }

    @Test
    public void getAggregate_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/filterValues/aggregate/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.value.name").value("Only Tradable"))
            .andExpect(jsonPath("$.data.currency").value(nullValue()))
            .andDo(print());

        mockMvc.perform(get("/api/admin/filterValues/aggregate/{id}", 2))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.value.name").value("0%"))
            .andExpect(jsonPath("$.data.currency").value(notNullValue()))
            .andDo(print());
    }

    private void prepareEnvironment(boolean withFilterValuesAdded) throws Exception {
        mockMvc.perform(post("/api/admin/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                   "id": 0,
                   "name": "Hybrid-Rate",
                   "rateEndpointUrl": "https://hybrid-rate.com/",
                   "rateEndpointType": "GET",
                   "rateEndpointPayloadType": "NONE",
                   "rateEndpointPayload": null,
                   "payloadCurrencyFormat": null,
                   "payloadFromCurrencyPath": null,
                   "payloadToCurrencyPath": null,
                   "payloadOrdersTypeKeyPath": null,
                   "payloadBuyOrdersValue": null,
                   "payloadSellOrdersValue": null,
                   "rateEndpointHeaders": null,
                   "rateArraySize": 1,
                   "rateKeyPath": "$.price",
                   "buyOrdersArrayKeyPath": "$.bid.items",
                   "sellOrdersArrayKeyPath": "$.ask.items",
                   "status": "DOWN"
                 }
            """));


            mockMvc.perform(post("/api/admin/filters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "sourceId": 1,
                  "keyPath": "onlyTradable",
                  "name": "Available",
                  "valueType": "STRING"
                }
            """));

            mockMvc.perform(post("/api/admin/filters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "sourceId": 1,
                  "keyPath": "makerCompleteRate",
                  "name": "Advertiser's 30D completion rate ≥",
                  "valueType": "STRING"
                }
            """));

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

        if (withFilterValuesAdded) {
            mockMvc.perform(post("/api/admin/filterValues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "filterId": 1,
                  "currencyId": null,
                  "name": "Only Tradable",
                  "value": "true"
                }
            """));

            mockMvc.perform(post("/api/admin/filterValues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "filterId": 2,
                  "currencyId": 1,
                  "name": "0%",
                  "value": "0"
                }
            """));

            mockMvc.perform(post("/api/admin/filterValues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "filterId": 2,
                  "currencyId": 2,
                  "name": "60%",
                  "value": "60"
                }
            """));
        }
    }
}
