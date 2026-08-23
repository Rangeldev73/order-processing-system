package com.rangel.orderservice.application.port.in;

import java.util.UUID;

public interface ConfirmStockReservationInputPort {
    void execute(UUID orderId);
}