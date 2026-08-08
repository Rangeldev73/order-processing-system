package com.rangel.orderservice.application.dto.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
        UUID customerId,
        List<CreateOrderItemCommand> items
) {
    public record CreateOrderItemCommand(
            String productId,
            Integer quantity,
            BigDecimal unitPrice
    ) {}
}
