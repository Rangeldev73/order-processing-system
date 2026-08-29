package com.rangel.orderservice.domain.model;

import com.rangel.orderservice.domain.exception.InvalidOrderException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {

    @Test
    void shouldCreateOrderWithPendingStatusesWhenItemsAreValid() {
        UUID customerId = UUID.randomUUID();
        List<OrderItem> items = List.of(new OrderItem("SKU-001", 2, new BigDecimal("50.00")));

        Order order = Order.create(customerId, items);

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(OrderStockStatus.PENDING, order.getStockStatus());
        assertEquals(OrderPaymentStatus.PENDING, order.getPaymentStatus());
    }

    @Test
    void shouldThrowInvalidOrderExceptionWhenItemsListIsEmpty() {
        UUID customerId = UUID.randomUUID();
        List<OrderItem> emptyItems = List.of();

        assertThrows(
                InvalidOrderException.class,
                () -> Order.create(customerId, emptyItems)
        );
    }

    @Test
    void shouldRecalculateTotalAmountCorrectlyWhenAddingItems() {
        UUID customerId = UUID.randomUUID();
        OrderItem item1 = new OrderItem("SKU-001", 2, new BigDecimal("50.00"));
        OrderItem item2 = new OrderItem("SKU-002", 1, new BigDecimal("30.00"));

        Order order = Order.create(customerId, List.of(item1, item2));

        assertEquals(new BigDecimal("130.00"), order.getTotalAmount());
        assertEquals(2, order.getItems().size());
    }

    @Test
    void shouldKeepStatusCreatedWhenOnlyStockIsReserved() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem("SKU-001", 1, new BigDecimal("100.00"));
        Order order = Order.create(customerId, List.of(item));

        order.markStockReserved();

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(OrderStockStatus.RESERVED, order.getStockStatus());
        assertEquals(OrderPaymentStatus.PENDING, order.getPaymentStatus());
    }

    @Test
    void shouldKeepStatusCreatedWhenOnlyPaymentIsApproved() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem("SKU-001", 1, new BigDecimal("100.00"));
        Order order = Order.create(customerId, List.of(item));

        order.markPaymentApproved();

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(OrderStockStatus.PENDING, order.getStockStatus());
        assertEquals(OrderPaymentStatus.APPROVED, order.getPaymentStatus());
    }

    @Test
    void shouldApproveOrderWhenStockReservedThenPaymentApproved() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem("SKU-001", 1, new BigDecimal("100.00"));
        Order order = Order.create(customerId, List.of(item));

        order.markStockReserved();
        order.markPaymentApproved();

        assertEquals(OrderStatus.APPROVED, order.getStatus());
        assertEquals(OrderStockStatus.RESERVED, order.getStockStatus());
        assertEquals(OrderPaymentStatus.APPROVED, order.getPaymentStatus());
    }

    @Test
    void shouldApproveOrderWhenPaymentApprovedThenStockReserved() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem("SKU-001", 1, new BigDecimal("100.00"));
        Order order = Order.create(customerId, List.of(item));

        order.markPaymentApproved();
        order.markStockReserved();

        assertEquals(OrderStatus.APPROVED, order.getStatus());
        assertEquals(OrderStockStatus.RESERVED, order.getStockStatus());
        assertEquals(OrderPaymentStatus.APPROVED, order.getPaymentStatus());
    }

    @Test
    void shouldRejectOrderWithInsufficientStockReasonWhenStockFails() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem("SKU-001", 1, new BigDecimal("100.00"));
        Order order = Order.create(customerId, List.of(item));

        order.markStockFailed();

        assertEquals(OrderStatus.REJECTED, order.getStatus());
        assertEquals(OrderStockStatus.FAILED, order.getStockStatus());
        assertEquals(RejectionReason.INSUFFICIENT_STOCK, order.getRejectionReason());
    }

    @Test
    void shouldRejectOrderWithPaymentDeclinedReasonWhenPaymentIsRejected() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem("SKU-001", 1, new BigDecimal("100.00"));
        Order order = Order.create(customerId, List.of(item));

        order.markPaymentRejected();

        assertEquals(OrderStatus.REJECTED, order.getStatus());
        assertEquals(OrderPaymentStatus.REJECTED, order.getPaymentStatus());
        assertEquals(RejectionReason.PAYMENT_DECLINED, order.getRejectionReason());
    }

    @Test
    void shouldRejectOrderWhenStockIsReservedButPaymentIsRejected() {
        UUID customerId = UUID.randomUUID();
        OrderItem item = new OrderItem("SKU-001", 1, new BigDecimal("100.00"));
        Order order = Order.create(customerId, List.of(item));

        order.markStockReserved();
        order.markPaymentRejected();

        assertEquals(OrderStatus.REJECTED, order.getStatus());
        assertEquals(OrderStockStatus.RESERVED, order.getStockStatus());
        assertEquals(OrderPaymentStatus.REJECTED, order.getPaymentStatus());
        assertEquals(RejectionReason.PAYMENT_DECLINED, order.getRejectionReason());
    }
}