package com.yourcompany.rates.data.model;

import com.yourcompany.rates.data.enums.OrderType;
import com.yourcompany.rates.data.enums.BroadcastingStatus;

public record RateBroadcastingSetting(
    int id,
    String name,
    int sourceId,
    int calculationRuleId,
    int currencyPairId,
    OrderType ordersType,
    int ordersWindowStart,
    int ordersWindowEnd,
    int broadcastingTimeout,
    BroadcastingStatus broadcastingStatus
) {}
