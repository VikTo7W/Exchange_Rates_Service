package com.yourcompany.rates.api.dto.RateSources;

import com.yourcompany.rates.data.enums.SourceStatus;

public record ChangeStatusBody(
    SourceStatus newStatus
) {}
