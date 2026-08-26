package com.rangel.paymentservice.application.usecase;

import com.rangel.paymentservice.application.port.in.ProcessPaymentInputPort;
import com.rangel.paymentservice.application.port.out.PaymentEventPublisherPort;
import com.rangel.paymentservice.domain.event.PaymentApprovedEvent;
import com.rangel.paymentservice.domain.event.PaymentRejectedEvent;
import com.rangel.paymentservice.domain.model.Payment;
import com.rangel.paymentservice.domain.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessPaymentUseCase implements ProcessPaymentInputPort {

    private final PaymentTransactionalService paymentTransactionalService;
    private final PaymentEventPublisherPort paymentEventPublisherPort;

    @Override
    public void execute(UUID orderId, UUID customerId, BigDecimal amount) {
        try {
            Payment savedPayment = paymentTransactionalService.processAndSave(orderId, customerId, amount);

            if (savedPayment.getStatus() == PaymentStatus.APPROVED) {
                paymentEventPublisherPort.publishApproved(
                        new PaymentApprovedEvent(savedPayment.getOrderId(), savedPayment.getCustomerId(), savedPayment.getAmount())
                );
            } else {
                paymentEventPublisherPort.publishRejected(
                        new PaymentRejectedEvent(savedPayment.getOrderId(), savedPayment.getRejectionReason())
                );
            }

        } catch (DataIntegrityViolationException ex) {
            log.warn("Duplicate OrderCreatedEvent for orderId: {}. Ignoring payment processing.", orderId);
        }
    }
}