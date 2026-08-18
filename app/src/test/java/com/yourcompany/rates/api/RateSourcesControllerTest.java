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
public class RateSourcesControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    RateSourcesControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
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
                   "rateKeyPath": "//*[@id=\\"hybrid-rate\\"]",
                   "buyOrdersArrayKeyPath": null,
                   "sellOrdersArrayKeyPath": null,
                   "status": "UP"
                 }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andDo(print());
    }

    @Test
    public void get_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/sources/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void getAll_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/sources"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andDo(print());
    }

    @Test
    public void getByStatus_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/sources").param("status", "UP"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data.[0].name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changeStatus_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changeStatus", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newStatus": "UP"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.status").value("UP"))
            .andExpect(jsonPath("$.data.name").value("Hybrid-Rate"))
            .andDo(print());
    }

    @Test
    public void changePayload_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changePayload", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newPayload": "payload"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.rateEndpointPayload").value("payload"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changePayloadCurrencyFormat_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changePayloadCurrencyFormat", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "payloadCurrencyFormat": "currencyFormat"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.payloadCurrencyFormat").value("currencyFormat"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changePayloadFromCurrencyPath_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changePayloadFromCurrencyPath", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newPayloadFromCurrencyPath": "fromCurrencyPath"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.payloadFromCurrencyPath").value("fromCurrencyPath"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changePayloadToCurrencyPath_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changePayloadToCurrencyPath", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newPayloadToCurrencyPath": "toCurrencyPath"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.payloadToCurrencyPath").value("toCurrencyPath"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changePayloadOrdersTypeKeyPath_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changePayloadOrdersTypeKeyPath", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newPayloadOrdersTypeKeyPath": "ordersTypeKeyPath"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.payloadOrdersTypeKeyPath").value("ordersTypeKeyPath"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changePayloadSellOrdersValue_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changePayloadSellOrdersValue", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newPayloadSellOrdersValue": "sellOrdersValue"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.payloadSellOrdersValue").value("sellOrdersValue"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changePayloadBuyOrdersValue_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changePayloadBuyOrdersValue", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newPayloadBuyOrdersValue": "buyOrdersValue"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.payloadBuyOrdersValue").value("buyOrdersValue"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changeHeaders_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changeHeaders", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newHeaders": "{\\"header\\": \\"header\\"}"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.rateEndpointHeaders").value("{\"header\": \"header\"}"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changeRateKeyPath_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changeRateKeyPath", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newRateKeyPath": "rateKeyPath"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.rateKeyPath").value("rateKeyPath"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changeBuyOrdersArrayKeyPath_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changeBuyOrdersArrayKeyPath", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newBuyOrdersArrayKeyPath": "buyOrdersArrayKeyPath"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.buyOrdersArrayKeyPath").value("buyOrdersArrayKeyPath"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void changeSellOrdersArrayKeyPath_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(put("/api/admin/sources/{id}/changeSellOrdersArrayKeyPath", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newSellOrdersArrayKeyPath": "sellOrdersArrayKeyPath"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.sellOrdersArrayKeyPath").value("sellOrdersArrayKeyPath"))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(delete("/api/admin/sources/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Rapira"))
            .andDo(print());

        mockMvc.perform(get("/api/admin/sources"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andDo(print());
    }

    @Test
    public void getAggregate_test() throws Exception {
        prepareEnvironment();
        mockMvc.perform(get("/api/admin/sources/aggregate/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.source.name").value("Rapira"))
            .andDo(print());
    }

    private void prepareEnvironment() throws Exception {
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
    }
}
