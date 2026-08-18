package com.yourcompany.rates.data.model;

public record FilterValue(
    int id,
    int filterId,
    Integer currencyId,
    String name,
    String value
) {}
