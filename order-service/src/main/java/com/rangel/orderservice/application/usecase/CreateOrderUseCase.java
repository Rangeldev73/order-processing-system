package com.rangel.orderservice.application.usecase;

import com.rangel.orderservice.application.dto.command.CreateOrderCommand;
import com.rangel.orderservice.application.port.in.CreateOrderInputPort;
import com.rangel.orderservice.application.port.out.OrderRepositoryPort;
import com.rangel.orderservice.domain.model.Order;
import com.rangel.orderservice.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderInputPort {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    public Order execute(CreateOrderCommand command) {
        List<OrderItem> items = command.items().stream()
                .map(item -> new OrderItem(item.productId(), item.quantity(), item.unitPrice()))
                .toList();

        Order order = Order.create(command.customerId(), items);

        return orderRepositoryPort.save(order);
    }
}
