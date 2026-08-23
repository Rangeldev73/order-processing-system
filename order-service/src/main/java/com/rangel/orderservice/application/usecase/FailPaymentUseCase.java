package com.rangel.orderservice.application.usecase;

import com.rangel.orderservice.application.port.in.FailPaymentInputPort;
import com.rangel.orderservice.application.port.out.OrderRepositoryPort;
import com.rangel.orderservice.domain.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FailPaymentUseCase implements FailPaymentInputPort {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    @Retryable(
            retryFor = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    public void execute(UUID orderId) {
        var order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        order.markPaymentRejected();

        orderRepositoryPort.save(order);
    }
}