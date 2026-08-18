package com.yourcompany.rates.data.model;

import com.yourcompany.rates.data.enums.JsonValueType;

public record CurrencyPairPerSource(
    int pairId,
    int sourceId,
    String fromCurrencyValue,
    JsonValueType fromCurrencyValueType,
    String toCurrencyValue,
    JsonValueType toCurrencyValueType
) {}
