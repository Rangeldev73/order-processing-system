package com.rangel.orderservice.domain.event;

import java.util.UUID;

public record PaymentRejectedEvent(
        UUID orderId,
        String rejectionReason
) {
}