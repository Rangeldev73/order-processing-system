package com.rangel.paymentservice.application.port.out;

import com.rangel.paymentservice.domain.event.PaymentApprovedEvent;
import com.rangel.paymentservice.domain.event.PaymentRejectedEvent;

public interface PaymentEventPublisherPort {
    void publishApproved(PaymentApprovedEvent event);
    void publishRejected(PaymentRejectedEvent event);
}