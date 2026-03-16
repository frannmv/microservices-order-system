package com.microservices.ordersystem.order_service.client;

import com.microservices.ordersystem.order_service.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UserRestClient {

    private final RestClient restClient;

    public UserRestClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://user-service:8080")
                .build();
    }

    public UserDto findById(Long userId) {
        return this.restClient.get()
                .uri("/users/{id}",userId)
                .retrieve()
                .body(UserDto.class);
    }
}
