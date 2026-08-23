package com.rangel.orderservice.infrastructure.messaging;

import com.rangel.orderservice.application.port.in.FailStockReservationInputPort;
import com.rangel.orderservice.config.RabbitMQConfig;
import com.rangel.orderservice.domain.event.StockReservationFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockReservationFailedListener {

    private final FailStockReservationInputPort failStockReservationInputPort;

    @RabbitListener(queues = RabbitMQConfig.STOCK_FAILED_QUEUE)
    public void handleStockFailed(StockReservationFailedEvent event) {
        log.info("Received StockReservationFailedEvent for orderId: {}, reason: {}", event.orderId(), event.rejectionReason());
        failStockReservationInputPort.execute(event.orderId());
    }
}