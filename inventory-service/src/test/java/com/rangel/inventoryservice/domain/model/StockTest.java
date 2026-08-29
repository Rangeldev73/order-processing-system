package com.rangel.inventoryservice.domain.model;

import com.rangel.inventoryservice.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class StockTest {

    @Test
    void shouldReturnTrueWhenAvailableQuantityIsSufficient() {
        Stock stock = Stock.builder()
                .productId("SKU-001")
                .availableQuantity(10)
                .build();

        boolean result = stock.hasAvailableQuantity(5);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenAvailableQuantityIsInsufficient() {
        Stock stock = Stock.builder()
                .productId("SKU-001")
                .availableQuantity(10)
                .build();

        boolean result = stock.hasAvailableQuantity(15);

        assertFalse(result);
    }

    @Test
    void shouldDeductQuantityWhenStockIsSufficient() {
        Stock stock = Stock.builder()
                .productId("SKU-001")
                .availableQuantity(10)
                .build();

        stock.reserve(3);

        assertEquals(7, stock.getAvailableQuantity());
    }

    @Test
    void shouldThrowInsufficientStockExceptionAndKeepQuantityUnchangedWhenStockIsInsufficient() {
        // Arrange
        Stock stock = Stock.builder()
                .productId("SKU-001")
                .availableQuantity(10)
                .build();

        assertThrows(
                InsufficientStockException.class,
                () -> stock.reserve(15)
        );

        assertEquals(10, stock.getAvailableQuantity());
    }
}