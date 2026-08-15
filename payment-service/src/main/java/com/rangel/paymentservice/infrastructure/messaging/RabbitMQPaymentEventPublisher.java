package com.rangel.paymentservice.infrastructure.messaging;

import com.rangel.paymentservice.application.port.out.PaymentEventPublisherPort;
import com.rangel.paymentservice.config.RabbitMQConfig;
import com.rangel.paymentservice.domain.event.PaymentApprovedEvent;
import com.rangel.paymentservice.domain.event.PaymentRejectedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPaymentEventPublisher implements PaymentEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPaymentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishApproved(PaymentApprovedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_EVENTS_EXCHANGE, "payment.approved", event);
    }

    @Override
    public void publishRejected(PaymentRejectedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_EVENTS_EXCHANGE, "payment.rejected", event);
    }
}