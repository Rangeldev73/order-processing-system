package com.rangel.orderservice.application.port.out;

import com.rangel.orderservice.domain.event.OrderCreatedEvent;

public interface EventPublisherPort {
    void publish(OrderCreatedEvent event);
}