package com.yourcompany.rates.data.aggregate;

import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.data.model.CurrencyPair;

public record CurrencyPairAggregate(
    CurrencyPair pair,
    Currency fromCurrency,
    Currency toCurrency
) {}
