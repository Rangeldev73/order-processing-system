package com.rangel.orderservice.domain.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productId) {
        super("Insufficient stock for product id: " + productId);
    }
}