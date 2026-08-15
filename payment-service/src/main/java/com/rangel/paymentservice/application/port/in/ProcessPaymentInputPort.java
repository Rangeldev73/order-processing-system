package com.rangel.paymentservice.application.port.in;

import com.rangel.paymentservice.domain.model.Payment;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProcessPaymentInputPort {
    Payment execute(UUID orderId, UUID customerId, BigDecimal amount);
}