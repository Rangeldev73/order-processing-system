package com.rangel.inventoryservice.domain.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String productId) {
        super("Stock not found for product id: " + productId);
    }
}