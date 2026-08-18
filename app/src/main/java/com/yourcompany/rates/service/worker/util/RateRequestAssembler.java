package com.yourcompany.rates.service.worker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.yourcompany.rates.data.aggregate.FilterValueAggregate;
import com.yourcompany.rates.data.aggregate.RateBroadcastingSettingAggregate;
import com.yourcompany.rates.data.enums.JsonValueType;
import com.yourcompany.rates.data.enums.OrderType;
import com.yourcompany.rates.data.enums.PayloadType;
import com.yourcompany.rates.data.enums.RequestType;
import com.yourcompany.rates.data.model.CurrencyPairPerSource;
import com.yourcompany.rates.data.model.Filter;
import com.yourcompany.rates.service.crud.CurrencyPairPerSourceService;
import com.yourcompany.rates.service.crud.FilterService;
import com.yourcompany.rates.common.exception.GetRequestAssemblyException;
import com.yourcompany.rates.common.exception.PostRequestAssemblyException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

public class RateRequestAssembler {

    private Request.Builder assembler;
    private final FilterService filterService;
    private final Logger log;
    ObjectMapper mapper;
    private String baseUri;
    private RequestType requestType;
    private String headers;
    private PayloadType payloadType;
    private String stringPayload;
    private List<FilterValueAggregate> filterValues;
    private CurrencyPairPerSource curPair;
    private String fromCurrencyPath;
    private String toCurrencyPath;
    private String currencyFormat;
    private OrderType ordersType;
    private String ordersTypePath;
    private String buyOrdersValue;
    private String sellOrdersValue;

    public RateRequestAssembler(FilterService filterService, CurrencyPairPerSourceService curPairPerSourceService, RateBroadcastingSettingAggregate setting) {
        this.filterService = filterService;
        this.log = Logger.getLogger(RateRequestAssembler.class.getName());
        this.mapper = new ObjectMapper();
        this.assembler = new Request.Builder();
        this.baseUri = setting.source().rateEndpointUrl();
        this.requestType = setting.source().rateEndpointType();
        this.headers = setting.source().rateEndpointHeaders();
        this.payloadType = setting.source().rateEndpointPayloadType();
        this.stringPayload = setting.source().rateEndpointPayload();
        this.filterValues = setting.filters();
        this.fromCurrencyPath = setting.source().payloadFromCurrencyPath();
        this.toCurrencyPath = setting.source().payloadToCurrencyPath();
        this.currencyFormat = setting.source().payloadCurrencyFormat();
        this.ordersType = setting.setting().ordersType();
        this.ordersTypePath = setting.source().payloadOrdersTypeKeyPath();
        this.buyOrdersValue = setting.source().payloadBuyOrdersValue();
        this.sellOrdersValue = setting.source().payloadSellOrdersValue();
        this.curPair = curPairPerSourceService.getByIds(setting.source().id(), setting.setting().currencyPairId());
    }

