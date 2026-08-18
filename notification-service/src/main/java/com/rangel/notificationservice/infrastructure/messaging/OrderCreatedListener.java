package com.rangel.notificationservice.infrastructure.messaging;

import com.rangel.notificationservice.application.usecase.NotifyOrderCreatedUseCase;
import com.rangel.notificationservice.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.rangel.notificationservice.infrastructure.messaging.RabbitMQConfig.NOTIFICATION_ORDER_CREATED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {

    private final NotifyOrderCreatedUseCase notifyOrderCreatedUseCase;

    @RabbitListener(queues = NOTIFICATION_ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for orderId: {}", event.orderId());

        notifyOrderCreatedUseCase.execute(event);
    }
}
