package com.microservices.ordersystem.order_service.client;

import com.microservices.ordersystem.order_service.dto.UserDto;
import com.microservices.ordersystem.order_service.exceptions.ProductNotFoundException;
import com.microservices.ordersystem.order_service.exceptions.ServiceUnavailableException;
import com.microservices.ordersystem.order_service.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    public void isValid(Long id) {
        this.restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new UserNotFoundException("User not found: " + id);
                        })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ServiceUnavailableException("User Service is unavailable");
                })
                .toBodilessEntity();
    }
}
