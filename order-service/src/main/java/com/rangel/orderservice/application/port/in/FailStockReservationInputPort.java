package com.rangel.orderservice.application.port.in;

import java.util.UUID;

public interface FailStockReservationInputPort {
    void execute(UUID orderId);
}