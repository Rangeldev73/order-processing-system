package com.rangel.paymentservice.infrastructure.messaging;

import com.rangel.paymentservice.application.usecase.ProcessPaymentUseCase;
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

    private final ProcessPaymentUseCase processPaymentUseCase;

    @RabbitListener(queues = QUEUE_NAME)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.orderId());

        processPaymentUseCase.execute(
                event.orderId(),
                event.customerId(),
                event.totalAmount()
        );
    }
}
