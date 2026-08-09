package com.rangel.inventoryservice.infrastructure.persistence.repository;

import com.rangel.inventoryservice.application.port.out.StockRepositoryPort;
import com.rangel.inventoryservice.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StockRepositoryAdapter implements StockRepositoryPort {

    private final StockJpaRepository stockJpaRepository;

    @Override
    public Optional<Stock> findByProductId(String productId){
        return stockJpaRepository.findByProductId(productId);
    }

}
