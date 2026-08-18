package com.yourcompany.rates.service.worker.event;

public record SettingDeactivatedEvent(
    int settingId,
    String reason
) implements RateBroadcastingEvent{
}
