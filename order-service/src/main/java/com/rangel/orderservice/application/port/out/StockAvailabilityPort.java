package com.rangel.orderservice.application.port.out;

public interface StockAvailabilityPort {
    boolean isAvailable(String productId, int quantity);
}