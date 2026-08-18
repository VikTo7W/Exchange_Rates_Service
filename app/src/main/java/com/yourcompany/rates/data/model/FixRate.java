package com.yourcompany.rates.data.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

public record FixRate(
    long id,
    int settingId,
    BigDecimal rate,
    OffsetDateTime collectedAt
) {}
