package com.yourcompany.rates.data.model;

import com.yourcompany.rates.data.enums.CurrencyType;

public record Currency(
    int id,
    String name,
    String ticker,
    CurrencyType type,
    String symbol,
    int rate
) {}
