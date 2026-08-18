package com.yourcompany.rates.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.rates.exchange}")
    private String exchange;

    @Value("${rabbitmq.rates.routing-key}")
    private String routingKey;

    @Bean
    FanoutExchange ratesExchange() {
        return new FanoutExchange(exchange);
    }

    @Bean
    Declarables ratesBindings(FanoutExchange exchange, List<Queue> queues) {
        return new Declarables(queues.stream().map(q -> BindingBuilder.bind(q).to(exchange)).toList()
        );
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    @Bean
    public AmqpTemplate rabbitTemplateCustom(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
