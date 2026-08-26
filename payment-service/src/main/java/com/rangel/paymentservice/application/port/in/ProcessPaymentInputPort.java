package com.rangel.paymentservice.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProcessPaymentInputPort {
    void execute(UUID orderId, UUID customerId, BigDecimal amount);
}