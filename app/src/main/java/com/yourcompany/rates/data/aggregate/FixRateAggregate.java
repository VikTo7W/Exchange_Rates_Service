package com.yourcompany.rates.data.aggregate;

import com.yourcompany.rates.data.model.FixRate;

public record FixRateAggregate(
    FixRate rate,
    RateBroadcastingSettingAggregate setting
) {}
