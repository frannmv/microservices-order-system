package com.microservices.ordersystem.order_service.client;

import com.microservices.ordersystem.order_service.exceptions.InventoryNotFoundException;
import com.microservices.ordersystem.order_service.exceptions.ServiceUnavailableException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Component
public class InventoryRestClient {

    private final RestClient client;

    public InventoryRestClient() {
        this.client = RestClient.builder()
                .baseUrl("http://inventory-service:8080")
                .build();
    }

    public void updateStock(Long productId, Integer quantity) {
        this.client.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/inventory/{productId}/decrease")
                        .queryParam("quantity",quantity)
                        .build(productId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new InventoryNotFoundException("Inventory not found for product: " + productId);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ServiceUnavailableException("Inventory Service is unavailable");
                })
                .toBodilessEntity();
    }
}
