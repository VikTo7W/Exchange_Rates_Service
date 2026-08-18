package com.yourcompany.rates.data.model;

import com.yourcompany.rates.data.enums.JsonValueType;

public record Filter(
    int id,
    int sourceId,
    String keyPath,
    String name,
    JsonValueType valueType
) {}
