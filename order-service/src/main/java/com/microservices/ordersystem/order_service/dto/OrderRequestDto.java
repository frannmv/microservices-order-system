package com.microservices.ordersystem.order_service.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderRequestDto {

    private Long customerId;
    private List<OrderItemRequestDto> items;

    public OrderRequestDto() {
        this.items = new ArrayList<>();
    };

    public OrderRequestDto(Long customerId, List<OrderItemRequestDto> items) {
        this.customerId = customerId;
        this.items = items;
    }
}
