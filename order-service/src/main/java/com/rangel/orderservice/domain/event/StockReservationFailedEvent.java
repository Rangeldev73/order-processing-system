package com.rangel.orderservice.domain.event;

import java.util.UUID;

public record StockReservationFailedEvent(UUID orderId, String rejectionReason) {}