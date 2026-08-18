package com.yourcompany.rates.data.aggregate;

import com.yourcompany.rates.data.model.RateSource;

import java.util.List;

public record RateSourceAggregate(
    RateSource source,
    List<CurrencyPairAggregate> supportedPairs,
    List<FilterAggregate> filters
) {}
