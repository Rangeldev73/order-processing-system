package com.rangel.orderservice.application.port.in;

import java.util.UUID;

public interface FailPaymentInputPort {
    void execute(UUID orderId);
}