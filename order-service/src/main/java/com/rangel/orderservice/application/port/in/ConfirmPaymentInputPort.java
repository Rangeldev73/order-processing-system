package com.rangel.orderservice.application.port.in;

import java.util.UUID;

public interface ConfirmPaymentInputPort {
    void execute(UUID orderId);
}