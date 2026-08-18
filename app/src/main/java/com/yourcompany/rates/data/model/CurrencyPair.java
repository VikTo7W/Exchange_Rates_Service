package com.yourcompany.rates.data.model;

public record CurrencyPair(
    int id,
    int fromCurrencyId,
    int toCurrencyId
) {}
