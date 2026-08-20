package com.rangel.inventoryservice.domain.model;

import com.rangel.inventoryservice.domain.exception.InsufficientStockException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "tb_stock")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String productId;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Version
    private Long version;

    public boolean hasAvailableQuantity(int requestedQuantity) {
        return this.availableQuantity != null && this.availableQuantity >= requestedQuantity;
    }

    public void reserve(int quantity) {
        if (!hasAvailableQuantity(quantity)) {
            throw new InsufficientStockException(productId);
        }
        this.availableQuantity -= quantity;
    }
}