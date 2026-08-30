package com.rangel.orderservice.application.usecase;

import com.rangel.orderservice.application.dto.command.CreateOrderCommand;
import com.rangel.orderservice.application.port.out.EventPublisherPort;
import com.rangel.orderservice.application.port.out.OrderRepositoryPort;
import com.rangel.orderservice.application.port.out.StockAvailabilityPort;
import com.rangel.orderservice.domain.event.OrderCreatedEvent;
import com.rangel.orderservice.domain.exception.InsufficientStockException;
import com.rangel.orderservice.domain.model.Order;
import com.rangel.orderservice.domain.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private StockAvailabilityPort stockAvailabilityPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void shouldCreateOrderAndPublishEventWhenStockIsAvailable() {
        UUID customerId = UUID.randomUUID();
        CreateOrderCommand.CreateOrderItemCommand itemCommand =
                new CreateOrderCommand.CreateOrderItemCommand("SKU-001", 2, new BigDecimal("50.00"));
        CreateOrderCommand command = new CreateOrderCommand(customerId, List.of(itemCommand));
        OrderItem orderItem = new OrderItem("SKU-001", 2, new BigDecimal("50.00"));
        Order savedOrder = Order.create(customerId, List.of(orderItem));

        when(stockAvailabilityPort.isAvailable(anyString(), anyInt())).thenReturn(true);
        when(orderRepositoryPort.save(any(Order.class))).thenReturn(savedOrder);

        Order result = createOrderUseCase.execute(command);

        assertNotNull(result);
        verify(stockAvailabilityPort).isAvailable("SKU-001", 2);
        verify(orderRepositoryPort).save(any(Order.class));
        verify(eventPublisherPort).publish(any(OrderCreatedEvent.class));
    }

    @Test
    void shouldThrowExceptionAndNotSaveOrderWhenStockIsUnavailable() {
        UUID customerId = UUID.randomUUID();
        CreateOrderCommand.CreateOrderItemCommand itemCommand =
                new CreateOrderCommand.CreateOrderItemCommand("SKU-001", 2, new BigDecimal("50.00"));
        CreateOrderCommand command = new CreateOrderCommand(customerId, List.of(itemCommand));
        when(stockAvailabilityPort.isAvailable(anyString(), anyInt())).thenReturn(false);

        assertThrows(
                InsufficientStockException.class,
                () -> createOrderUseCase.execute(command)
        );

        verify(stockAvailabilityPort).isAvailable("SKU-001", 2);
        verify(orderRepositoryPort, never()).save(any(Order.class));
        verify(eventPublisherPort, never()).publish(any());
    }
}