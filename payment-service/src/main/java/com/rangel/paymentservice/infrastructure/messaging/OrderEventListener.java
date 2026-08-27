package com.rangel.paymentservice.infrastructure.messaging;

import com.rangel.paymentservice.application.port.in.ProcessPaymentInputPort;
import com.rangel.paymentservice.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.rangel.paymentservice.config.RabbitMQConfig.QUEUE_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final ProcessPaymentInputPort processPaymentInputPort;

    @RabbitListener(queues = QUEUE_NAME, containerFactory = "rabbitListenerContainerFactory")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.orderId());
        processPaymentInputPort.execute(
                event.orderId(),
                event.customerId(),
                event.totalAmount()
        );
    }
}
