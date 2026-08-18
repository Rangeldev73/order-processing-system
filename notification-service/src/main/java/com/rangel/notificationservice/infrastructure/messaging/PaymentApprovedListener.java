package com.rangel.notificationservice.infrastructure.messaging;

import com.rangel.notificationservice.application.usecase.NotifyPaymentApprovedUseCase;
import com.rangel.notificationservice.domain.event.PaymentApprovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.rangel.notificationservice.infrastructure.messaging.RabbitMQConfig.NOTIFICATION_PAYMENT_APPROVED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentApprovedListener {

    private final NotifyPaymentApprovedUseCase notifyPaymentApprovedUseCase;

    @RabbitListener(queues = NOTIFICATION_PAYMENT_APPROVED_QUEUE)
    public void handlePaymentApproved(PaymentApprovedEvent event) {
        log.info("Received PaymentApprovedEvent for orderId: {}", event.orderId());

        notifyPaymentApprovedUseCase.execute(event);
    }
}
