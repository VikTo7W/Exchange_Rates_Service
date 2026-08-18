package com.yourcompany.rates.data.model;

import com.yourcompany.rates.data.enums.PayloadType;
import com.yourcompany.rates.data.enums.RequestType;
import com.yourcompany.rates.data.enums.SourceStatus;

public record RateSource(
    int id,
    String name,
    String rateEndpointUrl,
    RequestType rateEndpointType,
    PayloadType rateEndpointPayloadType,
    String rateEndpointPayload,
    String payloadCurrencyFormat,
    String payloadFromCurrencyPath,
    String payloadToCurrencyPath,
    String payloadOrdersTypeKeyPath,
    String payloadBuyOrdersValue,
    String payloadSellOrdersValue,
    String rateEndpointHeaders,
    int rateArraySize,
    String rateKeyPath,
    String buyOrdersArrayKeyPath,
    String sellOrdersArrayKeyPath,
    SourceStatus status
) {}
