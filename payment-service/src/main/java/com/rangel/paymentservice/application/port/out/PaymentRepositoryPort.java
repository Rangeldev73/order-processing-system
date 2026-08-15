package com.rangel.paymentservice.application.port.out;

import com.rangel.paymentservice.domain.model.Payment;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
}
