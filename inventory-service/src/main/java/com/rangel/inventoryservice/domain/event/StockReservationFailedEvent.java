package com.rangel.inventoryservice.domain.event;

import java.util.UUID;

public record StockReservationFailedEvent(
        UUID orderId,
        String rejectionReason
) {}