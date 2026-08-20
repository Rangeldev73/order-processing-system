package com.rangel.inventoryservice.application.port.in;

import com.rangel.inventoryservice.domain.event.OrderCreatedEvent;

public interface ReserveStockInputPort {
    void execute(OrderCreatedEvent event);
}