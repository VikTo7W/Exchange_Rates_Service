package com.yourcompany.rates.service.worker.event;

public interface RatesEventPublisher {
    void publish(RateBroadcastingEvent event);
}
