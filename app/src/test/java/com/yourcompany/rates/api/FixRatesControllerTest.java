package com.yourcompany.rates.api;

import com.yourcompany.rates.PostgresTestContainer;
import com.yourcompany.rates.data.model.FixRate;
import com.yourcompany.rates.service.crud.FixRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class FixRatesControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    @Autowired
    private FixRateService service;

    FixRatesControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void latest_test() throws Exception{
        prepareEnvironment();
        mockMvc.perform(get("/api/rates/{settingId}/latest", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.rate").value(77.74))
            .andDo(print());
    }

    @Test
    public void settingHistory_test() throws Exception{
        prepareEnvironment();
        mockMvc.perform(get("/api/rates/{settingId}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(3))
            .andDo(print());
    }

    @Test
    public void history_test() throws Exception{
        prepareEnvironment();
        mockMvc.perform(get("/api/rates/history"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(4))
            .andDo(print());
    }

    @Test
    public void getAggregateLatestBySettingId_test() throws Exception{
        prepareEnvironment();
        mockMvc.perform(get("/api/rates/aggregate/{settingId}/latest", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.rate.rate").value(77.74))
            .andDo(print());
    }

    @Test
    public void settingHistoryAggregates_test() throws Exception{
        prepareEnvironment();
        mockMvc.perform(get("/api/rates/aggregate/{settingId}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(3))
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

        mockMvc.perform(post("/api/admin/settingCalculationRules")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 0,
                  "name": "Main",
                  "marginPercentage": 0.0152,
                  "roundingType": "NEAREST",
                  "sourceStatOperation": "MEAN"
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
                  "currencyId": null,
                  "name": "0%",
                  "value": "0"
                }
            """));

        mockMvc.perform(post("/api/admin/settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "setting": {
                    "id": 0,
                    "name": "Rapira USDT-RUB",
                    "sourceId": 1,
                    "calculationRuleId": 1,
                    "currencyPairId": 1,
                    "ordersType": "BUY",
                    "ordersWindowStart": 1,
                    "ordersWindowEnd": 10,
                    "broadcastingTimeout": 1000,
                    "broadcastingStatus": "ACTIVE"
                  },
                    "filterValuesIds": [1,2]
                  }
            """));

        mockMvc.perform(post("/api/admin/settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "setting": {
                    "id": 0,
                    "name": "Rapira ETH-RUB",
                    "sourceId": 1,
                    "calculationRuleId": 1,
                    "currencyPairId": 2,
                    "ordersType": "BUY",
                    "ordersWindowStart": 1,
                    "ordersWindowEnd": 10,
                    "broadcastingTimeout": 1000,
                    "broadcastingStatus": "INACTIVE"
                  },
                    "filterValuesIds": []
                  }
            """));

        service.add(new FixRate(0, 1, new BigDecimal("77.92"), OffsetDateTime.now().minusSeconds(3)));
        service.add(new FixRate(0, 1, new BigDecimal("77.88"), OffsetDateTime.now().minusSeconds(2)));
        service.add(new FixRate(0, 1, new BigDecimal("77.74"), OffsetDateTime.now().minusSeconds(1)));
        service.add(new FixRate(0, 2, new BigDecimal("77.55"), OffsetDateTime.now()));
    }
}
