package com.rangel.orderservice.application.usecase;


import com.rangel.orderservice.application.port.in.GetOrderByIdInputPort;
import com.rangel.orderservice.application.port.out.OrderRepositoryPort;
import com.rangel.orderservice.domain.exception.OrderNotFoundException;
import com.rangel.orderservice.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetOrderByIdUseCase implements GetOrderByIdInputPort {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    public Order execute(UUID id) {
        return orderRepositoryPort.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }
}