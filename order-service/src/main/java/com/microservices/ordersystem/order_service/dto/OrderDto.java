package com.microservices.ordersystem.order_service.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDto {

    private Long id;
    private Long customerId;
    private List<OrderItemDto> items;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;

    public OrderDto() {};

    public OrderDto(Long id, Long customerId,  List<OrderItemDto> items, BigDecimal total, String status, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
        this.calculateTotal();
    }

    private void calculateTotal() {
        this.total = items.stream().map(OrderItemDto::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
