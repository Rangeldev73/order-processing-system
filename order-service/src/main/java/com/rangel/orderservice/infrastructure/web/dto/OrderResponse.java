package com.rangel.orderservice.infrastructure.web.dto;

import com.rangel.orderservice.domain.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID customerId,
        List<OrderItemResponse> items,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt
) {
    public record OrderItemResponse(
            String productId,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {}

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProductId(), item.getQuantity(), item.getUnitPrice(), item.getSubtotal()))
                        .toList(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}
