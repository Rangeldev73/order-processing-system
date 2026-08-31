package com.rangel.orderservice.infrastructure.client;

import com.rangel.orderservice.application.port.out.StockAvailabilityPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class InventoryClient implements StockAvailabilityPort {

    private final RestClient restClient;

    public InventoryClient(@Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .build();
    }

    @Override
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "isAvailableFallback")
    @Retry(name = "inventoryService")
    public boolean isAvailable(String productId, int quantity) {
        StockAvailabilityResponse response = restClient.get()
                .uri("/stocks/{productId}/availability?quantity={quantity}", productId, quantity)
                .retrieve()
                .body(StockAvailabilityResponse.class);

        return response != null && response.available();
    }

    private boolean isAvailableFallback(String productId, int quantity, Throwable throwable) {
        throw new RestClientException("Inventory service is currently unavailable after retries", throwable);
    }

    private record StockAvailabilityResponse(boolean available) {}
}