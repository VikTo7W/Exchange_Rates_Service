package com.yourcompany.rates.service.worker.event;

public sealed interface RateBroadcastingEvent permits SettingChangedOrActivatedEvent, SettingDeactivatedEvent, SourceDownEvent { }
