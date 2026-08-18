package com.yourcompany.rates.api.dto.RateBroadcastingSettings;

import com.yourcompany.rates.data.enums.BroadcastingStatus;

public record ChangeStatusBody(
    BroadcastingStatus newStatus
) {}
