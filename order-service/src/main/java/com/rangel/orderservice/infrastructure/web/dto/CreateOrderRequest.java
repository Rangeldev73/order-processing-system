package com.rangel.orderservice.infrastructure.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID customerId,
        @NotEmpty List<@Valid CreateOrderItemRequest> items
) {
    public record CreateOrderItemRequest(
            @NotBlank String productId,
            @NotNull @Positive Integer quantity,
            @NotNull @Positive BigDecimal unitPrice
    ) {}
}
