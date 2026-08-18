package com.rangel.notificationservice.domain.event;

import java.util.UUID;

public record PaymentRejectedEvent(
        UUID orderId,
        String rejectionReason
) {
}