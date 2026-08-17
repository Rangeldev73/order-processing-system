package com.rangel.orderservice.infrastructure.web.controller;

import com.rangel.orderservice.application.dto.command.CreateOrderCommand;
import com.rangel.orderservice.application.port.in.CreateOrderInputPort;
import com.rangel.orderservice.application.port.in.GetOrderByIdInputPort;
import com.rangel.orderservice.domain.model.Order;
import com.rangel.orderservice.infrastructure.web.dto.CreateOrderRequest;
import com.rangel.orderservice.infrastructure.web.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderInputPort createOrderInputPort;
    private final GetOrderByIdInputPort getOrderByIdInputPort;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        CreateOrderCommand command = new CreateOrderCommand(
                request.customerId(),
                request.items().stream()
                        .map(item -> new CreateOrderCommand.CreateOrderItemCommand(
                                item.productId(),
                                item.quantity(),
                                item.unitPrice()
                        ))
                        .toList()
        );

        Order createdOrder = createOrderInputPort.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderResponse.from(createdOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable UUID id) {
        Order order = getOrderByIdInputPort.execute(id);
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