    public Request assemble() {

        if (headers != null) {
            JsonNode headersJson;
            try {
                headersJson = mapper.readTree(headers);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            headersJson.fields().forEachRemaining(entry ->
                assembler.header(entry.getKey(), entry.getValue().asText())
            );
        }

        if (requestType == RequestType.POST) {
            try {
                assemblePost();
            } catch (Exception e) {
                log.severe("Error assembling POST request: " + e.getMessage());
                throw new PostRequestAssemblyException(e);
            }
        } else {
            try {
                assembleGet();
            } catch (Exception e) {
                log.severe("Error assembling GET request: " + e.getMessage());
                throw new GetRequestAssemblyException(e);
            }
        }
        return assembler.build();
    }

    private void assemblePost() {

        assembler.url(baseUri);

        if (payloadType == PayloadType.JSON) {

            DocumentContext payloadCtx = JsonPath.parse(stringPayload);

            if (fromCurrencyPath != null && toCurrencyPath != null) {
                if (Objects.equals(fromCurrencyPath, toCurrencyPath)) {
                    payloadCtx.set(fromCurrencyPath, currencyFormat
                        .replace("@fromCurrency", curPair.fromCurrencyValue())
                        .replace("@toCurrency", curPair.toCurrencyValue()));
                } else {
                    payloadCtx.set(fromCurrencyPath, convertStringToJsonType(curPair.fromCurrencyValue(), curPair.fromCurrencyValueType(), mapper));
                    payloadCtx.set(toCurrencyPath, convertStringToJsonType(curPair.toCurrencyValue(), curPair.toCurrencyValueType(), mapper));
                }
            }

            if (ordersTypePath != null) payloadCtx.set(ordersTypePath, ordersType == OrderType.BUY ? buyOrdersValue : sellOrdersValue);

            for (FilterValueAggregate filtValue : filterValues) {
                Filter filter = filterService.getById(filtValue.value().filterId());
                if (payloadCtx.read(filter.keyPath()) instanceof List) {
                    payloadCtx.add(filter.keyPath(), convertStringToJsonType(filtValue.value().value(), filter.valueType(), mapper));
                } else {
                    payloadCtx.set(filter.keyPath(), convertStringToJsonType(filtValue.value().value(), filter.valueType(), mapper));
                }
            }

            assembler.post(RequestBody.create(payloadCtx.jsonString(), MediaType.get("application/json")));

        } else {
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            if (fromCurrencyPath != null && toCurrencyPath != null) {
                if (Objects.equals(fromCurrencyPath, toCurrencyPath)) {
                    bodyBuilder.addFormDataPart(fromCurrencyPath, currencyFormat
                        .replace("@fromCurrency", curPair.fromCurrencyValue())
                        .replace("@toCurrency", curPair.toCurrencyValue()));
                } else {
                    bodyBuilder.addFormDataPart(fromCurrencyPath, curPair.fromCurrencyValue());
                    bodyBuilder.addFormDataPart(toCurrencyPath, curPair.toCurrencyValue());
                }
            }

            if (ordersTypePath != null) bodyBuilder.addFormDataPart(ordersTypePath, ordersType == OrderType.BUY ? buyOrdersValue : sellOrdersValue);

            for (FilterValueAggregate filtValue : filterValues) {
                Filter filter = filterService.getById(filtValue.value().filterId());
                bodyBuilder.addFormDataPart(filter.keyPath(), filtValue.value().value());
            }

            assembler.post(bodyBuilder.build());
        }
    }

    private void assembleGet() {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(payloadType ==  PayloadType.URL_PARAMETERS ? baseUri + stringPayload : baseUri);

        if (fromCurrencyPath != null && toCurrencyPath != null) {
            if (Objects.equals(fromCurrencyPath, toCurrencyPath)) {
                uriBuilder.replaceQueryParam(fromCurrencyPath, currencyFormat
                    .replace("@fromCurrency", curPair.fromCurrencyValue())
                    .replace("@toCurrency", curPair.toCurrencyValue()));
            } else {
                uriBuilder.replaceQueryParam(fromCurrencyPath, curPair.fromCurrencyValue());
                uriBuilder.replaceQueryParam(toCurrencyPath, curPair.toCurrencyValue());
            }
        }

        if (ordersTypePath != null) uriBuilder.replaceQueryParam(ordersTypePath, ordersType == OrderType.BUY ? buyOrdersValue : sellOrdersValue);
        MultiValueMap<String, String> existingParams;

        for (FilterValueAggregate filtValue : filterValues) {
            existingParams = new LinkedMultiValueMap<>(uriBuilder.build().getQueryParams());
            String parameter = filterService.getById(filtValue.value().filterId()).keyPath();
            String existing = existingParams.getFirst(parameter);
            if (existing != null && !existing.isBlank()) {
                uriBuilder.replaceQueryParam(parameter, existing + "," + filtValue.value().value());
            } else {
                uriBuilder.replaceQueryParam(parameter, filtValue.value().value());
            }
        }

        assembler.url(uriBuilder.build().toUriString());
    }



    public static Object convertStringToJsonType(
        String rawValue,
        JsonValueType type,
        ObjectMapper objectMapper
    ) {
        return switch (type) {
            case STRING -> rawValue;

            case NUMBER -> new BigDecimal(rawValue);

            case BOOLEAN -> Boolean.parseBoolean(rawValue);

            case OBJECT -> {
                try {
                    yield objectMapper.readValue(rawValue, Map.class);
                } catch (IOException e) {
                    throw new IllegalArgumentException(
                        "Invalid JSON object: " + rawValue, e
                    );
                }
            }
            case NULL -> null;
        };
    }
}
