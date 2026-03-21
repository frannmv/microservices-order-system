package com.microservices.ordersystem.order_service.dto.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private Long id;
    private Long customerId;
    private List<OrderItemResponse> items;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;

    public OrderResponse() {};

    public OrderResponse(Long id, Long customerId, List<OrderItemResponse> items, BigDecimal total, String status, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
        this.calculateTotal();
    }

    private void calculateTotal() {
        this.total = items.stream().map(OrderItemResponse::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
