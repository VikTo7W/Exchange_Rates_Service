package com.yourcompany.rates.api.dto.RateBroadcastingSettings;

import com.yourcompany.rates.data.model.RateBroadcastingSetting;

import java.util.List;

public record CreateSettingBody(
    RateBroadcastingSetting setting,
    List<Integer> filterValuesIds
) { }
