package com.rangel.orderservice.infrastructure.messaging;

import com.rangel.orderservice.application.port.in.FailPaymentInputPort;
import com.rangel.orderservice.domain.event.PaymentRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static com.rangel.orderservice.config.RabbitMQConfig.PAYMENT_REJECTED_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRejectedListener {

    private final FailPaymentInputPort failPaymentInputPort;

    @RabbitListener(queues = PAYMENT_REJECTED_QUEUE, containerFactory = "paymentRejectedContainerFactory")
    public void handlePaymentRejected(PaymentRejectedEvent event) {
        log.info("Received PaymentRejectedEvent for orderId: {}", event.orderId());

        failPaymentInputPort.execute(event.orderId());
    }
}