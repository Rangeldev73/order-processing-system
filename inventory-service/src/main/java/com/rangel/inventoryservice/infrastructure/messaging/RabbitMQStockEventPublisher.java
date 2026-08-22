package com.rangel.inventoryservice.infrastructure.messaging;

import com.rangel.inventoryservice.application.port.out.StockEventPublisherPort;
import com.rangel.inventoryservice.config.RabbitMQConfig;
import com.rangel.inventoryservice.domain.event.StockReservationFailedEvent;
import com.rangel.inventoryservice.domain.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQStockEventPublisher implements StockEventPublisherPort {

    private static final String STOCK_RESERVED_ROUTING_KEY = "stock.reserved";
    private static final String STOCK_RESERVATION_FAILED_ROUTING_KEY = "stock.reservation-failed";

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishReserved(StockReservedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.STOCK_EVENTS_EXCHANGE,
                STOCK_RESERVED_ROUTING_KEY,
                event
        );
    }

    @Override
    public void publishFailed(StockReservationFailedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.STOCK_EVENTS_EXCHANGE,
                STOCK_RESERVATION_FAILED_ROUTING_KEY,
                event
        );
    }
}