package com.rangel.orderservice.domain.event;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentApprovedEvent(
        UUID orderId,
        UUID customerId,
        BigDecimal amount
) {
}