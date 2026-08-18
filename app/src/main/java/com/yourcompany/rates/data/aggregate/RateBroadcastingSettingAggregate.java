package com.yourcompany.rates.data.aggregate;

import com.yourcompany.rates.data.model.RateBroadcastingSetting;
import com.yourcompany.rates.data.model.RateSource;
import com.yourcompany.rates.data.model.SettingCalculationRule;

import java.util.List;

public record RateBroadcastingSettingAggregate(
    RateBroadcastingSetting setting,
    RateSource source,
    SettingCalculationRule calculationRule,
    CurrencyPairAggregate currencyPair,
    List<FilterValueAggregate> filters
) {}
