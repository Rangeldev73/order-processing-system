package com.rangel.orderservice.domain.event;

import java.util.UUID;

public record StockReservedEvent(UUID orderId) {}