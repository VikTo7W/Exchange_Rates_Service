package com.yourcompany.rates.data.model;

import com.yourcompany.rates.data.enums.RoundingType;
import com.yourcompany.rates.data.enums.StatisticalOperation;
import java.math.BigDecimal;

public record SettingCalculationRule(
    int id,
    String name,
    BigDecimal marginPercentage,
    RoundingType roundingType,
    StatisticalOperation sourceStatOperation
) {}
