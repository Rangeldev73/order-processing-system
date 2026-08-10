package com.rangel.inventoryservice.application.port.in;

public interface CheckStockAvailabilityInputPort {
    boolean execute(String productId, int quantity);
}