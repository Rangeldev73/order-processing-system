package com.rangel.orderservice.infrastructure.messaging;

import com.rangel.orderservice.application.usecase.ApproveOrderUseCase;
import com.rangel.orderservice.domain.event.PaymentApprovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.rangel.orderservice.config.RabbitMQConfig.PAYMENT_APPROVED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentApprovedListener {

    private final ApproveOrderUseCase approveOrderUseCase;

    @RabbitListener(queues = PAYMENT_APPROVED_QUEUE)
    public void handlePaymentApproved(PaymentApprovedEvent event) {
        log.info("Received PaymentApprovedEvent for orderId: {}", event.orderId());
        approveOrderUseCase.execute(event.orderId());
    }
}
