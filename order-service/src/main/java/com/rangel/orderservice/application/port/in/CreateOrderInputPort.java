package com.rangel.orderservice.application.port.in;

import com.rangel.orderservice.application.dto.command.CreateOrderCommand;
import com.rangel.orderservice.domain.model.Order;

public interface CreateOrderInputPort {
    Order execute(CreateOrderCommand command);
}
