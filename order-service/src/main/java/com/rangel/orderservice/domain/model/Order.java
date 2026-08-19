package com.rangel.orderservice.domain.model;
import com.rangel.orderservice.domain.exception.InvalidOrderException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_order")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStockStatus stockStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
        private OrderPaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private RejectionReason rejectionReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    @Builder
    public Order(UUID customerId) {
        this.customerId = customerId;
        this.status = OrderStatus.CREATED;
        this.stockStatus = OrderStockStatus.PENDING;
        this.paymentStatus = OrderPaymentStatus.PENDING;
        this.totalAmount = BigDecimal.ZERO;
        this.items = new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static Order create(UUID customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidOrderException("Order must contain at least one item");
        }
        Order order = Order.builder().customerId(customerId).build();
        items.forEach(order::addItem);
        return order;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markAsAwaitingPayment() {
        this.status = OrderStatus.AWAITING_PAYMENT;
    }

    public void markStockReserved() {
        this.stockStatus = OrderStockStatus.RESERVED;
        evaluateFinalStatus();
    }

    public void markStockFailed() {
        this.stockStatus = OrderStockStatus.FAILED;
        this.status = OrderStatus.REJECTED;
        this.rejectionReason = RejectionReason.INSUFFICIENT_STOCK;
    }

    public void markPaymentApproved() {
        this.paymentStatus = OrderPaymentStatus.APPROVED;
        evaluateFinalStatus();
    }

    public void markPaymentRejected() {
        this.paymentStatus = OrderPaymentStatus.REJECTED;
        this.status = OrderStatus.REJECTED;
        this.rejectionReason = RejectionReason.PAYMENT_DECLINED;
    }

    private void evaluateFinalStatus() {
        if (this.stockStatus == OrderStockStatus.RESERVED && this.paymentStatus == OrderPaymentStatus.APPROVED) {
            this.status = OrderStatus.APPROVED;
        }
    }
}