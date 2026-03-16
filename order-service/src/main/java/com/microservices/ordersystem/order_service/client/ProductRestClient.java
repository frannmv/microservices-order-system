package com.microservices.ordersystem.order_service.client;

import com.microservices.ordersystem.order_service.dto.ProductDto;
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

    public ProductDto findById(Long id) {
        return this.client.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(ProductDto.class);
    }
}
