package com.rangel.orderservice.application.usecase;

import com.rangel.orderservice.application.port.out.OrderRepositoryPort;
import com.rangel.orderservice.domain.exception.OrderNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ApproveOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public ApproveOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public void execute(UUID orderId) {
        var order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        order.approve();

        orderRepositoryPort.save(order);
    }
}