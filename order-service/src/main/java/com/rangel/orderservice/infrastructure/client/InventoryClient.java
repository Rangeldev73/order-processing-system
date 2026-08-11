package com.rangel.orderservice.infrastructure.client;

import com.rangel.orderservice.application.port.out.StockAvailabilityPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class InventoryClient implements StockAvailabilityPort {

    private final RestClient restClient;

    public InventoryClient(@Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .build();
    }

    @Override
    public boolean isAvailable(String productId, int quantity) {
        StockAvailabilityResponse response = restClient.get()
                .uri("/stocks/{productId}/availability?quantity={quantity}", productId, quantity)
                .retrieve()
                .body(StockAvailabilityResponse.class);

        return response != null && response.available();
    }

    private record StockAvailabilityResponse(boolean available) {}
}