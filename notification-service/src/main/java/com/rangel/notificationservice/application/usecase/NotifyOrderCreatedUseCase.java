package com.rangel.notificationservice.application.usecase;

import com.rangel.notificationservice.domain.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotifyOrderCreatedUseCase {
    public void execute(OrderCreatedEvent event) {
        log.info("[NOTIFICATION] Order {} received for customer {}. Total: {}",
                event.orderId(), event.customerId(), event.totalAmount());
    }
}