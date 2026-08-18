package com.yourcompany.rates.service.messaging;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange exchange;

    public RabbitEventPublisher(RabbitTemplate rabbitTemplate, FanoutExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void publishRateUpdate(RateUpdate rateUpdate) {
        rabbitTemplate.convertAndSend(exchange.getName(), "", rateUpdate);
    }
}
