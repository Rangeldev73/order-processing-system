package com.rangel.inventoryservice.application.port.out;

import com.rangel.inventoryservice.domain.model.Stock;
import java.util.Optional;

public interface StockRepositoryPort {
    Optional<Stock> findByProductId(String productId);
}
