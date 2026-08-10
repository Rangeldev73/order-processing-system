package com.rangel.inventoryservice.infrastructure.web.controller;

import com.rangel.inventoryservice.application.port.in.CheckStockAvailabilityInputPort;
import com.rangel.inventoryservice.infrastructure.web.dto.StockAvailabilityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final CheckStockAvailabilityInputPort checkStockAvailabilityInputPort;

    @GetMapping("/{productId}/availability")
    public ResponseEntity<StockAvailabilityResponse> checkAvailability(
            @PathVariable String productId,
            @RequestParam int quantity) {

        boolean isAvailable = checkStockAvailabilityInputPort.execute(productId, quantity);

        return ResponseEntity.ok(new StockAvailabilityResponse(isAvailable));
    }
}