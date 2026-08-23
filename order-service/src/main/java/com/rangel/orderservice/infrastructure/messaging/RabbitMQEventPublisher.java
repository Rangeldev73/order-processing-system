package com.rangel.orderservice.infrastructure.messaging;

import com.rangel.orderservice.application.port.out.EventPublisherPort;
import com.rangel.orderservice.config.RabbitMQConfig;
import com.rangel.orderservice.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQEventPublisher implements EventPublisherPort {

    private static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EVENTS_EXCHANGE,
                ORDER_CREATED_ROUTING_KEY,
                event
        );
    }
}