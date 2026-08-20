package com.rangel.inventoryservice.application.usecase;

import com.rangel.inventoryservice.application.port.out.StockRepositoryPort;
import com.rangel.inventoryservice.domain.event.OrderCreatedEvent;
import com.rangel.inventoryservice.domain.exception.StockNotFoundException;
import com.rangel.inventoryservice.domain.model.Stock;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockReservationTransactionalService {

    private final StockRepositoryPort stockRepositoryPort;

    @Transactional
    public void reserveAllItems(OrderCreatedEvent event) {
        for (var item : event.items()) {
            Stock stock = stockRepositoryPort.findByProductId(item.productId())
                    .orElseThrow(() -> new StockNotFoundException(item.productId()));

            stock.reserve(item.quantity());
            stockRepositoryPort.save(stock);
        }
    }
}