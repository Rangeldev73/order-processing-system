package com.rangel.paymentservice.application.usecase;

import com.rangel.paymentservice.application.port.in.ProcessPaymentInputPort;
import com.rangel.paymentservice.application.port.out.PaymentEventPublisherPort;
import com.rangel.paymentservice.application.port.out.PaymentRepositoryPort;
import com.rangel.paymentservice.domain.event.PaymentApprovedEvent;
import com.rangel.paymentservice.domain.event.PaymentRejectedEvent;
import com.rangel.paymentservice.domain.model.Payment;
import com.rangel.paymentservice.domain.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcessPaymentUseCase implements ProcessPaymentInputPort {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final PaymentEventPublisherPort paymentEventPublisherPort;

    @Override
    public Payment execute(UUID orderId, UUID customerId, BigDecimal amount) {
        Payment payment = Payment.process(orderId, customerId, amount);
        Payment savedPayment = paymentRepositoryPort.save(payment);

        if (savedPayment.getStatus() == PaymentStatus.APPROVED) {
            PaymentApprovedEvent event = new PaymentApprovedEvent(
                    savedPayment.getOrderId(),
                    savedPayment.getCustomerId(),
                    savedPayment.getAmount()
            );
            paymentEventPublisherPort.publishApproved(event);
        } else {
            PaymentRejectedEvent event = new PaymentRejectedEvent(
                    savedPayment.getOrderId(),
                    savedPayment.getRejectionReason()
            );
            paymentEventPublisherPort.publishRejected(event);
        }

        return savedPayment;
    }
}
