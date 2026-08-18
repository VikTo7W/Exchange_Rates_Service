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
public class FilterValuePerSettingControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    FilterValuePerSettingControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
        prepareEnvironment(false);
        mockMvc.perform(post("/api/admin/filterValuesPerSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "settingId": 2,
                  "filterValueId": 1
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andDo(print());
    }

    @Test
    public void getBySettingId_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/filterValuesPerSetting").param("settingId", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data.[0].filterValueId").value(1))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(delete("/api/admin/filterValuesPerSetting").param("settingId", "2").param("filterValueId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.filterValueId").value(1))
            .andDo(print());

        prepareEnvironment(true);
        mockMvc.perform(delete("/api/admin/filterValuesPerSetting").param("settingId", "1").param("filterValueId", "1"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").value(nullValue()))
            .andDo(print());
    }

    private void prepareEnvironment(boolean withFilterValuesAdded) throws Exception {
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

            if (withFilterValuesAdded) {
                mockMvc.perform(post("/api/admin/filterValuesPerSetting")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                {
                  "settingId": 2,
                  "filterValueId": 1
                }
            """));
            }

    }

}
