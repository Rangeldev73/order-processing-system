package com.rangel.orderservice.application.usecase;

import com.rangel.orderservice.application.port.out.OrderRepositoryPort;
import com.rangel.orderservice.domain.exception.OrderNotFoundException;
import com.rangel.orderservice.domain.model.RejectionReason;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class RejectOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public RejectOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    public void execute(UUID orderId, RejectionReason rejectionReason) {
        var order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        order.reject(rejectionReason);

        orderRepositoryPort.save(order);
    }
}