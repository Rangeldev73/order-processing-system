package com.rangel.inventoryservice.infrastructure.messaging;

import com.rangel.inventoryservice.application.port.in.ReserveStockInputPort;
import com.rangel.inventoryservice.config.RabbitMQConfig;
import com.rangel.inventoryservice.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final ReserveStockInputPort reserveStockInputPort;

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.orderId());
        reserveStockInputPort.execute(event);
    }
}