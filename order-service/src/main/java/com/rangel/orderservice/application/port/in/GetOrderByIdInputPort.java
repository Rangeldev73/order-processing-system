package com.rangel.orderservice.application.port.in;

import com.rangel.orderservice.domain.model.Order;
import java.util.UUID;

public interface GetOrderByIdInputPort {
    Order execute(UUID id);
}