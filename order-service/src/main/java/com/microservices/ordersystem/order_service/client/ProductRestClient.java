package com.microservices.ordersystem.order_service.client;

import com.microservices.ordersystem.order_service.dto.ProductDto;
import com.microservices.ordersystem.order_service.exceptions.ProductNotFoundException;
import com.microservices.ordersystem.order_service.exceptions.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductRestClient {

    private final RestClient client;

    public ProductRestClient() {
        this.client = RestClient.builder()
                .baseUrl("http://product-service:8080")
                .build();
    }

    public void isValid(Long id) {
        this.client.get()
                .uri("/products/{id}/available", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new ProductNotFoundException("Product not found or not available: " + id);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ServiceUnavailableException("Product Service is unavailable");
                })
                .toBodilessEntity();
    }
}
