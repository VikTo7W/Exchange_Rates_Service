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
public class RateBroadcastingSettingsControllerTest extends PostgresTestContainer {

    private final MockMvc mockMvc;

    RateBroadcastingSettingsControllerTest(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void create_test() throws Exception {
        prepareEnvironment(false);
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
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andDo(print());

        mockMvc.perform(post("/api/admin/settings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "setting": {
                    "id": 0,
                    "name": "Hybrid ETH-RUB",
                    "sourceId": 2,
                    "calculationRuleId": 1,
                    "currencyPairId": 2,
                    "ordersType": "NONE",
                    "ordersWindowStart": 1,
                    "ordersWindowEnd": 1,
                    "broadcastingTimeout": 1000,
                    "broadcastingStatus": "ACTIVE"
                  },
                    "filterValuesIds": []
                  }
            """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").value(nullValue()))
            .andDo(print());

        mockMvc.perform(post("/api/admin/settings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "setting": {
                    "id": 0,
                    "name": "Hybrid ETH-RUB",
                    "sourceId": 1,
                    "calculationRuleId": 1,
                    "currencyPairId": 2,
                    "ordersType": "BUY",
                    "ordersWindowStart": 25,
                    "ordersWindowEnd": 40,
                    "broadcastingTimeout": 1000,
                    "broadcastingStatus": "ACTIVE"
                  },
                    "filterValuesIds": []
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
        mockMvc.perform(get("/api/admin/settings/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Rapira USDT-RUB"))
            .andDo(print());
    }

    @Test
    public void getByStatus_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/settings").param("status", "ACTIVE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data.[0].name").value("Rapira USDT-RUB"))
            .andDo(print());
    }

    @Test
    public void getAll_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/settings"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andDo(print());
    }

    @Test
    public void changeStatus_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(put("/api/admin/settings/{id}/changeStatus", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newStatus": "INACTIVE"
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.broadcastingStatus").value("INACTIVE"))
            .andExpect(jsonPath("$.data.name").value("Rapira USDT-RUB"))
            .andDo(print());
    }

    @Test
    public void changeTimeout_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(put("/api/admin/settings/{id}/changeTimeout", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newTimeout": 2000
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.broadcastingTimeout").value(2000))
            .andExpect(jsonPath("$.data.name").value("Rapira USDT-RUB"))
            .andDo(print());
    }

    @Test
    public void changeSource_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(put("/api/admin/settings/{id}/changeSource", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newSourceId": 3
                }
            """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.sourceId").value(3))
            .andExpect(jsonPath("$.data.name").value("Rapira USDT-RUB"))
            .andDo(print());

        mockMvc.perform(put("/api/admin/settings/{id}/changeSource", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                  "newSourceId": 2
                }
            """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").value(nullValue()))
            .andDo(print());
    }

    @Test
    public void delete_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(delete("/api/admin/settings/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.name").value("Rapira USDT-RUB"))
            .andDo(print());
    }

    @Test
    public void getAggregate_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/settings/aggregate/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.setting.name").value("Rapira USDT-RUB"))
            .andDo(print());
    }

    @Test
    public void getAggregatesByStatus_test() throws Exception {
        prepareEnvironment(true);
        mockMvc.perform(get("/api/admin/settings/aggregate").param("status", "ACTIVE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(notNullValue()))
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data.[0].setting.name").value("Rapira USDT-RUB"))
            .andDo(print());
    }

    private void prepareEnvironment(boolean withSettingsAdded) throws Exception {

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

        mockMvc.perform(post("/api/admin/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                   "id": 0,
                   "name": "Hybrid-Rate new",
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

        if (withSettingsAdded) {
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
        }
    }
}
