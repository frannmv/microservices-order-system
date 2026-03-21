package com.microservices.ordersystem.order_service.dto;

import lombok.Getter;

@Getter
public class OrderItemRequestDto {

    private Long productId;
    private Integer quantity;

    public OrderItemRequestDto() {};

    public OrderItemRequestDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
