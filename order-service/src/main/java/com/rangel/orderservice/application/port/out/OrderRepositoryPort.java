package com.rangel.orderservice.application.port.out;

import com.rangel.orderservice.domain.model.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(UUID id);
}