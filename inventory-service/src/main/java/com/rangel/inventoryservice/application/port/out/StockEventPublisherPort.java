package com.rangel.inventoryservice.application.port.out;

import com.rangel.inventoryservice.domain.event.StockReservationFailedEvent;
import com.rangel.inventoryservice.domain.event.StockReservedEvent;

public interface StockEventPublisherPort {
    void publishReserved(StockReservedEvent event);
    void publishFailed(StockReservationFailedEvent event);
}