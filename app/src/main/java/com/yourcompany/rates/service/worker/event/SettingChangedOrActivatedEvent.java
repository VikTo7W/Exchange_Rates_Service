package com.yourcompany.rates.service.worker.event;

public record SettingChangedOrActivatedEvent(
    int settingId
) implements RateBroadcastingEvent{
}
