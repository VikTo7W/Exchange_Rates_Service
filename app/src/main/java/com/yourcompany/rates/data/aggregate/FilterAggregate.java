package com.yourcompany.rates.data.aggregate;

import com.yourcompany.rates.data.model.Filter;

import java.util.List;

public record FilterAggregate(
    Filter filter,
    List<FilterValueAggregate> values
) {}
