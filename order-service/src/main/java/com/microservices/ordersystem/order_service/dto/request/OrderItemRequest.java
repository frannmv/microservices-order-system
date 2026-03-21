package com.microservices.ordersystem.order_service.dto.request;

import lombok.Getter;

@Getter
public class OrderItemRequest {

    private Long productId;
    private Integer quantity;

    public OrderItemRequest() {};

    public OrderItemRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
