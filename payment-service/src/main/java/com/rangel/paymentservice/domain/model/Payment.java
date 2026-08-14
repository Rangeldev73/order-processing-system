package com.rangel.paymentservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_payment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private static final BigDecimal PAYMENT_LIMIT = new BigDecimal("1000.00");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = true)
    private String rejectionReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Payment process(UUID orderId, UUID customerId, BigDecimal amount) {
        boolean isApproved = amount.compareTo(PAYMENT_LIMIT) < 0;
        PaymentStatus status = isApproved ? PaymentStatus.APPROVED : PaymentStatus.REJECTED;

        String rejectionReason = isApproved ? null : "Credit limit exceeded";

        return new Payment(
                null,
                orderId,
                customerId,
                amount,
                status,
                rejectionReason,
                LocalDateTime.now()
        );
    }
}
