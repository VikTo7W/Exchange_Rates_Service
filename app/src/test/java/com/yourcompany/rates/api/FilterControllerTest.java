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
public class FilterControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    FilterControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
        prepareEnvironment(false);
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
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andDo(print());
    }

    @Test
    public void get_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/filters/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Available"))
            .andDo(print());
    }

    @Test
    public void getBySourceId_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/filters").param("sourceId", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data.[0].name").value("Advertiser's 30D completion rate ≥"))
            .andDo(print());
    }

    @Test
    public void changeKeyPath_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(put("/api/admin/filters/{id}/changeKeyPath", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newKeyPath": "keyPath"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.keyPath").value("keyPath"))
            .andExpect(jsonPath("$.data.name").value("Available"))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(delete("/api/admin/filters/{id}", 3))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Advertiser's 30D completion rate ≥"))
            .andDo(print());
    }

    @Test
    public void getAggregate_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/filters/aggregate/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.filter.name").value("Available"))
            .andDo(print());
    }

    private void prepareEnvironment(boolean withFiltersAdded) throws Exception {
        mockMvc.perform(post("/api/admin/sources")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                   "id": 0,
                   "name": "Rapira",
                   "rateEndpointUrl": "https://api.rapira.net/market/exchange-plate-mini",
                   "rateEndpointType": "POST",
                   "rateEndpointPayloadType": "FORM_DATA",
                   "rateEndpointPayload": null,
                   "payloadCurrencyFormat": "@fromCurrency/@toCurrency",
                   "payloadFromCurrencyPath": "symbol",
                   "payloadToCurrencyPath": "symbol",
                   "payloadOrdersTypeKeyPath": null,
                   "payloadBuyOrdersValue": null,
                   "payloadSellOrdersValue": null,
                   "rateEndpointHeaders": null,
                   "rateArraySize": 24,
                   "rateKeyPath": "$.price",
                   "buyOrdersArrayKeyPath": "$.bid.items",
                   "sellOrdersArrayKeyPath": "$.ask.items",
                   "status": "UP"
                 }
            """));

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

        if (withFiltersAdded) {
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
                  "keyPath": "labelId",
                  "name": "Events",
                  "valueType": "STRING"
                }
            """));

            mockMvc.perform(post("/api/admin/filters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "id": 0,
                  "sourceId": 2,
                  "keyPath": "makerCompleteRate",
                  "name": "Advertiser's 30D completion rate ≥",
                  "valueType": "STRING"
                }
            """));
        }
    }
}
