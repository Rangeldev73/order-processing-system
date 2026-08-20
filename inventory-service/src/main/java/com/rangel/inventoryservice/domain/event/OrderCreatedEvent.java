package com.rangel.inventoryservice.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID customerId,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<OrderItemPayload> items
) {
    public record OrderItemPayload(String productId, Integer quantity) {}
}