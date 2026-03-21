package com.microservices.ordersystem.order_service.dto.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CreateOrderRequest {

    private Long customerId;
    private List<OrderItemRequest> items;

    public CreateOrderRequest() {
        this.items = new ArrayList<>();
    };

    public CreateOrderRequest(Long customerId, List<OrderItemRequest> items) {
        this.customerId = customerId;
        this.items = items;
    }
}
