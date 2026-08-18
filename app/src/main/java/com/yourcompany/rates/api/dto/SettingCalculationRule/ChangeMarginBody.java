package com.yourcompany.rates.api.dto.SettingCalculationRule;

import java.math.BigDecimal;

public record ChangeMarginBody(
    BigDecimal newMargin
) {}
