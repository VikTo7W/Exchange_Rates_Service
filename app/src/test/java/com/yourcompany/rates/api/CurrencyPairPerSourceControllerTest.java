package com.yourcompany.rates.api;

import com.yourcompany.rates.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class CurrencyPairPerSourceControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    CurrencyPairPerSourceControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
        prepareEnvironment(false);
        mockMvc.perform(post("/api/admin/currencyPairsPerSource")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "pairId": 1,
                  "sourceId": 1,
                  "fromCurrencyValue": "USDT",
                  "fromCurrencyValueType": "STRING",
                  "toCurrencyValue": "RUB",
                  "toCurrencyValueType": "STRING"
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
        mockMvc.perform(get("/api/admin/currencyPairsPerSource").param("sourceId", "1").param("pairId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.fromCurrencyValue").value("USDT"))
            .andExpect(jsonPath("$.data.toCurrencyValue").value("RUB"))
            .andDo(print());
    }

    @Test
    public void getBySourceId_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/currencyPairsPerSource").param("sourceId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data.[0].fromCurrencyValue").value("USDT"))
            .andExpect(jsonPath("$.data.[0].toCurrencyValue").value("RUB"))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(delete("/api/admin/currencyPairsPerSource").param("sourceId", "1").param("pairId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.fromCurrencyValue").value("USDT"))
            .andExpect(jsonPath("$.data.toCurrencyValue").value("RUB"))
            .andDo(print());
    }

    private void prepareEnvironment(boolean withCurPairAdded) throws Exception {
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

        if (withCurPairAdded) {
            mockMvc.perform(post("/api/admin/currencyPairsPerSource")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "pairId": 1,
                  "sourceId": 1,
                  "fromCurrencyValue": "USDT",
                  "fromCurrencyValueType": "STRING",
                  "toCurrencyValue": "RUB",
                  "toCurrencyValueType": "STRING"
                }
            """));

            mockMvc.perform(post("/api/admin/currencyPairsPerSource")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "pairId": 1,
                  "sourceId": 2,
                  "fromCurrencyValue": "3",
                  "fromCurrencyValueType": "STRING",
                  "toCurrencyValue": "11",
                  "toCurrencyValueType": "STRING"
                }
            """));
        }
    }

}
