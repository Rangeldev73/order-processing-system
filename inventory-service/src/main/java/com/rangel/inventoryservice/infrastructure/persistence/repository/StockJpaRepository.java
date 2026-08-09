package com.rangel.inventoryservice.infrastructure.persistence.repository;

import com.rangel.inventoryservice.domain.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockJpaRepository extends JpaRepository<Stock, UUID> {
    Optional<Stock> findByProductId(String productId);
}
