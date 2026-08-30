package com.rangel.paymentservice.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PaymentTest {

    @Test
    void shouldApprovePaymentWhenAmountIsLessThanLimit() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal amountUnderLimit = new BigDecimal("999.99");

        Payment payment = Payment.process(orderId, customerId, amountUnderLimit);

        assertNull(payment.getId());
        assertEquals(PaymentStatus.APPROVED, payment.getStatus());
        assertNull(payment.getRejectionReason());
        assertEquals(orderId, payment.getOrderId());
        assertEquals(customerId, payment.getCustomerId());
        assertEquals(amountUnderLimit, payment.getAmount());
    }

    @Test
    void shouldRejectPaymentWhenAmountIsExactlyAtLimit() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal exactLimit = new BigDecimal("1000.00");

        Payment payment = Payment.process(orderId, customerId, exactLimit);

        assertNull(payment.getId());
        assertEquals(PaymentStatus.REJECTED, payment.getStatus());
        assertEquals("Credit limit exceeded", payment.getRejectionReason());
    }

    @Test
    void shouldRejectPaymentWhenAmountExceedsLimit() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        BigDecimal amountExceedingLimit = new BigDecimal("1500.00");

        Payment payment = Payment.process(orderId, customerId, amountExceedingLimit);

        assertNull(payment.getId());
        assertEquals(PaymentStatus.REJECTED, payment.getStatus());
        assertEquals("Credit limit exceeded", payment.getRejectionReason());
    }
}