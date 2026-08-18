package com.yourcompany.rates.service.worker.event;

public record SourceDownEvent(
    int sourceId,
    int settingId,
    String reason
) implements RateBroadcastingEvent{ }
