package com.rangel.inventoryservice.application.usecase;

import com.rangel.inventoryservice.application.port.in.ReserveStockInputPort;
import com.rangel.inventoryservice.application.port.out.StockEventPublisherPort;
import com.rangel.inventoryservice.domain.event.OrderCreatedEvent;
import com.rangel.inventoryservice.domain.event.StockReservationFailedEvent;
import com.rangel.inventoryservice.domain.event.StockReservedEvent;
import com.rangel.inventoryservice.domain.exception.InsufficientStockException;
import com.rangel.inventoryservice.domain.exception.StockNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveStockUseCase implements ReserveStockInputPort {

    private final StockReservationTransactionalService transactionalService;
    private final StockEventPublisherPort stockEventPublisherPort;

    @Override
    public void execute(OrderCreatedEvent event) {
        try {
            transactionalService.reserveAllItems(event);
            stockEventPublisherPort.publishReserved(new StockReservedEvent(event.orderId()));

        } catch (DataIntegrityViolationException ex) {
            log.warn("Duplicate OrderCreatedEvent for orderId: {}. Ignoring.", event.orderId());

        } catch (StockNotFoundException | InsufficientStockException ex) {
            stockEventPublisherPort.publishFailed(
                    new StockReservationFailedEvent(event.orderId(), ex.getMessage())
            );
        }
    }
}