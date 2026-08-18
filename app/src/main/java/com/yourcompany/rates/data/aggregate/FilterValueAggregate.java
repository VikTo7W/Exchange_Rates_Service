package com.yourcompany.rates.data.aggregate;

import com.yourcompany.rates.data.model.Currency;
import com.yourcompany.rates.data.model.FilterValue;

public record FilterValueAggregate(
    FilterValue value,
    Currency currency
) {}
