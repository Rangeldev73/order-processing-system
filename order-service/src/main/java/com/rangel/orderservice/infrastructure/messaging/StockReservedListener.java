package com.rangel.orderservice.infrastructure.messaging;

import com.rangel.orderservice.application.port.in.ConfirmStockReservationInputPort;
import com.rangel.orderservice.config.RabbitMQConfig;
import com.rangel.orderservice.domain.event.StockReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockReservedListener {

    private final ConfirmStockReservationInputPort confirmStockReservationInputPort;

    @RabbitListener(queues = RabbitMQConfig.STOCK_RESERVED_QUEUE, containerFactory = "stockReservedContainerFactory")
    public void handleStockReserved(StockReservedEvent event) {
        log.info("Received StockReservedEvent for orderId: {}", event.orderId());
        confirmStockReservationInputPort.execute(event.orderId());
    }
}