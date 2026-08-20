package com.rangel.inventoryservice.domain.event;

import java.util.UUID;

public record StockReservedEvent(
        UUID orderId
){}
