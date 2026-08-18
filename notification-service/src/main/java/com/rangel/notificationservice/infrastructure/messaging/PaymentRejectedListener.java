package com.rangel.notificationservice.infrastructure.messaging;

import com.rangel.notificationservice.application.usecase.NotifyPaymentRejectedUseCase;
import com.rangel.notificationservice.domain.event.PaymentRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.rangel.notificationservice.infrastructure.messaging.RabbitMQConfig.NOTIFICATION_PAYMENT_REJECTED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRejectedListener {

    private final NotifyPaymentRejectedUseCase notifyPaymentRejectedUseCase;

    @RabbitListener(queues = NOTIFICATION_PAYMENT_REJECTED_QUEUE)
    public void handlePaymentRejected(PaymentRejectedEvent event) {
        log.info("Received PaymentRejectedEvent for orderId: {}", event.orderId());

        notifyPaymentRejectedUseCase.execute(event);
    }
}
