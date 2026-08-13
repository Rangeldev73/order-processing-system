package com.rangel.orderservice.infrastructure.messaging;

import com.rangel.orderservice.application.port.out.EventPublisherPort;
import com.rangel.orderservice.config.RabbitMQConfig;
import com.rangel.orderservice.domain.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Component
public class RabbitMQEventPublisher implements EventPublisherPort {

    private static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EVENTS_EXCHANGE,
                ORDER_CREATED_ROUTING_KEY,
                event
        );
    }
}