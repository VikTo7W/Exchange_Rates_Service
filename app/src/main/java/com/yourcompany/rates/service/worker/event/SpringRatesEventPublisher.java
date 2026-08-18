package com.yourcompany.rates.service.worker.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringRatesEventPublisher implements RatesEventPublisher {
    private final ApplicationEventPublisher springPublisher;

    public SpringRatesEventPublisher(
        ApplicationEventPublisher springPublisher
    ) {
        this.springPublisher = springPublisher;
    }

    @Override
    public void publish(RateBroadcastingEvent event) {
        springPublisher.publishEvent(event);
    }
}
