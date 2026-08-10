package com.rangel.inventoryservice.application.usecase;

import com.rangel.inventoryservice.application.port.in.CheckStockAvailabilityInputPort;
import com.rangel.inventoryservice.application.port.out.StockRepositoryPort;
import com.rangel.inventoryservice.domain.exception.StockNotFoundException;
import com.rangel.inventoryservice.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckStockAvailabilityUseCase implements CheckStockAvailabilityInputPort {

    private final StockRepositoryPort stockRepositoryPort;

    @Override
    public boolean execute(String productId, int quantity) {
        Stock stock = stockRepositoryPort.findByProductId(productId)
                .orElseThrow(() -> new StockNotFoundException(productId));

        return stock.hasAvailableQuantity(quantity);
    }
}
