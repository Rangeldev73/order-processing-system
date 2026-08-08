package com.rangel.orderservice.infrastructure.persistence.repository;

import com.rangel.orderservice.application.port.out.OrderRepositoryPort;
import com.rangel.orderservice.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderJpaRepository.findById(id);
    }
}
